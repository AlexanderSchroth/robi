package com.alex.robi.function;

import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Parameters;
import com.alex.robi.communication.Payload;
import java.text.MessageFormat;
import java.util.List;

public enum ServoIndicateState {

    On(Parameter.of(0x01)), Off(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x03);
    private Parameter parameter;

    private ServoIndicateState(Parameter value) {
        this.parameter = value;
    }

    public static ServoIndicateState fromRobotState(List<Payload> payload) {
        Parameters parameters = payload.get(3).parameters();
        if (!parameters.first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (ServoIndicateState servoIndicateState : ServoIndicateState.values()) {
            if (servoIndicateState.parameter.equals(parameters.second())) {
                return servoIndicateState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", parameters.second()));
    }

}
