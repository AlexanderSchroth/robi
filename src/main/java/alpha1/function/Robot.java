package alpha1.function;

public interface Robot extends AutoCloseable {

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

    void setOffset(Servo servo, Offset offset);

    Offset readOffset(Servo servo);

    void servoIndicators(OnOff onOff);
}
