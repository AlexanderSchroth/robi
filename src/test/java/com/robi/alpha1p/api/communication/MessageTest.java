package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.robi.alpha1p.api.communication.Message.Builder;
import java.io.IOException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void payload() {
        assertThat(new Message.Builder()
            .withCommand(Command.BTHandshake.asParameter())
            .withParameters(Parameter.NOP_PARAM)
            .build()
            .payload(),
            equalTo(Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM)));
    }

    @Test
    void command() {
        assertThat(new Message.Builder()
            .withCommand(Command.BTHandshake.asParameter())
            .withParameters(Parameter.NOP_PARAM)
            .build()
            .command(),
            equalTo(Command.BTHandshake));
    }

    @Nested
    class equals {
        @Test
        void wrongType() {
            assertThat(new Message.Builder().build().equals(""), is(false));
        }

        @Test
        void nullValue() {
            assertThat(new Message.Builder().build().equals(null), is(false));
        }

        @Test
        void sameInstance() {
            Message message = new Message.Builder().build();
            assertThat(message.equals(message), is(true));
        }

        @Test
        void equalsT() {
            Message left = new Message.Builder().withCheck(of(1)).build();
            Message right = new Message.Builder().withCheck(of(1)).build();
            assertThat(left.equals(right), is(true));
        }

        @Test
        void notEqualsT() {
            Message left = new Message.Builder().withCheck(of(1)).build();
            Message right = new Message.Builder().withCheck(of(2)).build();
            assertThat(left.equals(right), is(false));
        }
    }

    @Test
    void testHashCode() {
        assertThat(new Message.Builder().withCheck(of(1)).build().hashCode(), is(-1064906255));
    }

    @Test
    void testToString() {
        assertThat(new Message.Builder()
            .withCommandHeader1(of(1))
            .withCommandHeader2(of(2))
            .withLength(of(3))
            .withCommand(of(4))
            .withParameters(of(5))
            .withCheck(of(6))
            .withEndCharacter(of(7))
            .build().toString(),
            allOf(
                containsString("\"commandHeader1\" : \"0x1, (int)1\""),
                containsString("\"commandHeader2\" : \"0x2, (int)2\""),
                containsString("\"length\" : \"0x3, (int)3\""),
                containsString("\"command\" : \"0x4, (int)4\""),
                containsString("\"parameters\" : \"[ \\\"0x5, (int)5\\\" ]\""),
                containsString("\"parametersAsStr\" : \"\\u0005\""),
                containsString("\"check\" : \"0x6, (int)6\""),
                containsString("\"endCharacter\" : \"0x7, (int)7")));
    }

    @Nested
    class builder {
        @Test
        void addParameterReturnsOneMissingParameters() {
            assertThat(new Message.Builder()
                .withLength(Parameter.of(7))
                .addParameter(Parameter.NOP_PARAM), is(1));
        }

        @Test
        void addParameterReturnsZeroMissingParameters() {
            Builder builder = new Message.Builder()
                .withLength(Parameter.of(7));
            builder.addParameter(Parameter.NOP_PARAM);

            assertThat(
                builder.addParameter(Parameter.NOP_PARAM), is(0));
        }
    }

    @Test
    void sending() throws IOException {
        Message message = Payload.payload(Command.BTHandshake).toMessage();
        ByteSender byteSender = new ByteSender();

        message.send(byteSender);

        assertArrayEquals(byteSender.message, new int[] { 251, 191, 6, 1, 0, 7, 237 });
    }

    private static class ByteSender implements Sending {

        private int[] message;

        @Override
        public void close() throws IOException {

        }

        @Override
        public void send(int[] message) throws IOException {
            this.message = message;
        }
    }
}
