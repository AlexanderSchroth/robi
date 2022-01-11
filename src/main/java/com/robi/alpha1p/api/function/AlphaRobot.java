package com.robi.alpha1p.api.function;

import static com.robi.alpha1p.api.communication.Payload.payload;

import com.robi.alpha1p.api.communication.Command;
import com.robi.alpha1p.api.communication.Communication;
import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameters;
import com.robi.alpha1p.api.communication.Payload;
import java.text.MessageFormat;

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
                SoundState.fromRobotStateResponse(message.get(0)),
                PlayState.fromRobotStateResponse(message.get(1)),
                Volume.fromRobotState(message.get(2)),
                ServoIndicateState.fromRobotState(message.get(3)),
                TfCardInsertion.fromRobotState(message.get(4)));
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
            return messages.get(0).parameters().asString();
        });
    }

    @Override
    public String readSoftwareVersion() {
        return communication.send(payload(Command.ReadingTheSoftwareVersion), messages -> {
            return messages.get(0).parameters().asString();
        });
    }

    @Override
    public void implementActionList(String name) {
        communication.send(payload(Command.ImplementingAnActionList, Parameters.asParameters(name)), messages -> {
            Parameter first = messages.get(0).parameters().first();
            int value = first.value();
            if (value == 0x00) {
                return "success";
            } else if (value == 0x01) {
                throw new IllegalStateException("empty file name");
            } else if (value == 0x02) {
                throw new IllegalStateException("low battery");
            } else {
                throw new IllegalStateException(MessageFormat.format("value {0} unknown", first));
            }
        });
    }

    @Override
    public void move(Servo servo, Angle angle, Time time) throws MoveException {
        communication.send(payload(Command.ControllingTheMotionOfASingleServo, servo, time, angle), messages -> {
            int response = messages.get(0).parameters().second().value();
            if (response == 0X00) {
                return "success";
            } else if (response == 0x01) {
                throw new MoveException("wrong servo ID");
            } else if (response == 0x02) {
                throw new MoveException("allow servo angle acess");
            } else if (response == 0x03) {
                throw new MoveException("no reply from servo");
            } else {
                throw new MoveException(MessageFormat.format("Unkown move response {}", response));
            }
        });
    }

    @Override
    public void setOffset(Servo servo, Offset offset) throws SetOffsetException {
        communication.send(payload(Command.ControllingOffsetOfASingleServo, servo.asParameter(), offset.sign(), offset.absValue()),
            messages -> {
                int response = messages.get(0).parameters().second().value();
                if (response == 0X00) {
                    return "success";
                } else if (response == 0x01) {
                    throw new SetOffsetException("failure");
                } else if (response == 0x02) {
                    throw new SetOffsetException("no reply from servo");
                } else {
                    throw new SetOffsetException(MessageFormat.format("Unkown move response {}", response));
                }
            });
    }

    @Override
    public Offset readOffset(Servo servo) {
        return communication.send(Payload.payload(Command.ReadingOffsetValueOfASingleServo, servo), Offset::fromReadOffest);
    }

    @Override
    public void servoIndicators(OnOff onOff) {
        communication.send(Payload.payload(Command.ControllingAllServoIndicator, onOff), message -> "");
    }

    @Override
    public void close() throws Exception {
        communication.close();
    }
}
