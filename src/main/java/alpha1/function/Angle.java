package alpha1.function;

import alpha1.communication.Parameter;
import alpha1.communication.Parameterable;

public class Angle implements Parameterable {

    int value;

    public Angle(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }

}
