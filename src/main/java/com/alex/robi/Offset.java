package com.alex.robi;

import java.util.List;

public class Offset {

    private int offset;

    public Offset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return Integer.toString(offset);
    }

    public Parameter[] asParameter() {
        char sign;
        if (offset < 0) {
            sign = '-';
        } else {
            sign = '+';
        }
        return new Parameter[] { Parameter.of((int) sign), Parameter.of(offset) };
    }

    public static Offset fromReadOffest(List<Message> messages) {
        Message message = messages.get(0);
        char sign = (char) message.parameters()[1].value();
        int unsigned = message.parameters()[2].value();
        int sigedValue;
        if (sign == '-') {
            sigedValue = unsigned * -1;
        } else if (sign == '+') {
            sigedValue = unsigned * 1;
        } else {
            throw new IllegalArgumentException("Unkown sign");
        }
        return new Offset(sigedValue);
    }
}
