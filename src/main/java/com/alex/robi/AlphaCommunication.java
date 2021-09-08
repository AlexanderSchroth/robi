package com.alex.robi;

import static com.alex.robi.Message.messageWithPayload;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaCommunication implements Communication {

    private final static Logger LOG = LoggerFactory.getLogger(Message.class);

    private Connection connection;

    public AlphaCommunication(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void send(Payload payload) throws CommunicationException {
        try {
            Message message = messageWithPayload(payload);
            LOG.debug("sending {}", message);
            message
                .send(connection);
        } catch (IOException e) {
            throw new CommunicationException("Error during sending message to robi", e);
        }
    }
}
