package com.alex.robi.communication;

import static com.alex.robi.communication.Command.BTHandshake;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CommandTest {

    @Nested
    class findByValue {
        @Test
        void known() {
            assertThat(Command.findByValue(1), equalTo(BTHandshake));
        }

        @Test
        void unknown() {
            assertThrows(IllegalArgumentException.class, () -> Command.findByValue(666));
        }
    }

    @Test
    void expectedMessageOfReadRobotState() {
        assertThat(Command.ReadingRobotState.expectedResponseMessages(), equalTo(5));
    }

    @Test
    void value() {
        assertThat(Command.ReadingRobotState.value(), equalTo(10));
    }
}
