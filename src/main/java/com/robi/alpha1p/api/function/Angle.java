package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameterable;

public class Angle implements Parameterable {

    int value;

    public Angle(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }

}
