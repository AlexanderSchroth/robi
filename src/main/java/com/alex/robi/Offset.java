package com.alex.robi;

public class Offset {

    private int sign = 0;
    private int offset = 5;

    public Parameter[] asParameter() {
        char plus = '+';
        char minus = '-';

        return new Parameter[] { Parameter.of((int) minus), Parameter.of(offset) };
    }

}
