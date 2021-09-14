package com.alex.robi.communication;

import java.io.IOException;

public interface Sending {

    void send(int[] message) throws IOException;

    void close() throws IOException;

}
