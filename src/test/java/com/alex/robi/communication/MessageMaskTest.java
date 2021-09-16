package com.alex.robi.communication;

import static com.alex.robi.communication.RobiByte.robiByte;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

public class MessageMaskTest {

    private static final int[] TEST_MESSAGE = new int[] { 251, 191, 16, 1, 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68, 101, 237 };

    @Test
    void header1() {
        assertThat(new MessageMask(TEST_MESSAGE).header1(), equalTo(robiByte(251)));
    }

    @Test
    void header2() {
        assertThat(new MessageMask(TEST_MESSAGE).header2(), equalTo(robiByte(191)));
    }

    @Test
    void command() {
        assertThat(new MessageMask(TEST_MESSAGE).command(), equalTo(robiByte(1)));
    }

    @Test
    void length() {
        assertThat(new MessageMask(TEST_MESSAGE).length(), equalTo(robiByte(16)));
    }

    @Test
    void parameters() {
        List<RobiByte> parameters = new MessageMask(TEST_MESSAGE).parameters();
        assertThat(parameters,
            CoreMatchers.hasItems(robiByte(65), robiByte(108), robiByte(112), robiByte(104), robiByte(97), robiByte(49), robiByte(95), robiByte(48),
                robiByte(51),
                robiByte(55), robiByte(68)));
    }

    @Test
    void check() {
        assertThat(new MessageMask(TEST_MESSAGE).check(), equalTo(robiByte(101)));
    }

    @Test
    void end() {
        assertThat(new MessageMask(TEST_MESSAGE).endCharacter(), equalTo(robiByte(237)));
    }

}
