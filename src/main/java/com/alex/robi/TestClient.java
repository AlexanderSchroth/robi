package com.alex.robi;

import com.alex.robi.communication.AlphaCommunication;
import com.alex.robi.communication.AlphaReciving;
import com.alex.robi.communication.AlphaSending;
import com.alex.robi.communication.Communication;
import com.alex.robi.function.AlphaRobot;
import com.alex.robi.function.Offset;
import com.alex.robi.function.Robot;
import com.alex.robi.function.Servo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class TestClient {

    private static String ROBOT = "881B9912037D";

    private static void test(Robot robot) {

        System.out.println("---------------------");
        Offset readOffsetLeft = robot.readOffset(Servo.LEFT_ELBOW);
        System.out.println(readOffsetLeft);


        Offset readOffsetRight = robot.readOffset(Servo.RIGHT_ELBOW);
        System.out.println(readOffsetRight);
        robot.setOffset(Servo.LEFT_ELBOW, readOffsetRight);

        // robot.setOffset(Servo.LEFT_ARM, new Offset(20, Sign.Zero));

        sleep();
    }

    public static void main(String[] args) throws IOException {

        StreamConnection open3 = (StreamConnection) Connector.open("btspp://" + ROBOT + ":6");
        OutputStream outputStream = open3.openOutputStream();
        InputStream inputStream = open3.openInputStream();

        AlphaReciving responseReader = new AlphaReciving(inputStream);
        AlphaSending connection = new AlphaSending(outputStream);
        Communication communication = new AlphaCommunication(connection, responseReader);
        communication.open();

        Robot movement = new AlphaRobot(communication);

        try {
            movement.handshake();
            sleep();
            test(movement);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            communication.close();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
