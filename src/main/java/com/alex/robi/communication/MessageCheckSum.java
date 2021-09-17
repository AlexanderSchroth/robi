package com.alex.robi.communication;

import java.util.List;
import java.util.stream.IntStream;

public class MessageCheckSum {

    private static final int B_1111_1111 = 0xFF;

    private int value;

    public MessageCheckSum(int[] fragments) {
        this.value = compute(fragments);
    }

    public MessageCheckSum(List<Parameter> fragments) {
        this.value = compute(fragments.stream().mapToInt(Parameter::value).toArray());
    }

    private int compute(int[] fragments) {
        return IntStream.of(fragments).sum() & B_1111_1111;
    }

    @Override
    public String toString() {
        return Message.dump(value);
    }

    public int value() {
        return value;
    }

    public boolean isValid(int excpected) {
        return this.value == excpected;
    }
}
