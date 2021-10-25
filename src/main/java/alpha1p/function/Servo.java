package alpha1p.function;

import alpha1p.communication.Parameter;
import alpha1p.communication.Parameterable;

public enum Servo implements Parameterable {

    LEFT_SHOULDER(1), LEFT_ARM(2), LEFT_ELBOW(3), RIGHT_SHOULDER(4), RIGHT_ARM(5), RIGHT_ELBOW(6);

    private int value;

    Servo(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }
}