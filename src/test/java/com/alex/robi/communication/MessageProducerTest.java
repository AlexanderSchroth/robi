package com.alex.robi.communication;

import static com.alex.robi.communication.Parameter.of;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.alex.robi.communication.MessageProducer.MessageConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageProducerTest {

    @Test
    void expectedHeader1NotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer).received(of(0));

        verifyNoInteractions(consumer);
    }

    @Test
    void expectedHeader2NotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer).received(Message.PARAMETER_COMMAND_HEADER_1).received(of(3));

        verifyNoInteractions(consumer);
    }

    @Test
    void expectedEndCharNotCorrect() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer)
            .received(Message.PARAMETER_COMMAND_HEADER_1)
            .received(Message.PARAMETER_COMMAND_HEADER_2)
            .received(of(6))
            .received(Command.BTHandshake.asParameter())
            .received(Parameter.NOP_PARAM)
            .received(of(7))
            .received(of(111));

        verifyNoInteractions(consumer);
    }

    @Test
    void corretMessage() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        new MessageProducer(consumer)
            .received(Message.PARAMETER_COMMAND_HEADER_1)
            .received(Message.PARAMETER_COMMAND_HEADER_2)
            .received(of(7))
            .received(Command.BTHandshake.asParameter())
            .received(Parameter.NOP_PARAM)
            .received(Parameter.NOP_PARAM)
            .received(of(8))
            .received(Message.PARAMETER_END_CHARACTER);

        Message message2 = Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM).toMessage();
        verify(consumer).finished(Mockito.eq(message2));
    }
}
