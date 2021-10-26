package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static com.robi.alpha1p.api.communication.Parameters.asParameters;
import static com.robi.alpha1p.api.communication.Parameters.parameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.robi.alpha1p.api.communication.Parameter;
import com.robi.alpha1p.api.communication.Parameterable;
import com.robi.alpha1p.api.communication.Parameters;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParametersTest {

    @Nested
    class builder {
        @Test
        void merge() {
            Parameters parameters = Parameters.parameters(Parameter.of(1), Parameter.of(2), parameters(Parameter.of(3), Parameter.of(4)));
            assertThat(parameters.first(), equalTo(Parameter.of(1)));
            assertThat(parameters.second(), equalTo(Parameter.of(2)));
            assertThat(parameters.third(), equalTo(Parameter.of(3)));
            assertThat(parameters.fourth(), equalTo(Parameter.of(4)));
        }
    }

    @Test
    void testToString() {
        Parameters parameters = parameters(Parameter.of(0), Parameter.of(1));

        assertThat(parameters.toString(), equalTo("[ \"0x0, (int)0\", \"0x1, (int)1\" ]"));
    }

    @Test
    void length() {
        assertThat(parameters(Parameter.of(0), Parameter.of(0)).lenght(), equalTo(2));
    }

    @Test
    void parameterable() {
        assertThat(Parameters.parameters(() -> Parameter.NOP_PARAM), equalTo(Parameters.parameters(Parameter.NOP_PARAM)));
    }

    private class A implements Parameterable {
        @Override
        public Parameter asParameter() {
            // TODO Auto-generated method stub
            return null;
        }
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
    void thirdExact() {
        assertThat(asParameters("abc").third(), equalTo(Parameter.of(99)));
    }

    @Test
    void fourth() {
        assertThat(asParameters("abcdefg").fourth(), equalTo(Parameter.of(100)));
    }

    @Test
    void fourthExact() {
        assertThat(asParameters("abcd").fourth(), equalTo(Parameter.of(100)));
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

    @Nested
    class checkSum {

        @Test
        void lowChecksum() {
            assertThat(parameters(of(1), of(1), of(1)).checksum(), is(of(3)));
        }

        @Test
        void zeroChecksum() {
            assertThat(parameters(of(0)).checksum(), is(of(0)));
        }

        @Test
        void highChecksum() {
            assertThat(parameters(of(255), of(255), of(255)).checksum(), is(of(253)));
        }

        @Test
        void checksumSumOneByte() {
            assertThat(parameters(of(255), of(0), of(0)).checksum(), is(of(255)));
        }

        @Test
        void checksumSumTwoBytes() {
            assertThat(parameters(of(255), of(1)).checksum(), is(of(0)));
        }
    }

    @Nested
    class equals {
        @Test
        void wrongType() {
            assertThat(parameters(of(255)).equals(""), is(false));
        }

        @Test
        void nullValue() {
            assertThat(parameters(of(255)).equals(null), is(false));
        }

        @Test
        void sameInstance() {
            Parameters p = parameters(of(255));
            assertThat(p.equals(p), is(true));
        }

        @Test
        void equalsT() {
            assertThat(parameters(of(255)).equals(parameters(of(255))), is(true));
        }
    }
}
