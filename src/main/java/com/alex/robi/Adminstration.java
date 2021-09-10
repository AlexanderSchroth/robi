package com.alex.robi;

public interface Adminstration {

    void implementActionList(String name);

    void powerOffAllServos();

    void obtainActionList();

    ClockParameter readingClockParameters();

    void handshake();

    RobotState state();

    void playStop();

    String readSn();

    String readSoftwareVersion();
}
