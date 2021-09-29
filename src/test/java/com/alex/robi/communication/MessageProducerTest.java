package com.alex.robi.communication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

class MessageProducerTest {

    @Test
    void expectedHeader1NotCorrect() {
        assertThrows(IllegalStateException.class, () -> new MessageProducer(1, 2, 3, null).received(0));
    }

    @Test
    void expectedHeader2NotCorrect() {
        assertThrows(IllegalStateException.class, () -> new MessageProducer(1, 2, 3, null).received(1).received(3));
    }

    @Test
    void correct() {
        new MessageProducer(Message.COMMAND_HEADER_1, Message.COMMAND_HEADER_2, Message.END_CHARACTER, message -> {
            assertThat(message, CoreMatchers.equalTo(Payload.payload(Command.BTHandshake).toMessage()));
        })
            .received(Message.COMMAND_HEADER_1)
            .received(Message.COMMAND_HEADER_2)
            .received(6)
            .received(Command.BTHandshake.value())
            .received(Parameter.NOP_PARAM.value())
            .received(7)
            .received(Message.END_CHARACTER);
    }
}
