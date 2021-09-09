package com.alex.robi;

import java.util.List;

public interface Communication {

    <T> T send(Payload payload, ResponseFactory<T> responseFactory) throws CommunicationException;

    void close() throws CommunicationException;

    public static interface ResponseFactory<T> {

        T create(List<Message> message);
    }
}
