package com.alex.robi;

import java.text.MessageFormat;
import java.util.List;

public enum TfCardInsertion {

    Insertet(Parameter.of(0x01)), Removed(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x04);
    private Parameter parameter;

    private TfCardInsertion(Parameter value) {
        this.parameter = value;
    }

    public static TfCardInsertion fromRobotState(List<Message> message) {
        Parameter[] parameters = message.get(4).parameters();
        if (!parameters[0].equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (TfCardInsertion tfCardInsertion : TfCardInsertion.values()) {
            if (tfCardInsertion.parameter.equals(parameters[1])) {
                return tfCardInsertion;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", parameters[1]));
    }
}
