package com.alex.robi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Connect {

    private static String ROBOT = "881B9912037D";

    public static void main(String[] args) throws Exception {

        // String selectService = LocalDevice.getLocalDevice().getDiscoveryAgent().selectService(new UUID(ROBOT, false), ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
        // false);
        // System.out.println("Selected service: <" + selectService + ">");
        StreamConnection open3 = (StreamConnection) Connector.open("btspp://" + ROBOT + ":6");

        // DataOutputStream openDataOutputStream = open3.openDataOutputStream();
        // DataInputStream openDataInputStream = open3.openDataInputStream();

        OutputStream dataOutputStream = open3.openOutputStream();
        InputStream dataInputStream = open3.openInputStream();

        ResponseReader target = new ResponseReader(dataInputStream);
        new Thread(target).start();

        Thread.sleep(1000);
        BT_HANDSHAKE.send(dataOutputStream);

        Thread.sleep(1000);
        VERSION_COMMAND.send(dataOutputStream);

        Thread.sleep(10000);

        target.stop();
        open3.close();
        // open4.close();
    }

    private static PacketSend VERSION_COMMAND = new PacketSend(0x0A, Arrays.asList(0x00));
    private static PacketSend BT_HANDSHAKE = new PacketSend(0x01, Arrays.asList(0x00));

    private static class PacketSend {
        private static final int COMMAND_HEADER_1 = 0xFB;
        private static final int COMMAND_HEADER_2 = 0xBF;

        private Integer command;
        private List<Integer> paramters;

        PacketSend(Integer command, List<Integer> parameters) {
            this.command = command;
            this.paramters = parameters;
        }

        private static final int END_CHARACTER = 0xED;

        void send(OutputStream out) throws IOException {
            List<Integer> message = new ArrayList<>();
            message.add(COMMAND_HEADER_1);
            message.add(COMMAND_HEADER_2);
            message.add(length());
            message.add(command);
            for (Integer parameter : paramters) {
                message.add(parameter);
            }
            message.add(check());
            message.add(END_CHARACTER);

            byte[] b = new byte[message.size()];
            for (int i = 0; i < message.size(); i++) {
                b[i] = message.get(i).byteValue();
            }
            System.out.println("send " + message.stream().map(i -> Integer.toUnsignedString(i)).collect(Collectors.joining("-")));

            for (Integer i : message) {
                out.write(i.intValue());
            }

            out.flush();
        }

        private int paramSum() {
            int sum = 0;
            for (Integer param : paramters) {
                sum += param.intValue();
            }
            return sum;
        }

        private int check() {
            int checkSum = length() + command + paramSum();
            byte[] bytes = ByteBuffer.allocate(3 + paramters.size()).putInt(checkSum).array();

            return Byte.toUnsignedInt(bytes[3]);
        }

        private Integer length() {
            return 5 + paramters.size();
        }
    }
}
