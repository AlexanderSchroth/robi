package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Payload;
import java.text.MessageFormat;

public enum SoundState {

    Mute(Parameter.of(0x01)), NoneMute(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x00);
    private Parameter parameter;

    private SoundState(Parameter value) {
        this.parameter = value;
    }

    public static SoundState fromRobotStateResponse(Payload payload) {
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (SoundState soundState : SoundState.values()) {
            if (soundState.parameter.equals(payload.parameters().second())) {
                return soundState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", payload.parameters().second()));
    }
}
