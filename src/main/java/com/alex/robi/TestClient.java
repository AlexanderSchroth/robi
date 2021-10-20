package com.alex.robi;

import com.alex.robi.function.AlphaRobot;
import com.alex.robi.function.Offset;
import com.alex.robi.function.Robot;
import com.alex.robi.function.Servo;
import java.io.IOException;

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
        try (Robot movement = new AlphaRobot(CommunicationFactory.create(ROBOT))) {
            movement.handshake();
            sleep();
            test(movement);
        } catch (Exception e) {
            e.printStackTrace();
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
