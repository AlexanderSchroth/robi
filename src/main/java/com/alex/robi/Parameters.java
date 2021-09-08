package com.alex.robi;

import java.util.Arrays;
import java.util.List;

public class Parameters {

    private List<Parameter> parameters;

    public int lenght() {
        return parameters.size();
    }

    public static Parameters parameters(Parameter... params) {
        Parameters parameters = new Parameters();
        parameters.parameters = Arrays.asList(params);
        return parameters;
    }

    public int[] asArray() {
        return parameters.stream().mapToInt(p -> p.value()).toArray();
    }

    public int sum() {
        return parameters.stream().map(p -> p.value()).reduce(0, Integer::sum);
    }
}
