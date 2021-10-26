package com.robi.alpha1p.api.communication;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Parameter {

    public static Parameter NOP_PARAM = new Parameter(0x00);

    private int value;

    private Parameter(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value to low");
        }
        if (value > 255) {
            throw new IllegalArgumentException("value to high");
        }
        this.value = value;
    }

    public int value() {
        return value;
    }

    public boolean valueEqualTo(int otherValue) {
        return value == otherValue;
    }

    public String asString() {
        return new String(Character.toChars(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Parameter that = (Parameter) obj;
        return new EqualsBuilder()
            .append(this.value, that.value)
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(value)
            .toHashCode();
    }

    @Override
    public String toString() {
        return String.format("0x%1X", value) + ", (int)" + value;
    }

    public static Parameter of(int value) {
        if (value > 255) {
            throw new IllegalArgumentException("Paramter must be less then 255");
        }
        return new Parameter(value);
    }
}
