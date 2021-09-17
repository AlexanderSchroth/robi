package com.alex.robi.function;

import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Payload;
import java.util.List;

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

    public static Volume fromRobotState(List<Payload> response) {
        Payload payload = response.get(2);
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }
        return new Volume(payload.parameters().second().value());
    }
}
