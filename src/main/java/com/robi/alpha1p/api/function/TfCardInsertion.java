package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Payload;
import java.text.MessageFormat;

public enum TfCardInsertion {

    Inserted(Parameter.of(0x01)), Removed(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x04);
    private Parameter parameter;

    private TfCardInsertion(Parameter value) {
        this.parameter = value;
    }

    public static TfCardInsertion fromRobotState(Payload payload) {
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (TfCardInsertion tfCardInsertion : TfCardInsertion.values()) {
            if (tfCardInsertion.parameter.equals(payload.parameters().second())) {
                return tfCardInsertion;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", payload.parameters().second()));
    }
}
