package com.alex.robi.communication;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class MessageTest {

    @Test
    void builderFromBytes() {
        Message messageFromBytes = Message.messageFromBytes(new int[] { 251, 191, 16, 1, 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68, 101, 237 });
        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(16)
            .withCommand(1)
            .withParameters(new int[] { 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68 })
            .withCheck(101)
            .withEndCharacter(237)
            .builder();
        assertThat(messageFromBytes, equalTo(expectedMessage));
    }

    @Test
    void builderFromBytesWithLargeParameters() {
        Message messageFromBytes = Message
            .messageFromBytes(new int[] { 251, 191, 15, 49, 98, 116, 95, 108, 105, 110, 107, 49, 50, 51, 185, 237 });
        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(15)
            .withCommand(49)
            .withParameters(new int[] { 98, 116, 95, 108, 105, 110, 107, 49, 50, 51 })
            .withCheck(185)
            .withEndCharacter(237)
            .builder();
        assertThat(messageFromBytes, equalTo(expectedMessage));
    }

    @Test
    void builderFromPayload() {
        Message messageFromPayload = Message.messageWithPayload(Payload.payload(Command.BTHandshake, Parameter.of(1)));

        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(6)
            .withCommand(1)
            .withParameters(new int[] { 1 })
            .withCheck(8)
            .withEndCharacter(237)
            .builder();

        assertThat(messageFromPayload, equalTo(expectedMessage));
    }
}
