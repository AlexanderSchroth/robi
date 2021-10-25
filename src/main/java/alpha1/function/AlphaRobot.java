package alpha1.function;

import static alpha1.communication.Payload.payload;

import alpha1.communication.Command;
import alpha1.communication.Communication;
import alpha1.communication.Parameters;
import alpha1.communication.Payload;
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
            int value = messages.get(0).parameters().first().value();
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
        communication.send(payload(Command.ControllingOffsetOfASingleServo, servo.asParameter(), offset.offset1(), offset.offset2()),
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