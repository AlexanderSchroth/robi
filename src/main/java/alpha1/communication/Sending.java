package alpha1.communication;

import java.io.IOException;

public interface Sending {

    void send(int[] message) throws IOException;

    void close() throws IOException;

}
