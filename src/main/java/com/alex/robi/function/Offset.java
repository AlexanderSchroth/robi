package com.alex.robi.function;

import com.alex.robi.communication.Message;
import com.alex.robi.communication.Parameter;
import com.alex.robi.communication.Parameterable;
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

    public Sign sign() {
        if (offset < 0) {
            return Sign.Minus;
        } else {
            return Sign.Plus;
        }
    }

    public AbsolutOffset absulutOffset() {
        return new AbsolutOffset(Math.abs(offset));
    }

    public static enum Sign implements Parameterable {

        Plus, Minus;

        @Override
        public Parameter asParameter() {
            char sign;
            if (this == Plus) {
                sign = '+';
            } else {
                sign = '-';
            }
            return Parameter.of((int) sign);
        }
    }

    public static class AbsolutOffset implements Parameterable {

        private int value;

        public AbsolutOffset(int value) {
            this.value = value;
        }

        @Override
        public Parameter asParameter() {
            return Parameter.of((int) value);
        }
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
