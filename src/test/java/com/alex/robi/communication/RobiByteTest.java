package com.alex.robi.communication;

import static com.alex.robi.communication.RobiByte.robiByte;
import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

public class RobiByteTest {

    @Test
    void testToString0() {
        assertThat(robiByte(0).toString(), CoreMatchers.equalToObject("0x0, (int)0"));
    }

    @Test
    void testToString255() {
        assertThat(robiByte(255).toString(), equalToObject("0xFF, (int)255"));
    }

    @Test
    void toLow() {
        assertThrows(IllegalArgumentException.class, () -> robiByte(-1));
    }

    @Test
    void toHigh() {
        assertThrows(IllegalArgumentException.class, () -> robiByte(256));
    }
}
