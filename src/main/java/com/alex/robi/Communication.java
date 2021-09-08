package com.alex.robi;

public interface Communication {

    void send(Payload payload) throws CommunicationException;

}
