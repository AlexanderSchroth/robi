package com.robi.alpha1p.api.communication;

import com.robi.alpha1p.api.communication.Message.Builder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProducer {

    private final static Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

    private Message.Builder messageBuilder;
    private IntReceiver current;

    private Map<ExpectInt, IntReceiver> receivers;

    public MessageProducer(MessageConsumer messageConsumer) {
        receivers = new HashMap<>();
        receivers.put(ExpectInt.Header1, new Header1Receiver());
        receivers.put(ExpectInt.Header2, new Header2Receiver());
        receivers.put(ExpectInt.Length, new LenghReceiver());
        receivers.put(ExpectInt.Command, new CommandReceiver());
        receivers.put(ExpectInt.Parameters, new ParameterReceiver());
        receivers.put(ExpectInt.Check, new CheckReceiver());
        receivers.put(ExpectInt.EndCharater, new EndCharacterReceiver(messageConsumer));

        this.messageBuilder = new Message.Builder();
        this.current = receivers.get(ExpectInt.Header1);
    }

    MessageProducer received(Parameter value) {
        ExpectInt nextResponsibleReceiver = current.read(messageBuilder, value);
        this.current = receivers.get(nextResponsibleReceiver);
        return this;
    }

    private enum ExpectInt {
        Header1, Header2, Length, Command, Parameters, Check, EndCharater
    }

    interface MessageConsumer {
        void received(Message message);
    }

    private interface IntReceiver {
        ExpectInt read(Message.Builder message, Parameter value);
    }

    private static class Header1Receiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            if (value.equals(Message.PARAMETER_COMMAND_HEADER_1)) {
                message.withCommandHeader1(value);
                return ExpectInt.Header2;
            } else {
                LOG.warn(MessageFormat.format("Expect {0} but was {1}", ExpectInt.Header1, value));
                message.skip();
                return ExpectInt.Header1;
            }
        }
    }

    private static class Header2Receiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            if (value.equals(Message.PARAMETER_COMMAND_HEADER_2)) {
                message.withCommandHeader2(value);
                return ExpectInt.Length;
            } else {
                LOG.warn(MessageFormat.format("Expect {0} but was {1}", ExpectInt.Header1, value));
                message.skip();
                return ExpectInt.Header1;
            }
        }
    }

    private static class LenghReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            message.withLength(value);
            return ExpectInt.Command;
        }
    }

    private static class CommandReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            message.withCommand(value);
            return ExpectInt.Parameters;
        }
    }

    private static class ParameterReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            int remaining = message.addParameter(value);
            if (remaining == 0) {
                return ExpectInt.Check;
            } else {
                return ExpectInt.Parameters;
            }
        }
    }

    private static class CheckReceiver implements IntReceiver {
        @Override
        public ExpectInt read(Builder message, Parameter value) {
            message.withCheck(value);
            return ExpectInt.EndCharater;
        }
    }

    private static class EndCharacterReceiver implements IntReceiver {

        private MessageConsumer messageConsumer;

        public EndCharacterReceiver(MessageConsumer messageConsumer) {
            this.messageConsumer = messageConsumer;
        }

        @Override
        public ExpectInt read(Builder message, Parameter value) {
            if (value.equals(Message.PARAMETER_END_CHARACTER)) {
                message.withEndCharacter(value);
                messageConsumer.received(message.build());
                message.reset();
            } else {
                LOG.warn(MessageFormat.format("Expect {0} but was {1}", ExpectInt.Check, value));
            }
            return ExpectInt.Header1;
        }
    }
}
