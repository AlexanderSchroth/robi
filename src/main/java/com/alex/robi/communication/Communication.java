package com.alex.robi.communication;

import java.util.List;

public interface Communication {

    <T> T send(Payload payload, ResponseFactory<T> responseFactory) throws CommunicationException;

    void close() throws CommunicationException;

    void open() throws CommunicationException;

    public static interface ResponseFactory<T> {

        T create(List<Payload> responsePayloads);
    }
}
