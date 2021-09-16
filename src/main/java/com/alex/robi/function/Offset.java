package com.alex.robi.function;

import com.alex.robi.communication.Message;
import com.alex.robi.communication.Parameter;
import java.util.List;

public class Offset {

    private int offset1;
    private int offset2;

    public Offset(int offset1, int offset2) {
        this.offset1 = offset1;
        this.offset2 = offset2;
    }

    @Override
    public String toString() {
        return Integer.toString(offset1) + "/" + Integer.toString(offset2);
    }

    Parameter offset1() {
        return Parameter.of(offset1);
    }

    Parameter offset2() {
        return Parameter.of(offset2);
    }

    public Parameter[] asParameter() {
        char sign;
        if (offset2 < 0) {
            sign = '-';
        } else {
            sign = '+';
        }
        return new Parameter[] { Parameter.of((int) sign), Parameter.of(offset2) };
    }

    public static Offset fromReadOffest(List<Message> messages) {
        Message message = messages.get(0);
        int offset1 = message.parameters()[1].value();
        int offset2 = message.parameters()[2].value();
        return new Offset(offset1, offset2);
    }
}
