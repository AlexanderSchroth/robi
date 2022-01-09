package com.robi.alpha1p.api.communication;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ResponseWaiterTest {

    @Nested
    class complete {
        @Test
        void completeTrue() {
            ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);
            responseWaiter.add(new Message.Builder().build());

            assertThat(responseWaiter.complete(), is(true));
        }

        @Test
        void completeFalseNoMessageReceived() {
            ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);

            assertThat(responseWaiter.complete(), is(false));
        }

        @Test
        void compleateFalseOneMessageReceived() {
            ResponseWaiter responseWaiter = new ResponseWaiter(Command.ReadingRobotState);
            responseWaiter.add(new Message.Builder().build());

            assertThat(responseWaiter.complete(), is(false));
        }
    }

    @Nested
    class take {
        @Test
        void takeReturnsImmediately() throws Exception {
            ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);
            Message testMessage = new Message.Builder().build();
            responseWaiter.add(testMessage);

            List<Message> receivedMessages = responseWaiter.take();

            assertThat(receivedMessages, hasItem(testMessage));
        }

        @Test
        void takeDelayed() throws Exception {
            ResponseWaiter responseWaiter = new ResponseWaiter(Command.BTHandshake);
            Message testMessage = new Message.Builder().build();
            new Thread(new DelayedMessage(responseWaiter, testMessage)).start();

            List<Message> receivedMessages = responseWaiter.take();

            assertThat(receivedMessages, hasItem(testMessage));
        }
    }

    private class DelayedMessage implements Runnable {

        private ResponseWaiter responseWaiter;
        private Message message;

        public DelayedMessage(ResponseWaiter responseWaiter, Message message) {
            this.responseWaiter = responseWaiter;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                sleep(500);
                responseWaiter.add(message);
            } catch (InterruptedException e) {
                fail(e);
            }
        }
    }

}
