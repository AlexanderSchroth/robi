package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Payload;

public class Volume {

    private static final Parameter FLAG = Parameter.of(0x02);
    private int value;

    private Volume(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    public static Volume fromRobotState(Payload payload) {
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }
        return new Volume(payload.parameters().second().value());
    }
}
