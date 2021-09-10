package com.alex.robi;

import java.text.MessageFormat;

public enum Command {

    BTHandshake(0x01), //
    ObtainingAnActionList(0x02), //
    ImplementingAnActionList(0x03), //
    PlayStop(0x05), //
    PoweringOfAllServos(0X0C), //
    ReadingClockParameters(0X0F), //
    ControllingAllServoIndicator(0x0D), //
    ReadingRobotState(0x0A, 5), //
    ControllingOffsetOfASingleServo(0x26), //
    ControllingTheMotionOfASingleServo(0x22), //
    ReadingOffsetValueOfASingleServo(0x28), //
    PlayCompletion(0x31), //
    SendingTheActionList(0x80), //
    CompleteActionListSending(0x81), // ;
    ReadingTheSoftwareVersion(0x11), // ;
    ReadingTheSnOfTheTobot(0x33);

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
