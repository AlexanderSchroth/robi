package alpha1p.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Message {

    private Parameter commandHeader1;
    private Parameter commandHeader2;
    private Parameter length;
    private Parameter command;
    private Parameters parameters;
    private Parameter check;
    private Parameter endCharacter;

    private static final int COMMAND_HEADER_1 = 0xFB;
    public static final Parameter PARAMETER_COMMAND_HEADER_1 = Parameter.of(COMMAND_HEADER_1);

    private static final int COMMAND_HEADER_2 = 0xBF;
    public static final Parameter PARAMETER_COMMAND_HEADER_2 = Parameter.of(COMMAND_HEADER_2);

    private static final int END_CHARACTER = 0xED;
    public static final Parameter PARAMETER_END_CHARACTER = Parameter.of(END_CHARACTER);

    // COMMAND_HEADER_1, COMMAND_HEADER2, LENGHT, COMMAND, CHECK, END_CHARACTER,
    static final int FIXED_PARTS = 6;
    static final int FIXED_PARTS_ZERO_BASED = FIXED_PARTS - 1;

    private Message() {
    }

    public void send(Sending connection) throws IOException {
        int[] message = new int[FIXED_PARTS + parameters.size()];
        int part = 0;
        message[part++] = commandHeader1.value();
        message[part++] = commandHeader2.value();
        message[part++] = length.value();
        message[part++] = command.value();

        int[] parameterAsIntArray = parameters.asArray();
        for (int i = 0; i < parameterAsIntArray.length; i++) {
            message[part++] = parameterAsIntArray[i];
        }
        message[part++] = check.value();
        message[part++] = endCharacter.value();

        connection.send(message);
    }

    public Payload payload() {
        return new Payload.Builder()
            .withCommand(command())
            .withParameters(parameters)
            .build();
    }

    public Command command() {
        return Command.findByValue(command.value());
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
        message.put("commandHeader1", saveToString(commandHeader1));
        message.put("commandHeader2", saveToString(commandHeader2));
        message.put("length", saveToString(length));
        message.put("command", saveToString(command));
        message.put("parameters", parameters.toString());
        message.put("check", saveToString(check));
        message.put("endCharacter", saveToString(endCharacter));
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String saveToString(Parameter p) {
        if (p == null) {
            return "<null>";
        } else {
            return p.toString();
        }
    }

    public static class Builder {
        private Parameter commandHeader1;
        private Parameter commandHeader2;
        private Parameter length;
        private Parameter command;
        private Parameters parameters;
        private Parameter check;
        private Parameter endCharacter;

        public Builder() {
            this.parameters = Parameters.empty();
        }

        public Builder withEndCharacter(Parameter endCharacter) {
            this.endCharacter = endCharacter;
            return this;
        }

        public Builder withCheck(Parameter check) {
            this.check = check;
            return this;
        }

        public Builder withParameters(Parameter... parameters) {
            this.parameters = Parameters.parameters(parameters);
            return this;
        }

        public Builder withParameters(Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public int addParameter(Parameter parameter) {
            this.parameters.add(parameter);
            return this.length.value() - this.parameters.size() - FIXED_PARTS_ZERO_BASED;
        }

        public Builder withCommandHeader1(Parameter commandHeader1) {
            this.commandHeader1 = commandHeader1;
            return this;
        }

        public Builder withCommandHeader2(Parameter commandHeader2) {
            this.commandHeader2 = commandHeader2;
            return this;
        }

        public Builder withCommand(Parameter command) {
            this.command = command;
            return this;
        }

        public Builder withLength(Parameter length) {
            this.length = length;
            return this;
        }

        public Message build() {
            Message m = new Message();
            m.commandHeader1 = commandHeader1;
            m.commandHeader2 = commandHeader2;
            m.length = length;
            m.command = command;
            m.parameters = parameters;
            m.check = check;
            m.endCharacter = endCharacter;
            return m;
        }

        public void skip() {
            this.commandHeader1 = null;
            this.commandHeader2 = null;
            this.length = null;
            this.command = null;
            this.parameters = null;
            this.check = null;
            this.endCharacter = null;
        }
    }
}
