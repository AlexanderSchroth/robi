package com.robi.alpha1p.api.function;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameters;
import com.robi.alpha1p.api.communication.Payload;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Offset {

    public static Offset ZERO = new Offset(0);

    private int value;

    public Offset(int value) {
        if (value < -255 || value > 255) {
            throw new IllegalArgumentException("Offset must be in range -255 to 255");
        }
        this.value = value;
    }

    public Offset sum(int by) {
        return new Offset(value + by);
    }

    public Offset sum(Offset other) {
        return new Offset(other.value + value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    Parameter sign() {
        if (value < 0) {
            return Parameter.of(0);
        } else {
            return Parameter.of(255);
        }
    }

    Parameter absValue() {
        return Parameter.of(Math.abs(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Offset that = (Offset) obj;
        return new EqualsBuilder()
            .append(this.value, that.value)
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(value)
            .toHashCode();
    }

    public static Offset fromReadOffest(List<Payload> response) {
        Parameters parameters = response.get(0).parameters();
        int sign = parameters.second().value();
        int unsignedOffset = parameters.third().value();

        if (sign == 0) {
            return new Offset(unsignedOffset * -1);
        } else if (sign == 255) {
            return new Offset(unsignedOffset * 1);
        } else {
            throw new IllegalStateException("Unexpected sign :" + sign);
        }
    }
}
