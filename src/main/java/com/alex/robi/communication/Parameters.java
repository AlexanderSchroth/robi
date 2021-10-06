package com.alex.robi.communication;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Parameters {

    private static final int B_1111_1111 = 0xFF;

    private List<Parameter> parameters;

    public int lenght() {
        return parameters.size();
    }

    private Parameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public static Parameters parameters(Parameter p1, Parameter p2, Parameters params) {
        List<Parameter> newParams = new ArrayList<>(params.parameters);
        newParams.add(0, p1);
        newParams.add(1, p2);
        return new Parameters(newParams);
    }

    public static Parameters parameters(Parameter... params) {
        return new Parameters(Arrays.asList(params));
    }

    public static Parameters parameters(List<Parameter> params) {
        return new Parameters(params);
    }

    public static Parameters parameters(Parameterable... params) {
        return new Parameters(asList(params).stream()
            .map(Parameterable::asParameter)
            .collect(toList()));
    }

    public Parameter checksum() {
        return Parameter.of(parameters.stream().mapToInt(Parameter::value).sum() & B_1111_1111);
    }

    public Parameter first() {
        return parameterNumber(1);
    }

    public Parameter second() {
        return parameterNumber(2);
    }

    public Parameter third() {
        return parameterNumber(3);
    }

    public Parameter fourth() {
        return parameterNumber(4);
    }

    public Parameter fifth() {
        return parameterNumber(5);
    }

    public List<Parameter> all() {
        return parameters;
    }

    public String asString() {
        StringBuffer sb = new StringBuffer();
        for (Parameter p : parameters) {
            sb.append(p.asString());
        }
        return sb.toString();
    }

    public Parameters subset(int from, int to) {
        return new Parameters(parameters.subList(from, to));
    }

    private Parameter parameterNumber(int n) {
        if (parameters.size() >= n) {
            return parameters.get(n - 1);
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Try to read {0}. parameter which is not present", n));
        }
    }

    public int[] asArray() {
        return parameters.stream().mapToInt(p -> p.value()).toArray();
    }

    public static Parameters asParameters(String aString) {
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < aString.length(); i++) {
            char c = aString.charAt(i);
            parameters.add(Parameter.of(c));
        }
        return new Parameters(parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Parameters that = (Parameters) obj;
        return new EqualsBuilder()
            .append(this.parameters, that.parameters)
            .build();
    }

    public void toJson(ObjectNode node) {
        node.put("asString", asString());
        ArrayNode arrayNode = node.putArray("asInt");
        for (int v : asArray()) {
            arrayNode.add(v);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(parameters)
            .toHashCode();
    }
}
