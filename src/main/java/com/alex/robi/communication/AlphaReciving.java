package com.alex.robi.communication;

import com.alex.robi.communication.MessageProducer.MessageConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaReciving implements Receiving, Runnable, MessageConsumer {

    private final static Logger LOG = LoggerFactory.getLogger(AlphaReciving.class);

    private InputStream inputStream;
    private MessageProducer readStage;
    private boolean run;

    private Map<Command, ResponseWaiter> waiters;

    public AlphaReciving(InputStream inputStream) {
        this.inputStream = inputStream;
        this.waiters = new HashMap<>();
        this.readStage = new MessageProducer(this);
    }

    @Override
    public void accept(Message message) {
        ResponseWaiter responseWaiter = waiters.get(message.command());
        if (responseWaiter == null) {
            LOG.debug("No one waits for answer of command {}. Message:{}", message.command(), message);
        } else {
            responseWaiter.add(message);
            if (responseWaiter.complete()) {
                waiters.remove(message.command());
            }
        }
    }

    public ResponseWaiter waitFor(Command c) {
        if (waiters.containsKey(c)) {
            throw new IllegalArgumentException("Someone already is waiting for <" + c + "> to return");
        }
        return waiters.put(c, new ResponseWaiter(c));
    }

    public void stop() throws IOException {
        run = false;
        inputStream.close();
    }

    @Override
    public void start() {
        this.run = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (run) {
            try {
                read();
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                LOG.error("Error while reading", e);
            }
        }
    }

    private void read() throws IOException {
        int available = inputStream.available();
        int[] b = new int[available];
        for (int i = 0; i < b.length; i++) {
            readStage.received(inputStream.read());
        }
    }
}
