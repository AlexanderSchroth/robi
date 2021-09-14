package com.alex.robi.api;

import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Parameterable;

public class Angle implements Parameterable {

    int value;

    public Angle(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }

}
