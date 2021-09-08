package com.alex.robi;

import java.io.IOException;
import java.io.OutputStream;

public class AlphaConnection implements Connection {

    private OutputStream outputStream;

    public AlphaConnection(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void send(int[] message) throws IOException {
        for (int i = 0; i < message.length; i++) {
            outputStream.write(message[i]);
        }
        outputStream.flush();
    }
}
