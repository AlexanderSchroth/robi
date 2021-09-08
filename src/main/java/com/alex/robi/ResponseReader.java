package com.alex.robi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResponseReader implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(ResponseReader.class);

    private InputStream openDataInputStream;

    private boolean commandHeader1Received = false;
    private boolean commandHeader2Received = false;

    private boolean endCommandReceived = false;

    private List<Integer> partialMessage;

    private boolean run;

    ResponseReader(InputStream openDataInputStream) throws IOException {
        this.openDataInputStream = openDataInputStream;
        this.run = true;
        this.partialMessage = new ArrayList<>();
    }

    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        while (run) {
            try {
                read();
                Thread.sleep(100);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void read() throws IOException {
        int available = openDataInputStream.available();
        int[] b = new int[available];

        for (int i = 0; i < b.length; i++) {
            b[i] = openDataInputStream.read();
            if (b[i] == Message.COMMAND_HEADER_1) {
                commandHeader1Received = true;
            }
            if (b[i] == Message.COMMAND_HEADER_2) {
                commandHeader2Received = true;
            }
            if (b[i] == Message.END_CHARACTER) {
                endCommandReceived = true;
            }
            partialMessage.add(b[i]);

            if (commandHeader1Received && commandHeader2Received && endCommandReceived) {
                Message message = new Message.FromBytesBuilder().withBytes(partialMessage.stream().mapToInt(integer -> integer).toArray()).build();
                partialMessage.clear();
                commandHeader1Received = false;
                commandHeader2Received = false;
                endCommandReceived = false;
                LOG.debug("received {}", message);
            }
        }
    }
}