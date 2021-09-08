package com.alex.robi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Arrays;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Message {

    private int commandHeader1;
    private int commandHeader2;
    private int length;
    private int command;
    private int[] parameters;
    private int check;
    private int endCharacter;

    public static final int COMMAND_HEADER_1 = 0xFB;
    public static final int COMMAND_HEADER_2 = 0xBF;
    public static final int END_CHARACTER = 0xED;

    // COMMAND_HEADER_1, COMMAND_HEADER2, LENGHT, COMMAND, CHECK, END_CHARACTER,
    private static final int FIXED_PARTS = 6;
    private static final int FIXED_PARTS_ZERO_BASED = FIXED_PARTS - 1;

    private Message() {
    }

    public void send(Connection connection) throws IOException {
        int[] message = new int[FIXED_PARTS + parameters.length];
        int part = 0;
        message[part++] = commandHeader1;
        message[part++] = commandHeader2;
        message[part++] = length;
        message[part++] = command;

        for (int i = 0; i < parameters.length; i++) {
            message[part++] = parameters[i];
        }
        message[part++] = check;
        message[part++] = endCharacter;

        connection.send(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Message that = (Message) obj;
        return new EqualsBuilder()
            .append(this.commandHeader1, that.commandHeader1)
            .append(this.commandHeader2, that.commandHeader2)
            .append(this.length, that.length)
            .append(this.command, that.command)
            .append(this.parameters, that.parameters)
            .append(this.check, that.check)
            .append(this.endCharacter, that.endCharacter)
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(commandHeader1)
            .append(commandHeader2)
            .append(length)
            .append(command)
            .append(parameters)
            .append(check)
            .append(endCharacter)
            .toHashCode();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("commandHeader1", dump(commandHeader1));
        message.put("commandHeader2", dump(commandHeader2));
        message.put("length", dump(length));
        message.put("command", dump(command));
        for (int i = 0; i < parameters.length; i++) {
            message.put("parameter" + i, dump(parameters[i]));
        }
        message.put("parametersAsString", parametersAsString());
        message.put("check", dump(check));
        message.put("endCharacter", dump(endCharacter));
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String parametersAsString() {
        StringBuffer sb = new StringBuffer();
        for (int intChar : parameters) {
            sb.append(Character.toChars(intChar));
        }
        return sb.toString();
    }

    private static String dump(int i) {
        return String.format("0x%1X", i) + ", (int)" + i;
    }

    public static Message messageWithPayload(Payload payload) {
        return new Message.FromPayloadBuilder().withPayload(payload).build();
    }

    public static Message messageFromBytes(int[] bytes) {
        return new Message.FromBytesBuilder().withBytes(bytes).build();
    }

    public static class FromBytesBuilder {
        private Message toBuild;

        public FromBytesBuilder() {
            toBuild = new Message();
        }

        private void checkConsistence(int[] b) {
            expectedHeader(b);
            expectedEnd(b);
            expectedLength(b);
            validateCheckByte(b);
            expectedEnd(b);
        }

        private void validateCheckByte(int[] b) {
            int calculatedCheck = calculateCheck(b[2], b[3], Arrays.copyOfRange(b, 4, b.length - 2));
            int givenCheck = b[b.length - 2];
            if (calculatedCheck != givenCheck) {
                throw new IllegalArgumentException(MessageFormat.format("Check {0} value not as expected {1}", dump(givenCheck), dump(calculatedCheck)));
            }
        }

        private void expectedLength(int[] b) {
            int expectedLength = b.length - FIXED_PARTS;
            if (b[3] == expectedLength) {
                throw new IllegalArgumentException(MessageFormat.format("Length {0} value not as expected {1}", dump(b[3]), dump(expectedLength)));
            }
        }

        private void expectedEnd(int[] b) {
            int expectedEndCharacterPosition = b.length - 1;
            if (b[expectedEndCharacterPosition] != END_CHARACTER) {
                throw new IllegalArgumentException(
                    MessageFormat.format("End character {0} value not as expected {1}", dump(b[expectedEndCharacterPosition]), dump(END_CHARACTER)));
            }
        }

        private void expectedHeader(int[] b) {
            if (b[0] != COMMAND_HEADER_1) {
                throw new IllegalArgumentException(MessageFormat.format("Header1 {0} value not as expected {1}", dump(b[0]), dump(COMMAND_HEADER_1)));
            }

            if (b[1] != COMMAND_HEADER_2) {
                throw new IllegalArgumentException(MessageFormat.format("Header2 {0} value not as expected {1}", dump(b[1]), dump(COMMAND_HEADER_2)));
            }
        }

        public FromBytesBuilder withBytes(int[] b) {
            checkConsistence(b);
            toBuild.commandHeader1 = b[0];
            toBuild.commandHeader2 = b[1];
            toBuild.length = b[2];
            toBuild.command = b[3];
            toBuild.parameters = Arrays.copyOfRange(b, 4, b.length - 2);
            toBuild.check = b[b.length - 2];
            toBuild.endCharacter = b[b.length - 1];
            return this;
        }

        public Message build() {
            return toBuild;
        }
    }

    public static class Builder {
        private Message toBuild;

        public Builder() {
            toBuild = new Message();
        }

        public Builder withEndCharacter(int endCharacter) {
            toBuild.endCharacter = endCharacter;
            return this;
        }

        public Builder withCheck(int check) {
            toBuild.check = check;
            return this;
        }

        public Builder withParameters(int[] parameters) {
            toBuild.parameters = parameters;
            return this;
        }

        public Builder withCommandHeader1(int commandHeader1) {
            toBuild.commandHeader1 = commandHeader1;
            return this;
        }

        public Builder withCommandHeader2(int commandHeader2) {
            toBuild.commandHeader2 = commandHeader2;
            return this;
        }

        public Builder withCommand(int command) {
            toBuild.command = command;
            return this;
        }

        public Builder withLength(int length) {
            toBuild.length = length;
            return this;
        }

        public Message builder() {
            return toBuild;
        }
    }

    public static class FromPayloadBuilder {

        private Message toBuild;

        public FromPayloadBuilder() {
            toBuild = new Message();
            toBuild.commandHeader1 = COMMAND_HEADER_1;
            toBuild.commandHeader2 = COMMAND_HEADER_2;
            toBuild.endCharacter = END_CHARACTER;
        }

        public FromPayloadBuilder withPayload(Payload payload) {
            toBuild.command = payload.command().value();
            toBuild.parameters = payload.parameters().asArray();
            return this;
        }

        public Message build() {
            toBuild.length = FIXED_PARTS_ZERO_BASED + toBuild.parameters.length;
            toBuild.check = calculateCheck(toBuild.length, toBuild.command, toBuild.parameters);
            return toBuild;
        }
    }

    private static int calculateCheck(int length, int command, int[] parameters) {
        int checkSum = length + command + sumParameters(parameters);
        byte[] bytes = ByteBuffer.allocate(3 + parameters.length).putInt(checkSum).array();
        return Byte.toUnsignedInt(bytes[3]);
    }

    private static int sumParameters(int[] parameters) {
        int sum = 0;
        for (int i = 0; i < parameters.length; i++) {
            sum += parameters[i];
        }
        return sum;
    }
}
