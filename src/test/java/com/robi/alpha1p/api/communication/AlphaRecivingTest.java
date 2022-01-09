package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AlphaRecivingTest {

    private static Message BT_HANDSHAKE_RESPONSE = new Message.Builder()
        .withCommandHeader1(of(251))
        .withCommandHeader2(of(191))
        .withLength(of(6))
        .withCommand(of(1))
        .withParameters(of(1))
        .withCheck(of(8))
        .withEndCharacter(of(237))
        .build();

    @Test
    void messageReceived() throws InterruptedException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new MessageToByteArray(BT_HANDSHAKE_RESPONSE).toByteArray());

        Receiving alphaReciving = new AlphaReciving(inputStream);

        ResponseWaiter waitFor = alphaReciving.waitFor(Command.BTHandshake);

        assertThat(waitFor.take(), Matchers.hasItem(BT_HANDSHAKE_RESPONSE));
        alphaReciving.close();
    }

    @Test
    void responseWaiterAlreadyRegistered() throws InterruptedException, IOException {
        Receiving alphaReciving = new AlphaReciving(new ByteArrayInputStream(new byte[] {}));

        alphaReciving.waitFor(Command.BTHandshake);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            alphaReciving.waitFor(Command.BTHandshake);
        });
        alphaReciving.close();
    }

    private static class MessageToByteArray {
        private Message message;
        private byte[] byteArray;

        public MessageToByteArray(Message message) {
            this.message = message;
        }

        byte[] toByteArray() {
            try {
                message.send(new Sending() {
                    @Override
                    public void send(int[] message) throws IOException {
                        MessageToByteArray.this.byteArray = new byte[message.length];
                        for (int i = 0; i < message.length; i++) {
                            byteArray[i] = Integer.valueOf(message[i]).byteValue();
                        }
                    }

                    @Override
                    public void close() throws IOException {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return byteArray;
        }
    }
}
