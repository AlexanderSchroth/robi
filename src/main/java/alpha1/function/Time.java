package alpha1.function;

import alpha1.communication.Parameter;
import alpha1.communication.Parameterable;

public class Time implements Parameterable {

    public Parameter asParameter() {
        return Parameter.of(50);
    }

}
