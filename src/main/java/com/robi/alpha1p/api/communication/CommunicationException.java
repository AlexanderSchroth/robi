package com.robi.alpha1p.api.communication;

public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }

}
