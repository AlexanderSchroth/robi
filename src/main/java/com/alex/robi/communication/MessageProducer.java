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

    private Map<Read, IntReceiver> receivers;

    public MessageProducer(MessageConsumer partialMessageConsumer) {
        this.partialMessageConsumer = partialMessageConsumer;

        receivers = new HashMap<>();
        receivers.put(Read.ExpectHeader1, new Header1Receiver());
        receivers.put(Read.ExpectHeader2, new Header2Receiver());
        receivers.put(Read.ExpectLength, new LenghReceiver());
        receivers.put(Read.ExpectCommand, new CommandReceiver());
        receivers.put(Read.ExpectParameters, new ParameterReceiver());
        receivers.put(Read.ExpectedCheck, new CheckReceiver());
        receivers.put(Read.ExpectEndCharater, new EndCharacterReceiver());

        this.messageBuilder = new Message.Builder();
        this.current = receivers.get(Read.ExpectHeader1);
    }

    private enum Read {
        ExpectHeader1, ExpectHeader2, ExpectLength, ExpectCommand, ExpectParameters, ExpectedCheck, ExpectEndCharater
    }

    public interface MessageConsumer extends Consumer<Message> {
    }

    private interface IntReceiver {
        Read read(Message.Builder message, int value);
    }

    private class Header1Receiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            if (value == Message.COMMAND_HEADER_1) {
                message.withCommandHeader1(value);
                return Read.ExpectHeader2;
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", Read.ExpectHeader1, value));
                message.skip();
                return Read.ExpectHeader1;
            }
        }
    }

    private class Header2Receiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            if (value == Message.COMMAND_HEADER_2) {
                message.withCommandHeader2(value);
                return Read.ExpectLength;
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", Read.ExpectHeader1, value));
                return Read.ExpectHeader1;
            }
        }
    }

    private class LenghReceiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            message.withLength(value);
            return Read.ExpectCommand;
        }
    }

    private class CommandReceiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            message.withCommand(value);
            return Read.ExpectParameters;
        }
    }

    private class ParameterReceiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            int remaining = message.addParameter(value);
            if (remaining == 0) {
                return Read.ExpectedCheck;
            } else {
                return Read.ExpectParameters;
            }
        }
    }

    private class CheckReceiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            message.withCheck(value);
            return Read.ExpectEndCharater;
        }
    }

    private class EndCharacterReceiver implements IntReceiver {
        @Override
        public Read read(Builder message, int value) {
            if (value == Message.END_CHARACTER) {
                message.withEndCharacter(value);
                partialMessageConsumer.accept(message.build());
            } else {
                LOG.warn(MessageFormat.format("{0} but was {1}", Read.ExpectedCheck, value));
            }
            return Read.ExpectHeader1;
        }
    }

    MessageProducer received(int value) {
        Read nextResponsibleReceiver = current.read(messageBuilder, value);
        this.current = receivers.get(nextResponsibleReceiver);
        return this;
    }
}
