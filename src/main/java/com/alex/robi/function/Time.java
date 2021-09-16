package com.alex.robi.function;

import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Parameterable;

public class Time implements Parameterable {

    public Parameter asParameter() {
        return Parameter.of(50);
    }

}
