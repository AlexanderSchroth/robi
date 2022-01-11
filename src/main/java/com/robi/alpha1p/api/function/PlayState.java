package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Payload;
import java.text.MessageFormat;

public enum PlayState {

    NonePause(Parameter.of(0x01)), Pause(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x01);
    private Parameter paramter;

    private PlayState(Parameter parameter) {
        this.paramter = parameter;
    }

    public static PlayState fromRobotStateResponse(Payload payload) {
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (PlayState playState : PlayState.values()) {
            if (playState.paramter.equals(payload.parameters().second())) {
                return playState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", payload.parameters().second()));
    }

}
