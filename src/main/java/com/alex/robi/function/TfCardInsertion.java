package com.alex.robi.function;

import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Payload;
import java.text.MessageFormat;
import java.util.List;

public enum TfCardInsertion {

    Inserted(Parameter.of(0x01)), Removed(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x04);
    private Parameter parameter;

    private TfCardInsertion(Parameter value) {
        this.parameter = value;
    }

    public static TfCardInsertion fromRobotState(List<Payload> response) {
        Payload payload = response.get(4);
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
