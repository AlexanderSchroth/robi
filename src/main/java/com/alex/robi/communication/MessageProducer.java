package com.alex.robi.communication;

import com.alex.robi.communication.Message.Builder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

    private Message.Builder messageBuilder;
    private Consumer<Message> partialMessageConsumer;
    private IntReceiver current;

    private Map<ExpectInt, IntReceiver> receivers;

    public MessageProducer(MessageConsumer partialMessageConsumer) {
        this.partialMessageConsumer = partialMessageConsumer;

        receivers = new HashMap<>();
        receivers.put(ExpectInt.Header1, new Header1Receiver());
        receivers.put(ExpectInt.Header2, new Header2Receiver());
        receivers.put(ExpectInt.Length, new LenghReceiver());
        receivers.put(ExpectInt.Command, new CommandReceiver());
        receivers.put(ExpectInt.Parameters, new ParameterReceiver());
        receivers.put(ExpectInt.Check, new CheckReceiver());
        receivers.put(ExpectInt.EndCharater, new EndCharacterReceiver());

        this.messageBuilder = new Message.Builder();
        this.current = receivers.get(ExpectInt.Header1);
    }

    private enum ExpectInt {
        Header1, Header2, Length, Command, Parameters, Check, EndCharater
    }

    public interface MessageConsumer extends Consumer<Message> {
    }

    private interface IntReceiver {
        ExpectInt read(Message.Builder message, int value);
    }

    private class Header1Receiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            if (value == Message.COMMAND_HEADER_1) {
                message.withCommandHeader1(value);
                return ExpectInt.Header2;
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", ExpectInt.Header1, value));
                message.skip();
                return ExpectInt.Header1;
            }
        }
    }

    private class Header2Receiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            if (value == Message.COMMAND_HEADER_2) {
                message.withCommandHeader2(value);
                return ExpectInt.Length;
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", ExpectInt.Header1, value));
                return ExpectInt.Header1;
            }
        }
    }

    private class LenghReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            message.withLength(value);
            return ExpectInt.Command;
        }
    }

    private class CommandReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            message.withCommand(value);
            return ExpectInt.Parameters;
        }
    }

    private class ParameterReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            int remaining = message.addParameter(value);
            if (remaining == 0) {
                return ExpectInt.Check;
            } else {
                return ExpectInt.Parameters;
            }
        }
    }

    private class CheckReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            message.withCheck(value);
            return ExpectInt.EndCharater;
        }
    }

    private class EndCharacterReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, int value) {
            if (value == Message.END_CHARACTER) {
                message.withEndCharacter(value);
                partialMessageConsumer.accept(message.build());
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", ExpectInt.Check, value));
            }
            return ExpectInt.Header1;
        }
    }

    MessageProducer received(int value) {
        ExpectInt nextResponsibleReceiver = current.read(messageBuilder, value);
        this.current = receivers.get(nextResponsibleReceiver);
        return this;
    }
}
