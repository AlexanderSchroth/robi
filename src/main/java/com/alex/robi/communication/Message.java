package com.alex.robi.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    public static final Parameter PARAMETER_COMMAND_HEADER_1 = Parameter.of(COMMAND_HEADER_1);

    public static final int COMMAND_HEADER_2 = 0xBF;
    public static final Parameter PARAMETER_COMMAND_HEADER_2 = Parameter.of(COMMAND_HEADER_2);

    public static final int END_CHARACTER = 0xED;
    public static final Parameter PARAMETER_END_CHARACTER = Parameter.of(END_CHARACTER);

    // COMMAND_HEADER_1, COMMAND_HEADER2, LENGHT, COMMAND, CHECK, END_CHARACTER,
    static final int FIXED_PARTS = 6;
    static final int FIXED_PARTS_ZERO_BASED = FIXED_PARTS - 1;

    private Message() {
    }

    public void send(Sending connection) throws IOException {
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

    public Payload payload() {
        return new Payload.Builder()
            .withCommand(command())
            .withParameters(Parameters.parameters(this.parameters()))
            .build();
    }

    public Command command() {
        return Command.findByValue(command);
    }

    private Parameter[] parameters() {
        return IntStream.of(parameters)
            .mapToObj(i -> Parameter.of(i))
            .collect(Collectors.toList())
            .toArray(new Parameter[0]);
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
        message.put("check", dump(check));
        message.put("endCharacter", dump(endCharacter));
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String parametersAsString(int[] parameters) {
        StringBuffer sb = new StringBuffer();
        for (int intChar : parameters) {
            sb.append(Character.toChars(intChar));
        }
        return sb.toString();
    }

    public static String dump(int i) {
        return String.format("0x%1X", i) + ", (int)" + i;
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
}
