package alpha1p.function;

import alpha1p.communication.Parameter;
import alpha1p.communication.Parameterable;

public class Time implements Parameterable {

    public Parameter asParameter() {
        return Parameter.of(50);
    }

}
