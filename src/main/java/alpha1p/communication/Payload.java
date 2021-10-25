package alpha1p.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Payload {

    private Command command;
    private Parameters parameters;

    private Payload() {

    }

    public Command command() {
        return command;
    }

    public Parameters parameters() {
        return parameters;
    }

    public static Payload payload(Command command) {
        return new Builder()
            .withCommand(command)
            .withParameters(Parameters.parameters(Parameter.NOP_PARAM))
            .build();
    }

    public static Payload payload(Command command, Parameter... parameters) {
        return new Builder()
            .withCommand(command)
            .withParameters(Parameters.parameters(parameters))
            .build();
    }

    public static Payload payload(Command command, Parameters parameters) {
        return new Builder()
            .withCommand(command)
            .withParameters(parameters)
            .build();
    }

    public static Payload payload(Command command, Parameterable... parameters) {
        return new Builder()
            .withCommand(command)
            .withParameters(Parameters.parameters(parameters))
            .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Payload that = (Payload) obj;
        return new EqualsBuilder()
            .append(this.command, that.command)
            .append(this.parameters, that.parameters)
            .build();
    }

    public Message toMessage() {
        int length = Message.FIXED_PARTS_ZERO_BASED + parameters.lenght();
        Parameter checksum = Parameters.parameters(Parameter.of(length), command().asParameter(), parameters).checksum();

        return new Message.Builder()
            .withCommandHeader1(Message.PARAMETER_COMMAND_HEADER_1)
            .withCommandHeader2(Message.PARAMETER_COMMAND_HEADER_2)
            .withLength(Parameter.of(length))
            .withCommand(command.asParameter())
            .withCheck(checksum)
            .withParameters(parameters)
            .withEndCharacter(Message.PARAMETER_END_CHARACTER)
            .build();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("command", command.name());
        message.put("parameters", parameters.toString());

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class Builder {

        private Command command;
        private Parameters parameters;

        public Builder withCommand(Command command) {
            this.command = command;
            return this;
        }

        public Builder withParameters(Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Payload build() {
            Payload payload = new Payload();
            payload.command = command;
            payload.parameters = parameters;
            return payload;
        }
    }
}
