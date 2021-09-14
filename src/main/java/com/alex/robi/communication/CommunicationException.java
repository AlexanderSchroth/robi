package com.alex.robi.communication;

public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }

}
