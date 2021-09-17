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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MessageMask {

    private int[] rawUnsignedBytes;

    public MessageMask(int[] message) {
        this.rawUnsignedBytes = message;
    }

    public Parameter header1() {
        Parameter header1 = of(rawUnsignedBytes[0]);
        if (header1.equals(PARAMETER_COMMAND_HEADER_1)) {
            return header1;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Header1 {0} not as expected {1}", header1, PARAMETER_COMMAND_HEADER_1));
        }
    }

    public Parameter header2() {
        Parameter header2 = of(rawUnsignedBytes[1]);
        if (header2.equals(PARAMETER_COMMAND_HEADER_2)) {
            return header2;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Header2 {0} not as expected {1}", header2, PARAMETER_COMMAND_HEADER_2));
        }
    }

    public Parameter length() {
        Parameter givenLength = of(rawUnsignedBytes[2]);
        Parameter expectedLength = of(rawUnsignedBytes.length - Message.FIXED_PARTS);

        if (givenLength.equals(expectedLength)) {
            throw new IllegalArgumentException(MessageFormat.format("Length byte {0} not as expected {1}", givenLength, expectedLength));
        } else {
            return givenLength;
        }
    }

    public Parameter command() {
        return of(rawUnsignedBytes[3]);
    }

    public List<Parameter> parameters() {
        return IntStream.of(rawUnsignedBytes)
            .skip(4)
            .limit(rawUnsignedBytes.length - Message.FIXED_PARTS)
            .mapToObj(Parameter::of)
            .collect(toList());
    }

    public Parameter check() {
        List<Parameter> dataParameters = Stream.concat(Stream.of(length(), command()), parameters().stream()).collect(Collectors.toList());
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

    public MessageCheckSum checkSum() {
        return new MessageCheckSum(
            concat(
                of(length(), command()),
                parameters().stream())
                    .collect(toList()));
    }

    public Parameter endCharacter() {
        Parameter endCharacter = of(rawUnsignedBytes[rawUnsignedBytes.length - 1]);
        if (endCharacter.equals(PARAMETER_END_CHARACTER)) {
            return endCharacter;
        } else {
            throw new IllegalArgumentException(
                MessageFormat.format("End character {0} value not as expected {1}", endCharacter, PARAMETER_END_CHARACTER));
        }
    }

    public Message toMessage() {
        return new Message.Builder().builder();
    }
}
