package com.alex.robi;

import java.text.MessageFormat;
import java.util.List;

public enum SoundState {

    Mute(Parameter.of(0x01)), NoneMute(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x00);
    private Parameter parameter;

    private SoundState(Parameter value) {
        this.parameter = value;
    }

    public static SoundState fromRobotStateResponse(List<Message> m) {
        Parameter[] parameters = m.get(0).parameters();
        if (!parameters[0].equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (SoundState soundState : SoundState.values()) {
            if (soundState.parameter.equals(parameters[1])) {
                return soundState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", parameters[1]));
    }
}
