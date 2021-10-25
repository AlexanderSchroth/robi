package alpha1.communication;

import alpha1.communication.MessageProducer.MessageConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaReciving implements Receiving {

    private final static Logger LOG = LoggerFactory.getLogger(AlphaReciving.class);

    private InputStream inputStream;
    private MessageProducer messageProducer;
    private boolean run;

    private Map<Command, ResponseWaiter> waiters;

    public AlphaReciving(InputStream inputStream) {
        this.inputStream = inputStream;
        this.waiters = new HashMap<>();
        this.messageProducer = new MessageProducer(new Consumer());

        this.run = true;
        new Thread(new ReciveRunable()).start();
    }

    private class Consumer implements MessageConsumer {
        @Override
        public void received(Message message) {
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
    }

    private class ReciveRunable implements Runnable {

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
                messageProducer.received(Parameter.of(inputStream.read()));
            }
        }
    }

    public ResponseWaiter waitFor(Command c) {
        if (waiters.containsKey(c)) {
            throw new IllegalArgumentException("Someone already is waiting for <" + c + "> to return");
        }
        ResponseWaiter responseWaiter = new ResponseWaiter(c);
        waiters.put(c, responseWaiter);
        return responseWaiter;
    }

    public void close() throws IOException {
        run = false;
        inputStream.close();
    }

}
