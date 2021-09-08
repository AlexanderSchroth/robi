package com.alex.robi;

public class Parameter {

    private int value;

    private Parameter(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Parameter of(int value) {
        if (value > 255) {
            throw new IllegalArgumentException("Paramter must be less then 255");
        }
        return new Parameter(value);
    }
}
