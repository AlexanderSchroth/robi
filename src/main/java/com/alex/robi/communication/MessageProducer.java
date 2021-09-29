package com.alex.robi.communication;

import static com.alex.robi.communication.MessageMask.bytesAsMessage;
import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class MessageProducer {
    private boolean commandHeader1Received = false;
    private boolean commandHeader2Received = false;
    private boolean endCommandReceived = false;

    private int header1ExpectedValue;
    private int header2ExpectedValue;
    private int endCommandExpectedValue;

    private List<Integer> partialMessage;
    private Consumer<Message> partialMessageConsumer;

    public MessageProducer(int header1ExpectedValue, int header2ExpectedValue, int endCommandExpectedValue, Consumer<Message> partialMessageConsumer) {
        this.header1ExpectedValue = header1ExpectedValue;
        this.header2ExpectedValue = header2ExpectedValue;
        this.endCommandExpectedValue = endCommandExpectedValue;
        this.partialMessageConsumer = partialMessageConsumer;

        partialMessage = new ArrayList<>();
    }

    MessageProducer received(int value) {
        if (isExpectedHeader1(value)) {
            commandHeader1Received = true;
        }
        if (isExpectedHeader2(value)) {
            commandHeader2Received = true;
        }
        if (isExpectedEndCharacter(value)) {
            endCommandReceived = true;
        }

        partialMessage.add(value);

        if (commandHeader1Received && commandHeader2Received && endCommandReceived) {
            partialMessageConsumer.accept(bytesAsMessage(partialMessageAsIntArray()));
            reset();
        }
        return this;
    }

    private int[] partialMessageAsIntArray() {
        return partialMessage.stream()
            .mapToInt(integer -> integer)
            .toArray();
    }

    private boolean isExpectedHeader1(int value) {
        if (partialMessage.isEmpty()) {
            if (value == header1ExpectedValue) {
                return true;
            } else {
                throw new IllegalStateException(format("Expect commandHeader1 {0} to be the first int of a message", header1ExpectedValue));
            }
        }
        return false;
    }

    private boolean isExpectedEndCharacter(int value) {
        if (value == endCommandExpectedValue) {
            return true;
        }
        return false;
    }

    private boolean isExpectedHeader2(int value) {
        if (partialMessage.size() == 1) {
            if (value == header2ExpectedValue) {
                return true;
            } else {
                throw new IllegalStateException(format("Expect commandHeader2 {0} to be the second int of a message", header2ExpectedValue));
            }
        }
        return false;
    }

    private void reset() {
        partialMessage.clear();
        commandHeader1Received = false;
        commandHeader2Received = false;
        endCommandReceived = false;
    }
}
