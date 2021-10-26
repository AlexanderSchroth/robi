package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameterable;
import java.text.MessageFormat;

public enum OnOff implements Parameterable {

    On(0x01), Off(0x00);

    private int value;

    private OnOff(int value) {
        this.value = value;
    }

    @Override
    public Parameter asParameter() {
        return Parameter.of(value);
    }

    public static OnOff from(boolean b) {
        if (b) {
            return On;
        } else {
            return Off;
        }
    }

    public static OnOff fromRobotState(Parameter p) {
        for (OnOff yesNo : OnOff.values()) {
            if (yesNo.value == p.value()) {
                return yesNo;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", p));
    }
}
