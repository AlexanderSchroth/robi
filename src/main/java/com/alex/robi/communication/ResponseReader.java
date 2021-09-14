package com.alex.robi.communication;

import com.alex.robi.communication.AlphaCommunication.ResponseWaiter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResponseReader implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(ResponseReader.class);

    private InputStream inputStream;

    private boolean commandHeader1Received = false;
    private boolean commandHeader2Received = false;

    private boolean endCommandReceived = false;

    private List<Integer> partialMessage;

    private boolean run;

    private Map<Command, ResponseWaiter> waiters;

    ResponseReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.run = true;
        this.partialMessage = new ArrayList<>();
        this.waiters = new HashMap<>();
    }

    public ResponseWaiter waitFor(Command c) {
        if (waiters.containsKey(c)) {
            throw new IllegalArgumentException("Someone already waits for <" + c + "> to return");
        }
        ResponseWaiter responseWaiter = new ResponseWaiter(c);
        waiters.put(c, responseWaiter);
        return responseWaiter;
    }

    public void stop() throws IOException {
        run = false;
        inputStream.close();
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
        int available = inputStream.available();
        int[] b = new int[available];

        for (int i = 0; i < b.length; i++) {
            b[i] = inputStream.read();
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

                ResponseWaiter responseWaiter = waiters.get(message.command());
                if (responseWaiter == null) {
                    LOG.debug("No one waits for answer of command {}. Message:{}", message.command(), message);
                } else {
                    if (responseWaiter.add(message)) {
                        waiters.remove(message.command());
                    }
                }

                partialMessage.clear();
                commandHeader1Received = false;
                commandHeader2Received = false;
                endCommandReceived = false;
            }
        }
    }
}