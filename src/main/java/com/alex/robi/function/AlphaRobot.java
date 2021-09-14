package com.alex.robi.api;

import static com.alex.robi.communication.Payload.payload;

import com.alex.robi.communication.Command;
import com.alex.robi.communication.Communication;
import com.alex.robi.communication.Parameters;
import com.alex.robi.communication.Payload;

public class AlphaRobot implements Robot {

    private Communication communication;

    public AlphaRobot(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void powerOffAllServos() {
        communication.send(payload(Command.PoweringOfAllServos), messages -> null);
    }

    @Override
    public void handshake() {
        communication.send(payload(Command.BTHandshake), messages -> null);
    }

    @Override
    public void obtainActionList() {
        communication.send(payload(Command.ObtainingAnActionList), messages -> null);
    }

    @Override
    public void playStop() {
        communication.send(payload(Command.PlayStop), messages -> null);
    }

    @Override
    public RobotState state() {
        return communication.send(payload(Command.ReadingRobotState), message -> {
            return new RobotState(
                SoundState.fromRobotStateResponse(message),
                PlayState.fromRobotStateResponse(message),
                Volume.fromRobotState(message),
                ServoIndicateState.fromRobotState(message),
                TfCardInsertion.fromRobotState(message));
        });
    }

    @Override
    public ClockParameter readingClockParameters() {
        return communication.send(payload(Command.ReadingClockParameters), messages -> {
            return ClockParameter.fromMessage(messages.get(0));
        });
    }

    @Override
    public String readSn() {
        return communication.send(payload(Command.ReadingTheSnOfTheTobot), messages -> {
            return ClockParameter.asString(messages.get(0).parameters());
        });
    }

    @Override
    public String readSoftwareVersion() {
        return communication.send(payload(Command.ReadingTheSoftwareVersion), messages -> {
            return ClockParameter.asString(messages.get(0).parameters());
        });
    }

    @Override
    public void implementActionList(String name) {
        communication.send(payload(Command.ImplementingAnActionList, Parameters.asParameters(name)), messages -> {
            int value = messages.get(0).parameters()[0].value();
            if (value == 0x00) {
                return "success";
            } else if (value == 0x01) {
                throw new IllegalStateException("empty file name");
            } else if (value == 0x02) {
                throw new IllegalStateException("low battery");
            } else {
                throw new IllegalStateException("unkown");
            }
        });
    }

    @Override
    public void move(Servo servo, Angle angle, Time time) throws MoveException {
        communication.send(payload(Command.ControllingTheMotionOfASingleServo, servo, time, angle), messages -> {
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
        return communication.send(payload(Command.ControllingOffsetOfASingleServo, servo, offset.sign(), offset.absulutOffset()),
            messages -> new OffsetResponse(messages));
    }

    @Override
    public Offset readOffset(Servo servo) {
        return communication.send(Payload.payload(Command.ReadingOffsetValueOfASingleServo, servo), Offset::fromReadOffest);
    }

    @Override
    public void servoIndicators(OnOff onOff) {
        communication.send(Payload.payload(Command.ControllingAllServoIndicator, onOff), message -> "");
    }
}
