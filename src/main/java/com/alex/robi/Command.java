package com.alex.robi;

public enum Command {

    BTHandshake(0x01),
    ControllingOffsetOfASingleServo(0x26), ControllingTheMotionOfASingleServo(0x22),;

    private int value;

    Command(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
