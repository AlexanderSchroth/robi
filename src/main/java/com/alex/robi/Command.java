package com.alex.robi;

import java.text.MessageFormat;

public enum Command {

    BTHandshake(0x01), //
    PlayStop(0x05), //
    ReadingRobotState(0x0A, 5), //
    ControllingOffsetOfASingleServo(0x26), //
    ControllingTheMotionOfASingleServo(0x22), //
    ReadingOffsetValueOfASingleServo(0x28), //
    PlayCompletion(0x31);

    private int value;
    private int expectedResponseMessages;

    Command(int value, int expectedResponseMessages) {
        this.value = value;
        this.expectedResponseMessages = expectedResponseMessages;
    }

    Command(int value) {
        this.value = value;
        this.expectedResponseMessages = 1;
    }

    public int value() {
        return value;
    }

    public int expectedResponseMessages() {
        return expectedResponseMessages;
    }

    public static Command findByValue(int intValue) {
        for (Command c : values()) {
            if (c.value == intValue) {
                return c;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Command {0} not found!", intValue));
    }
}
