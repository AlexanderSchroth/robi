package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.robi.alpha1p.api.communication.MessageProducer.MessageConsumer;
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
    void correctMessage() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        MessageProducer messageProducer = new MessageProducer(consumer);
        messageProducer
            .received(Message.PARAMETER_COMMAND_HEADER_1)
            .received(Message.PARAMETER_COMMAND_HEADER_2)
            .received(of(7))
            .received(Command.BTHandshake.asParameter())
            .received(Parameter.NOP_PARAM)
            .received(Parameter.NOP_PARAM)
            .received(of(8))
            .received(Message.PARAMETER_END_CHARACTER);

        Message message2 = Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM).toMessage();
        verify(consumer).received(Mockito.eq(message2));
    }

    @Test
    void producesTwoMessages() {
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);

        MessageProducer messageProducer = new MessageProducer(consumer);
        messageProducer
            .received(Message.PARAMETER_COMMAND_HEADER_1)
            .received(Message.PARAMETER_COMMAND_HEADER_2)
            .received(of(7))
            .received(Command.BTHandshake.asParameter())
            .received(Parameter.NOP_PARAM)
            .received(Parameter.NOP_PARAM)
            .received(of(8))
            .received(Message.PARAMETER_END_CHARACTER);

        messageProducer
            .received(Message.PARAMETER_COMMAND_HEADER_1)
            .received(Message.PARAMETER_COMMAND_HEADER_2)
            .received(of(6))
            .received(Command.CompleteActionListSending.asParameter())
            .received(Parameter.NOP_PARAM)
            .received(of(135))
            .received(Message.PARAMETER_END_CHARACTER);

        Message message1 = Payload.payload(Command.BTHandshake, Parameter.NOP_PARAM, Parameter.NOP_PARAM).toMessage();
        verify(consumer).received(Mockito.eq(message1));

        Message message2 = Payload.payload(Command.CompleteActionListSending, Parameter.NOP_PARAM).toMessage();
        verify(consumer).received(Mockito.eq(message2));
    }
}
