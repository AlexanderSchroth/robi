package com.alex.robi;

import java.text.MessageFormat;
import java.util.List;

public enum PlayState {

    NonePause(Parameter.of(0x01)), Pause(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x01);
    private Parameter paramter;

    private PlayState(Parameter parameter) {
        this.paramter = parameter;
    }

    public static PlayState fromRobotStateResponse(List<Message> messages) {
        Parameter[] parameters = messages.get(1).parameters();
        if (!parameters[0].equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (PlayState playState : PlayState.values()) {
            if (playState.paramter.equals(parameters[1])) {
                return playState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", parameters[1]));
    }

}
