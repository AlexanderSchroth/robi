package com.alex.robi;

public class AlphaMovement implements Movement {

    private Communication communication;

    public AlphaMovement(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void move(Servo servo, Angle angle, Time time) {
        communication.send(new Payload.Builder()
            .withCommand(Command.ControllingTheMotionOfASingleServo)
            .withParameters(Parameters.parameters(
                servo.asParameter(),
                time.asParameter(),
                angle.asParameter()))
            .build());
    }

    @Override
    public void offset(Servo servo, Offset offset) {
        communication.send(new Payload.Builder()
            .withCommand(Command.ControllingOffsetOfASingleServo)
            .withParameters(Parameters.parameters(
                servo.asParameter(),
                offset.asParameter()[0],
                offset.asParameter()[1]))
            .build());
    }

    public static interface MovePromise {

        void moveSuccess();

        void wrongServoId();

        void allowServoAngleAccess();

        void noReplyFromServo();
    }
}
