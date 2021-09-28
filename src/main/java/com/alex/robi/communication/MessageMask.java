package com.alex.robi.communication;

import static com.alex.robi.communication.Message.PARAMETER_COMMAND_HEADER_1;
import static com.alex.robi.communication.Message.PARAMETER_COMMAND_HEADER_2;
import static com.alex.robi.communication.Message.PARAMETER_END_CHARACTER;
import static com.alex.robi.communication.Parameter.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.IntStream;

class MessageMask {

    private int[] rawUnsignedBytes;

    MessageMask(int[] message) {
        this.rawUnsignedBytes = message;
    }

    Parameter header1() {
        Parameter header1 = of(rawUnsignedBytes[0]);
        if (header1.equals(PARAMETER_COMMAND_HEADER_1)) {
            return header1;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Header1 {0} not as expected {1}", header1, PARAMETER_COMMAND_HEADER_1));
        }
    }

    Parameter header2() {
        Parameter header2 = of(rawUnsignedBytes[1]);
        if (header2.equals(PARAMETER_COMMAND_HEADER_2)) {
            return header2;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Header2 {0} not as expected {1}", header2, PARAMETER_COMMAND_HEADER_2));
        }
    }

    Parameter length() {
        Parameter givenLength = of(rawUnsignedBytes[2]);
        Parameter expectedLength = of(rawUnsignedBytes.length - Message.FIXED_PARTS);

        if (givenLength.equals(expectedLength)) {
            throw new IllegalArgumentException(MessageFormat.format("Length byte {0} not as expected {1}", givenLength, expectedLength));
        } else {
            return givenLength;
        }
    }

    Parameter command() {
        return of(rawUnsignedBytes[3]);
    }

    List<Parameter> parameters() {
        return IntStream.of(rawUnsignedBytes)
            .skip(4)
            .limit(rawUnsignedBytes.length - Message.FIXED_PARTS)
            .mapToObj(Parameter::of)
            .collect(toList());
    }

    Parameter check() {
        List<Parameter> dataParameters = concat(of(length(), command()), parameters().stream()).collect(toList());
        Parameter expectedCheck = Parameters.parameters(dataParameters)
            .checksum();

        int value = rawUnsignedBytes[rawUnsignedBytes.length - 2];
        Parameter givenCheck = Parameter.of(value);

        if (givenCheck.equals(expectedCheck)) {
            return givenCheck;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Check byte {0} not as expected {1}", givenCheck, expectedCheck));
        }
    }

    Parameter endCharacter() {
        Parameter endCharacter = of(rawUnsignedBytes[rawUnsignedBytes.length - 1]);
        if (endCharacter.equals(PARAMETER_END_CHARACTER)) {
            return endCharacter;
        } else {
            throw new IllegalArgumentException(
                MessageFormat.format("End character {0} value not as expected {1}", endCharacter, PARAMETER_END_CHARACTER));
        }
    }

    Message toMessage() {
        return new Message.Builder()
            .withCommandHeader1(header1().value())
            .withCommandHeader2(header2().value())
            .withLength(length().value())
            .withCommand(command().value())
            .withParameters(parameters().stream().mapToInt(value -> value.value()).toArray())
            .withCheck(check().value())
            .withEndCharacter(endCharacter().value())
            .builder();
    }

    public static Message bytesAsMessage(int[] bytes) {
        return new MessageMask(bytes).toMessage();
    }
}
