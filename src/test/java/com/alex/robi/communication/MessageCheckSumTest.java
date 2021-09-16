package com.alex.robi.communication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class MessageCheckSumTest {

    @Test
    void lowChecksum() {
        MessageCheckSum messageCheckSum = new MessageCheckSum(new int[] { 1, 1, 1 });

        assertThat(messageCheckSum.value(), is(3));
    }

    @Test
    void zeroChecksum() {
        MessageCheckSum messageCheckSum = new MessageCheckSum(new int[] { 0 });

        assertThat(messageCheckSum.value(), is(0));
    }

    @Test
    void highChecksum() {
        MessageCheckSum messageCheckSum = new MessageCheckSum(new int[] { 255, 255, 255 });

        assertThat(messageCheckSum.value(), is(253));
    }

    @Test
    void checksumSumOneByte() {
        MessageCheckSum messageCheckSum = new MessageCheckSum(new int[] { 255, 0, 0 });

        assertThat(messageCheckSum.value(), is(255));
    }

    @Test
    void checksumSumTwoBytes() {
        MessageCheckSum messageCheckSum = new MessageCheckSum(new int[] { 255, 1 });

        assertThat(messageCheckSum.value(), is(0));
    }

}
