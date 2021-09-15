package com.alex.robi;

import com.alex.robi.communication.AlphaCommunication;
import com.alex.robi.communication.AlphaConnection;
import com.alex.robi.communication.Communication;
import com.alex.robi.function.AlphaRobot;
import com.alex.robi.function.Robot;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class TestClient {

    private static String ROBOT = "881B9912037D";

    private static void test(Robot movement) {

        System.out.println("---------------------");
        movement.obtainActionList();
        movement.implementActionList("Default");
        // administration.implementActionList("Default");
        System.out.println("---------------------");

        sleep();

        // movement.offset(Servo.LEFT_SHOULDER, new Offset(90));
        // movement.offset(Servo.LEFT_ARM, new Offset(90));
        //movement.move(Servo.LEFT_ARM, new Angle(), new Time());
        //movement.move(Servo.LEFT_SHOULDER, new Angle(), new Time());
    }

    public static void main(String[] args) throws IOException {

        StreamConnection open3 = (StreamConnection) Connector.open("btspp://" + ROBOT + ":6");
        OutputStream outputStream = open3.openOutputStream();
        InputStream inputStream = open3.openInputStream();

        AlphaConnection connection = new AlphaConnection(outputStream);
        Communication communication = new AlphaCommunication(connection, inputStream);

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
