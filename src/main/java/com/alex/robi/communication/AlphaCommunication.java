package com.alex.robi.communication;

import static com.alex.robi.communication.Message.messageWithPayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaCommunication implements Communication {

    private final static Logger LOG = LoggerFactory.getLogger(Communication.class);

    private Sending sending;
    private ResponseReader responseReader;

    public AlphaCommunication(Sending sending, InputStream inputStream) {
        this.sending = sending;

        responseReader = new ResponseReader(inputStream);
        new Thread(responseReader).start();
    }

    @Override
    public <T> T send(Payload payload, ResponseFactory<T> responseFactory) throws CommunicationException {
        try {
            Message message = messageWithPayload(payload);
            LOG.debug("sending {}, message: \n{}", payload.command(), message);
            ResponseWaiter waitFor = responseReader.waitFor(payload.command());
            message.send(sending);
            List<Message> responseMessages = waitFor.take();
            LOG.debug("received {}", responseMessages.stream().map(m -> m.toString()).collect(Collectors.joining(",", "[", "]")));
            return responseFactory.create(responseMessages.stream().map(Message::payload).collect(Collectors.toList()));
        } catch (IOException | InterruptedException e) {
            throw new CommunicationException("Error during sending message to robi", e);
        }
    }

    public void close() throws CommunicationException {
        try {
            sending.close();
            responseReader.stop();
        } catch (IOException e) {
            throw new CommunicationException("Error closing connection to robi", e);
        }
    }
}
