package com.alex.robi.communication;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class PayloadTest {

    @Test
    void toMessage() {
        Message messageFromPayload = Payload.payload(Command.BTHandshake, Parameter.of(1)).toMessage();

        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(6)
            .withCommand(1)
            .withParameters(new int[] { 1 })
            .withCheck(8)
            .withEndCharacter(237)
            .build();

        assertThat(messageFromPayload, equalTo(expectedMessage));
    }

}
