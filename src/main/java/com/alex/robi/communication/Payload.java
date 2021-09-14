package com.alex.robi.communication;

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