package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.robi.alpha1p.api.function.Angle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PayloadTest {

    @Test
    void parameters() {
        Payload payload = Payload.payload(Command.BTHandshake, Parameters.asParameters("abc"));

        assertThat(payload.parameters(), equalTo(Parameters.asParameters("abc")));
    }

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

    @Nested
    class staticBuilders {
        @Test
        void payloadWithNoOpParameter() {
            Payload payload = Payload.payload(Command.BTHandshake);

            assertThat(payload, equalTo(Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM)));
        }

        @Test
        void payloadWithParametersArray() {
            Payload payload = Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM);

            assertThat(payload, equalTo(Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM)));
        }

        @Test
        void payloadWithParameters() {
            Payload payload = Payload.payload(Command.BTHandshake, Parameters.parameters(Parameter.of(0)));

            assertThat(payload, equalTo(Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM)));
        }

        @Test
        void payloadWithParameterable() {
            Payload payload = Payload.payload(Command.BTHandshake, new Angle(1));

            assertThat(payload, equalTo(Payload.payload(Command.BTHandshake, Parameter.of(1))));
        }
    }

    @Test
    void testToString() {
        Payload payload = Payload.payload(Command.BTHandshake, Parameter.of(1));

        assertThat(payload.toString(), allOf(
            containsString("\"command\" : \"BTHandshake\""),
            containsString("\"parameters\" : \"[ \\\"0x1, (int)1\\\" ]\"")));
    }
}
