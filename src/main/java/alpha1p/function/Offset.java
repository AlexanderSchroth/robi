package alpha1p.function;

import alpha1p.communication.Parameter;
import alpha1p.communication.Parameters;
import alpha1p.communication.Payload;
import java.util.List;

public class Offset {

    private int offset1;
    private int offset2;

    public Offset(int offset1, int offset2) {
        this.offset1 = offset1;
        this.offset2 = offset2;
    }

    @Override
    public String toString() {
        return Integer.toString(offset1) + "/" + Integer.toString(offset2);
    }

    Parameter offset1() {
        return Parameter.of(offset1);
    }

    Parameter offset2() {
        return Parameter.of(offset2);
    }

    public Parameter[] asParameter() {
        char sign;
        if (offset2 < 0) {
            sign = '-';
        } else {
            sign = '+';
        }
        return new Parameter[] { Parameter.of((int) sign), Parameter.of(offset2) };
    }

    public static Offset fromReadOffest(List<Payload> response) {
        Parameters parameters = response.get(0).parameters();
        int offset1 = parameters.first().value();
        int offset2 = parameters.second().value();
        return new Offset(offset1, offset2);
    }
}
