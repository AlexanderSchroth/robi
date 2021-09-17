package com.alex.robi.communication;

import static com.alex.robi.communication.Parameters.asParameters;
import static com.alex.robi.communication.Parameters.parameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParametersTest {

    @Test
    void length() {
        assertThat(parameters(Parameter.of(0), Parameter.of(0)).lenght(), equalTo(2));
    }

    @Test
    void testAsString() {
        assertThat(asParameters("ab").asString(), equalTo("ab"));
    }

    @Test
    void first() {
        assertThat(asParameters("abcdefg").first(), equalTo(Parameter.of(97)));
    }

    @Test
    void second() {
        assertThat(asParameters("abcdefg").second(), equalTo(Parameter.of(98)));
    }

    @Test
    void third() {
        assertThat(asParameters("abcdefg").third(), equalTo(Parameter.of(99)));
    }

    @Test
    void fourth() {
        assertThat(asParameters("abcdefg").fourth(), equalTo(Parameter.of(100)));
    }

    @Test
    void fifth() {
        assertThat(asParameters("abcdefg").fifth(), equalTo(Parameter.of(101)));
    }

    @Test
    void throwsIndexException() {
        assertThrows(IllegalArgumentException.class, () -> asParameters("a").second());
    }

    @Nested
    class sublist {
        @Test
        void fromBegin() {
            assertThat(asParameters("abc").subset(0, 1).asString(), equalTo("a"));
        }

        @Test
        void middle() {
            assertThat(asParameters("abc").subset(1, 2).asString(), equalTo("b"));
        }

        @Test
        void toEnd() {
            assertThat(asParameters("abc").subset(2, 3).asString(), equalTo("c"));
        }
    }
}
