package com.alex.robi.communication;

import static com.alex.robi.communication.RobiByte.robiByte;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

import java.util.List;
import java.util.stream.IntStream;

public class MessageMask {

    private int[] rawUnsignedBytes;

    public MessageMask(int[] message) {
        this.rawUnsignedBytes = message;
    }

    public RobiByte header1() {
        return robiByte(rawUnsignedBytes[0]);
    }

    public RobiByte header2() {
        return robiByte(rawUnsignedBytes[1]);
    }

    public RobiByte length() {
        return robiByte(rawUnsignedBytes[2]);
    }

    public RobiByte command() {
        return robiByte(rawUnsignedBytes[3]);
    }

    public List<RobiByte> parameters() {
        return IntStream.of(rawUnsignedBytes)
            .skip(4)
            .limit(rawUnsignedBytes.length - 2)
            .mapToObj(RobiByte::robiByte)
            .collect(toList());
    }

    public RobiByte check() {
        return robiByte(rawUnsignedBytes[rawUnsignedBytes.length - 2]);
    }

    public MessageCheckSum checkSum() {
        return new MessageCheckSum(
            concat(
                of(length(), command()),
                parameters().stream())
                    .collect(toList()));
    }

    public RobiByte endCharacter() {
        return robiByte(rawUnsignedBytes[rawUnsignedBytes.length - 1]);
    }

    public Message toMessage() {
        return new Message.Builder().builder();
    }
}
