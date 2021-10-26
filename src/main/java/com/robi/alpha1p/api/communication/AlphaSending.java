package com.robi.alpha1p.api.communication;

import java.io.IOException;
import java.io.OutputStream;

public class AlphaSending implements Sending {

    private OutputStream outputStream;

    public AlphaSending(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void send(int[] message) throws IOException {
        for (int i = 0; i < message.length; i++) {
            outputStream.write(message[i]);
        }
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
