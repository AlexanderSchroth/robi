package com.alex.robi;

public class AlphaMovement implements Movement {

    private Communication communication;

    public AlphaMovement(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void move(Servo servo, Angle angle, Time time) throws MoveException {
        communication.send(new Payload.Builder()
            .withCommand(Command.ControllingTheMotionOfASingleServo)
            .withParameters(Parameters.parameters(
                servo.asParameter(),
                time.asParameter(),
                angle.asParameter()))
            .build(), messages -> {
                int response = messages.get(0).parameters()[1].value();
                if (response == 0X00) {

                } else if (response == 0x01) {
                    throw new MoveException("wrong servo ID");
                } else if (response == 0x02) {
                    throw new MoveException("allow servo angle acess");
                } else if (response == 0x03) {
                    throw new MoveException("no reply from servo");
                }
                return "success";
            });
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

    @Override
    public void servoIndicators(boolean on) {
        Parameter p;
        if (on) {
            p = Parameter.of(0x01);
        } else {
            p = Parameter.of(0x00);
        }

        communication.send(new Payload.Builder()
            .withCommand(Command.ControllingAllServoIndicator)
            .withParameters(Parameters.parameters(p))
            .build(), message -> "");
    }

    public static interface MovePromise {

        void moveSuccess();

        void wrongServoId();

        void allowServoAngleAccess();

        void noReplyFromServo();
    }
}
