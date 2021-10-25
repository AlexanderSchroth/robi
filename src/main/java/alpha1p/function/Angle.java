package alpha1p.function;

import alpha1p.communication.Parameter;
import alpha1p.communication.Parameterable;

public class Angle implements Parameterable {

    int value;

    public Angle(int value) {
        this.value = value;
    }

    public Parameter asParameter() {
        return Parameter.of(value);
    }

}
