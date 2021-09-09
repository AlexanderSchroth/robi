package com.alex.robi;

import static com.alex.robi.Message.messageWithPayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaCommunication implements Communication {

    private final static Logger LOG = LoggerFactory.getLogger(Message.class);

    private Sending sending;
    private ResponseReader responseReader;

    public AlphaCommunication(Sending sending, InputStream inputStream) {
        this.sending = sending;

        responseReader = new ResponseReader(inputStream);
        new Thread(responseReader).start();
    }

    public static class ResponseWaiter {
        private Command command;
        private BlockingQueue<List<Message>> q;

        private List<Message> messages;

        ResponseWaiter(Command command) {
            this.command = command;
            this.messages = new ArrayList<>();
            this.q = new ArrayBlockingQueue<List<Message>>(1);
        }

        public List<Message> take() throws InterruptedException {
            return q.take();
        }

        public boolean add(Message m) {
            messages.add(m);
            if (command.expectedResponseMessages() == messages.size()) {
                q.add(messages);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public <T> T send(Payload payload, ResponseFactory<T> responseFactory) throws CommunicationException {
        try {
            Message message = messageWithPayload(payload);
            LOG.debug("sending {}", message);
            ResponseWaiter waitFor = responseReader.waitFor(payload.command());
            message
                .send(sending);

            List<Message> responseMessages = waitFor.take();
            LOG.debug("received {}", responseMessages.stream().map(m -> m.toString()).collect(Collectors.joining(",", "[", "]")));
            return responseFactory.create(responseMessages);
        } catch (IOException | InterruptedException e) {
            throw new CommunicationException("Error during sending message to robi", e);
        }
    }

    public void close() throws CommunicationException {
        try {
            sending.close();
            responseReader.stop();
        } catch (IOException e) {
            throw new CommunicationException("Error during sending message to robi", e);
        }
    }
}
