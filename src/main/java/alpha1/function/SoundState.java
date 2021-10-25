package alpha1.function;

import alpha1.communication.Parameter;
import alpha1.communication.Payload;
import java.text.MessageFormat;
import java.util.List;

public enum SoundState {

    Mute(Parameter.of(0x01)), NoneMute(Parameter.of(0x00));

    private static final Parameter FLAG = Parameter.of(0x00);
    private Parameter parameter;

    private SoundState(Parameter value) {
        this.parameter = value;
    }

    public static SoundState fromRobotStateResponse(List<Payload> m) {
        Payload payload = m.get(0);
        if (!payload.parameters().first().equals(FLAG)) {
            throw new IllegalArgumentException("Wrong message?!?");
        }

        for (SoundState soundState : SoundState.values()) {
            if (soundState.parameter.equals(payload.parameters().second())) {
                return soundState;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Unkown value {}", payload.parameters().second()));
    }
}
