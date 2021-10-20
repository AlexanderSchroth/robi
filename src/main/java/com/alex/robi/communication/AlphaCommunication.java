package com.alex.robi.communication;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaCommunication implements Communication {

    private final static Logger LOG = LoggerFactory.getLogger(Communication.class);

    private Sending sending;
    private Receiving receiving;

    public AlphaCommunication(Sending sending, Receiving receiving) {
        this.sending = sending;
        this.receiving = receiving;
    }

    @Override
    public <T> T send(Payload payload, ResponseFactory<T> responseFactory) throws CommunicationException {
        try {
            Message message = payload.toMessage();
            LOG.debug("sending {}, message: \n{}", payload.command(), message);
            ResponseWaiter waitFor = receiving.waitFor(payload.command());
            message.send(sending);
            List<Message> responseMessages = waitFor.take();
            LOG.debug("received {}", responseMessages.stream().map(m -> m.toString()).collect(Collectors.joining(",", "[", "]")));
            return responseFactory.create(responseMessages.stream()
                .map(Message::payload)
                .collect(toList()));
        } catch (IOException | InterruptedException e) {
            throw new CommunicationException("Error during sending message to robi", e);
        }
    }

    @Override
    public void open() throws CommunicationException {
        receiving.start();
    }

    public void close() throws CommunicationException {
        try {
            receiving.stop();
            sending.close();
        } catch (IOException e) {
            throw new CommunicationException("Error closing connection to robi", e);
        }
    }
}
