package com.alex.robi;

public class AlphaAdministration implements Adminstration {

    private Communication communication;

    public AlphaAdministration(Communication communication) {
        this.communication = communication;
    }

    @Override
    public void handshake() {
        communication.send(new Payload.Builder()
            .withCommand(Command.BTHandshake)
            .withParameters(Parameters.parameters(Parameter.of(0x00))).build());
    }

}
