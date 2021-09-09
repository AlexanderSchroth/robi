package com.alex.robi;

public class AlphaMovement implements Movement {

    private Communication communication;

    public AlphaMovement(Communication communication) {
        this.communication = communication;
    }

    @Override
    public MoveResponse move(Servo servo, Angle angle, Time time) {
        return communication.send(new Payload.Builder()
            .withCommand(Command.ControllingTheMotionOfASingleServo)
            .withParameters(Parameters.parameters(
                servo.asParameter(),
                time.asParameter(),
                angle.asParameter()))
            .build(), messages -> new MoveResponse(messages));
    }

    @Override
    public OffsetResponse offset(Servo servo, Offset offset) {
        return communication.send(new Payload.Builder()
            .withCommand(Command.ControllingOffsetOfASingleServo)
            .withParameters(Parameters.parameters(
                servo.asParameter(),
                offset.asParameter()[0],
                offset.asParameter()[1]))
            .build(),
            messages -> new OffsetResponse(messages));
    }

    @Override
    public Offset readOffset(Servo servo) {
        return communication.send(new Payload.Builder()
            .withCommand(Command.ReadingOffsetValueOfASingleServo)
            .withParameters(
                Parameters.parameters(
                    servo.asParameter()))
            .build(), Offset::fromReadOffest);
    }

    public static interface MovePromise {

        void moveSuccess();

        void wrongServoId();

        void allowServoAngleAccess();

        void noReplyFromServo();
    }
}
