package com.alex.robi;

import java.util.Arrays;

public class ClockParameter {

    private YesNo clockSwitch;
    private YesNo daily;
    private int hour;
    private int minute;
    private int second;
    private String actionList;

    public static ClockParameter fromMessage(Message m) {
        ClockParameter clock = new ClockParameter();

        Parameter[] parameters = m.parameters();
        clock.clockSwitch = YesNo.fromRobotState(parameters[0]);
        clock.daily = YesNo.fromRobotState(parameters[1]);
        clock.hour = parameters[2].value();
        clock.minute = parameters[3].value();
        clock.second = parameters[4].value();

        Parameter[] copyOfRange = Arrays.copyOfRange(parameters, 6, parameters.length);
        clock.actionList = asString(copyOfRange);
        return clock;
    }

    public static String asString(Parameter[] parameters) {
        StringBuffer sb = new StringBuffer();
        for (Parameter p : parameters) {
            sb.append(p.asString());
        }
        return sb.toString();
    }
}
