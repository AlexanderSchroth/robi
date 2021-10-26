package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameterable;

public class Time implements Parameterable {

    public Parameter asParameter() {
        return Parameter.of(50);
    }

}
