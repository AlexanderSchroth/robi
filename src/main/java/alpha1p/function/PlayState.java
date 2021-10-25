package alpha1p.function;

import alpha1p.communication.Parameter;
import alpha1p.communication.Payload;
import java.text.MessageFormat;
import java.util.List;

public enum PlayState {

    NonePause(Parameter.of(0x01)), Pause(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x01);
    private Parameter paramter;

    private PlayState(Parameter parameter) {
        this.paramter = parameter;
    }

    public static PlayState fromRobotStateResponse(List<Payload> response) {
        Payload payload = response.get(0);

        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (PlayState playState : PlayState.values()) {
            if (playState.paramter.equals(payload.parameters().second())) {
                return playState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", payload.parameters().second()));
    }

}
