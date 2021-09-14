package com.alex.robi;

public class Angle implements Parameterable {

    int value;

    public Angle(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }

}
