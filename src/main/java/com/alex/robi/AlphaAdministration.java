package com.alex.robi;

public class AlphaAdministration implements Adminstration {

    private Communication communication;

    public AlphaAdministration(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void powerOffAllServos() {
        Payload build = new Payload.Builder()
            .withCommand(Command.PoweringOfAllServos)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();
        communication.send(build, messages -> null);
    }

    @Override
    public void handshake() {
        Payload build = new Payload.Builder()
            .withCommand(Command.BTHandshake)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();
        communication.send(build, messages -> null);
    }

    @Override
    public void obtainActionList() {
        Payload build = new Payload.Builder()
            .withCommand(Command.ObtainingAnActionList)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();
        communication.send(build, messages -> null);
    }

    @Override
    public void playStop() {
        Payload build = new Payload.Builder()
            .withCommand(Command.PlayStop)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();
        communication.send(build, messages -> null);
    }

    @Override
    public RobotState state() {
        Payload send = new Payload.Builder()
            .withCommand(Command.ReadingRobotState)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();

        return communication.send(send, message -> {
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
        Payload send = new Payload.Builder()
            .withCommand(Command.ReadingClockParameters)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();

        return communication.send(send, messages -> {
            return ClockParameter.fromMessage(messages.get(0));
        });
    }

    @Override
    public String readSn() {
        Payload send = new Payload.Builder()
            .withCommand(Command.ReadingTheSnOfTheTobot)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();

        return communication.send(send, messages -> {
            return ClockParameter.asString(messages.get(0).parameters());
        });
    }

    @Override
    public String readSoftwareVersion() {
        Payload send = new Payload.Builder()
            .withCommand(Command.ReadingTheSnOfTheTobot)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build();

        return communication.send(send, messages -> {
            return ClockParameter.asString(messages.get(0).parameters());
        });
    }

    @Override
    public void implementActionList(String name) {
        Payload send = new Payload.Builder()
            .withCommand(Command.ImplementingAnActionList)
            .withParameters(Parameters.asParameters(name))
            .build();

        communication.send(send, messages -> {
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
}
