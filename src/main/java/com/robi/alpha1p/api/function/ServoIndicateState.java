package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameters;
import com.robi.alpha1p.api.communication.Payload;
import java.text.MessageFormat;

public enum ServoIndicateState {

    On(Parameter.of(0x01)), Off(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x03);
    private Parameter parameter;

    private ServoIndicateState(Parameter value) {
        this.parameter = value;
    }

    public static ServoIndicateState fromRobotState(Payload payload) {
        Parameters parameters = payload.parameters();
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
