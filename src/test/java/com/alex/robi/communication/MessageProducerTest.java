package com.alex.robi.communication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.alex.robi.communication.MessageProducer.MessageConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageProducerTest {

    @Test
    void expectedHeader1NotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer).received(0);

        verifyNoInteractions(consumer);
    }

    @Test
    void expectedHeader2NotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer).received(Message.COMMAND_HEADER_1).received(3);

        verifyNoInteractions(consumer);
    }

    @Test
    void expectedEndCharNotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer)
            .received(Message.COMMAND_HEADER_1)
            .received(Message.COMMAND_HEADER_2)
            .received(6)
            .received(Command.BTHandshake.value())
            .received(Parameter.NOP_PARAM.value())
            .received(7)
            .received(666);

        verifyNoInteractions(consumer);
    }

    @Test
    void corretMessage() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer)
            .received(Message.COMMAND_HEADER_1)
            .received(Message.COMMAND_HEADER_2)
            .received(7)
            .received(Command.BTHandshake.value())
            .received(Parameter.NOP_PARAM.value())
            .received(Parameter.NOP_PARAM.value())
            .received(8)
            .received(Message.END_CHARACTER);

        Message message2 = Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM).toMessage();
        verify(consumer).accept(Mockito.eq(message2));
    }
}
