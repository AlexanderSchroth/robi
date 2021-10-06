package com.alex.robi.communication;

import static com.alex.robi.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class PayloadTest {

    @Test
    void toMessage() {
        Message messageFromPayload = Payload.payload(Command.BTHandshake, Parameter.of(1)).toMessage();

        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(of(251))
            .withCommandHeader2(of(191))
            .withLength(of(6))
            .withCommand(of(1))
            .withParameters(of(1))
            .withCheck(of(8))
            .withEndCharacter(of(237))
            .build();

        assertThat(messageFromPayload, equalTo(expectedMessage));
    }

}
