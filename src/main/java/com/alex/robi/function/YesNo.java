package com.alex.robi.api;

import com.alex.robi.communication.Parameter;
import java.text.MessageFormat;

public enum YesNo {

    Yes(0x01), No(0x00);

    private int value;

    private YesNo(int value) {
        this.value = value;
    }

    public static YesNo fromRobotState(Parameter p) {
        for (YesNo yesNo : YesNo.values()) {
            if (yesNo.value == p.value()) {
                return yesNo;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", p));
    }
}
