package com.robi.alpha1p.api.communication;

import static com.robi.alpha1p.api.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

public class ParameterTest {

    @Test
    void testToString0() {
        assertThat(of(0).toString(), CoreMatchers.equalToObject("0x0, (int)0"));
    }

    @Test
    void testToString255() {
        assertThat(of(255).toString(), equalToObject("0xFF, (int)255"));
    }

    @Test
    void toLow() {
        assertThrows(IllegalArgumentException.class, () -> of(-1));
    }

    @Test
    void toHigh() {
        assertThrows(IllegalArgumentException.class, () -> of(256));
    }
}
