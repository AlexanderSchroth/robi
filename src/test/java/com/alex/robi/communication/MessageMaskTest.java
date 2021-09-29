package com.alex.robi.communication;

import static com.alex.robi.communication.Parameter.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class MessageMaskTest {

    private static final int[] TEST_MESSAGE = new int[] { 251, 191, 16, 1, 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68, 101, 237 };

    @Test
    void header1() {
        assertThat(new MessageMask(TEST_MESSAGE).header1(), equalTo(of(251)));
    }

    @Test
    void header2() {
        assertThat(new MessageMask(TEST_MESSAGE).header2(), equalTo(of(191)));
    }

    @Test
    void command() {
        assertThat(new MessageMask(TEST_MESSAGE).command(), equalTo(of(1)));
    }

    @Test
    void length() {
        assertThat(new MessageMask(TEST_MESSAGE).length(), equalTo(of(16)));
    }

    @Test
    void parameters() {
        List<Parameter> parameters = new MessageMask(TEST_MESSAGE).parameters();
        assertThat(parameters,
            Matchers.contains(of(65), of(108), of(112), of(104), of(97), of(49), of(95), of(48),
                of(51),
                of(55), of(68)));
    }

    @Test
    void check() {
        assertThat(new MessageMask(TEST_MESSAGE).check(), equalTo(of(101)));
    }

    @Test
    void end() {
        assertThat(new MessageMask(TEST_MESSAGE).endCharacter(), equalTo(of(237)));
    }

    @Test
    void builderFromBytes() {
        Message messageFromBytes = MessageMask.bytesAsMessage(new int[] { 251, 191, 16, 1, 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68, 101, 237 });
        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(16)
            .withCommand(1)
            .withParameters(new int[] { 65, 108, 112, 104, 97, 49, 95, 48, 51, 55, 68 })
            .withCheck(101)
            .withEndCharacter(237)
            .build();
        assertThat(messageFromBytes, equalTo(expectedMessage));
    }

    @Test
    void builderFromBytesWithLargeParameters() {
        Message messageFromBytes = MessageMask.bytesAsMessage(new int[] { 251, 191, 15, 49, 98, 116, 95, 108, 105, 110, 107, 49, 50, 51, 185, 237 });
        Message expectedMessage = new Message.Builder()
            .withCommandHeader1(251)
            .withCommandHeader2(191)
            .withLength(15)
            .withCommand(49)
            .withParameters(new int[] { 98, 116, 95, 108, 105, 110, 107, 49, 50, 51 })
            .withCheck(185)
            .withEndCharacter(237)
            .build();
        assertThat(messageFromBytes, equalTo(expectedMessage));
    }

}
