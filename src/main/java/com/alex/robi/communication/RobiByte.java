package com.alex.robi.communication;

public class RobiByte {

    private int value;

    private RobiByte(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value to low");
        }
        if (value > 255) {
            throw new IllegalArgumentException("value to high");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("0x%1X", value) + ", (int)" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        RobiByte that = (RobiByte) obj;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public static RobiByte robiByte(int value) {
        return new RobiByte(value);
    }

    public int value() {
        return value;
    }
}
