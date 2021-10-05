package com.alex.robi.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Message {

    private Parameter commandHeader1;
    private Parameter commandHeader2;
    private Parameter length;
    private Parameter command;
    private Parameter[] parameters;
    private Parameter check;
    private Parameter endCharacter;

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
        message[part++] = commandHeader1.value();
        message[part++] = commandHeader2.value();
        message[part++] = length.value();
        message[part++] = command.value();

        for (int i = 0; i < parameters.length; i++) {
            message[part++] = parameters[i].value();
        }
        message[part++] = check.value();
        message[part++] = endCharacter.value();

        connection.send(message);
    }

    public Payload payload() {
        return new Payload.Builder()
            .withCommand(command())
            .withParameters(Parameters.parameters(this.parameters()))
            .build();
    }

    public Command command() {
        return Command.findByValue(command.value());
    }

    private Parameter[] parameters() {
        return parameters;
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
        message.put("commandHeader1", commandHeader1.toString());
        message.put("commandHeader2", commandHeader2.toString());
        message.put("length", length.toString());
        message.put("command", command.toString());
        for (int i = 0; i < parameters.length; i++) {
            message.put("parameter" + i, parameters[i].toString());
        }
        message.put("check", check.toString());
        message.put("endCharacter", endCharacter.toString());
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class Builder {
        private Parameter commandHeader1;
        private Parameter commandHeader2;
        private Parameter length;
        private Parameter command;
        private List<Parameter> parameters;
        private Parameter check;
        private Parameter endCharacter;

        public Builder() {
            parameters = new ArrayList<>();
        }

        public Builder withEndCharacter(int endCharacter) {
            this.endCharacter = Parameter.of(endCharacter);
            return this;
        }

        public Builder withCheck(int check) {
            this.check = Parameter.of(check);
            return this;
        }

        public Builder withParameters(int[] parameters) {
            this.parameters = IntStream.of(parameters).mapToObj(Parameter::of).collect(Collectors.toList());
            return this;
        }

        public int addParameter(int parameter) {
            this.parameters.add(Parameter.of(parameter));
            return this.length.value() - this.parameters.size() - FIXED_PARTS_ZERO_BASED;
        }

        public Builder withCommandHeader1(int commandHeader1) {
            this.commandHeader1 = Parameter.of(commandHeader1);
            return this;
        }

        public Builder withCommandHeader2(int commandHeader2) {
            this.commandHeader2 = Parameter.of(commandHeader2);
            return this;
        }

        public Builder withCommand(int command) {
            this.command = Parameter.of(command);
            return this;
        }

        public Builder withLength(int length) {
            this.length = Parameter.of(length);
            return this;
        }

        public Message build() {
            Message m = new Message();
            m.commandHeader1 = commandHeader1;
            m.commandHeader2 = commandHeader2;
            m.length = length;
            m.command = command;
            m.parameters = parameters.toArray(new Parameter[0]);
            m.check = check;
            m.endCharacter = endCharacter;
            return m;
        }

        public void skip() {
            this.commandHeader1 = null;
            this.commandHeader2 = null;
            this.length = null;
            this.command = null;
            this.parameters = new ArrayList<>();
            this.check = null;
            this.endCharacter = null;
        }
    }
}
