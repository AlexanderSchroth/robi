package com.alex.robi;

public interface Robot {

    public static enum Servo implements Parameterable {

        LEFT_SHOULDER(1), LEFT_ARM(2), LEFT_ELBOW(3), RIGHT_SHOULDER(4), RIGHT_ARM(5), RIGHT_ELBOW(6);

        private int value;

        Servo(int value) {
            this.value = value;
        }

        public Parameter asParameter() {
            return Parameter.of(value);
        }
    }

    void implementActionList(String name);

    void powerOffAllServos();

    void obtainActionList();

    ClockParameter readingClockParameters();

    void handshake();

    RobotState state();

    void playStop();

    String readSn();

    String readSoftwareVersion();

    void move(Servo s, Angle angle, Time time);

    OffsetResponse offset(Servo servo, Offset offset);

    Offset readOffset(Servo servo);

    void servoIndicators(OnOff onOff);
}
