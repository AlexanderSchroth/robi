package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameterable;

public enum Servo implements Parameterable {

    LEFT_SHOULDER(1), LEFT_ARM(2), LEFT_ELBOW(3), RIGHT_SHOULDER(4), RIGHT_ARM(5), RIGHT_ELBOW(6);

    private int value;

    Servo(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }
}