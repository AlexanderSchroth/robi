package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameters;
import com.robi.alpha1p.api.communication.Payload;

public class ClockParameter {

    private YesNo clockSwitch;
    private YesNo daily;
    private int hour;
    private int minute;
    private int second;
    private String actionList;

    public static ClockParameter fromMessage(Payload m) {
        ClockParameter clock = new ClockParameter();
        Parameters parameters = m.parameters();
        clock.clockSwitch = YesNo.fromRobotState(parameters.first());
        clock.daily = YesNo.fromRobotState(parameters.second());
        clock.hour = parameters.third().value();
        clock.minute = parameters.fourth().value();
        clock.second = parameters.fifth().value();
        clock.actionList = parameters.subset(6, parameters.lenght()).asString();
        return clock;
    }
}
