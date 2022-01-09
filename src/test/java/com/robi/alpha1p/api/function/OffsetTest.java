package com.robi.alpha1p.api.function;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static com.robi.alpha1p.api.communication.Payload.payload;
import static com.robi.alpha1p.api.function.Offset.fromReadOffest;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.robi.alpha1p.api.communication.Command;
import com.robi.alpha1p.api.communication.Parameter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OffsetTest {

    @Test
    void lowerBound() {
        assertThrows(IllegalArgumentException.class, () -> new Offset(-256));
    }

    @Test
    void upperBound() {
        assertThrows(IllegalArgumentException.class, () -> new Offset(256));
    }

    @Nested
    class testIncrease {
        @Test
        void fromZeroToOne() {
            Offset increaseOffset = new Offset(0).sum(1);
            assertThat(increaseOffset.sign(), equalTo(Parameter.of(255)));
            assertThat(increaseOffset.absValue(), equalTo(Parameter.of(1)));
        }

        @Test
        void fromMinusOneToOne() {
            Offset increaseOffset = new Offset(-1).sum(2);
            assertThat(increaseOffset.sign(), equalTo(Parameter.of(255)));
            assertThat(increaseOffset.absValue(), equalTo(Parameter.of(1)));
        }

        @Test
        void fromOneToMinusOne() {
            Offset increaseOffset = new Offset(1).sum(-2);
            assertThat(increaseOffset.sign(), equalTo(Parameter.of(0)));
            assertThat(increaseOffset.absValue(), equalTo(Parameter.of(1)));
        }
    }

    @Nested
    class fromReadOffset {
        @Test
        void plus() {
            Offset fromReadOffest = fromReadOffest(asList(payload(Command.ReadingOffsetValueOfASingleServo, of(123), of(255), of(1))));
            assertThat(fromReadOffest, equalTo(new Offset(1)));
        }

        @Test
        void minus() {
            Offset fromReadOffest = fromReadOffest(asList(payload(Command.ReadingOffsetValueOfASingleServo, of(123), of(0), of(1))));
            assertThat(fromReadOffest, equalTo(new Offset(-1)));
        }

        @Test
        void illegal() {
            assertThrows(IllegalStateException.class,
                () -> fromReadOffest(asList(payload(Command.ReadingOffsetValueOfASingleServo, of(123), of(123), of(1)))));
        }
    }
}
