package com.alex.robi;

import java.text.MessageFormat;
import java.util.List;

public enum ServoIndicateState {

    On(Parameter.of(0x01)), Off(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x03);
    private Parameter parameter;

    private ServoIndicateState(Parameter value) {
        this.parameter = value;
    }

    public static ServoIndicateState fromRobotState(List<Message> messages) {
        Parameter[] parameters = messages.get(3).parameters();
        if (!parameters[0].equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (ServoIndicateState servoIndicateState : ServoIndicateState.values()) {
            if (servoIndicateState.parameter.equals(parameters[1])) {
                return servoIndicateState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", parameters[1]));
    }

}
