package com.alex.robi;

public class AlphaAdministration implements Adminstration {

    private Communication communication;

    public AlphaAdministration(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void handshake() {
        Payload build = new Payload.Builder()
            .withCommand(Command.BTHandshake)
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
}
