package com.alex.robi;

import java.util.List;

public class Volume {

    private static final Parameter FLAG = Parameter.of(0x02);
    private Parameter parameter;

    private Volume(Parameter value) {
        this.parameter = value;
    }

    @Override
    public String toString() {
        return parameter.toString();
    }

    public static Volume fromRobotState(List<Message> messages) {
        Parameter[] parameters = messages.get(2).parameters();
        if (!parameters[0].equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }
        return new Volume(parameters[1]);
    }
}
