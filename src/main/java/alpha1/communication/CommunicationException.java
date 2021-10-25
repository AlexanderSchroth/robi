package alpha1.communication;

public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }

}
