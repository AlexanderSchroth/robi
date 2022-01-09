package com.robi.alpha1p.api;

import com.robi.alpha1p.api.function.AlphaRobot;
import com.robi.alpha1p.api.function.Offset;
import com.robi.alpha1p.api.function.Robot;
import com.robi.alpha1p.api.function.Servo;

public class TestClient {

    private static String ROBOT_ID = "881B9912037D";

    public static void main(String[] args) throws Exception {
        try (Robot robot = new AlphaRobot(CommunicationFactory.create(ROBOT_ID))) {
            robot.handshake();
            Offset readOffsetRightShoulder = robot.readOffset(Servo.RIGHT_SHOULDER);
            Offset readOffsetRightElbow = robot.readOffset(Servo.RIGHT_ELBOW);
            Offset readOffsetRightArm = robot.readOffset(Servo.RIGHT_ARM);

            Offset readOffsetLeftShoulder = robot.readOffset(Servo.LEFT_SHOULDER);
            Offset readOffsetLeftElbow = robot.readOffset(Servo.LEFT_ELBOW);
            Offset readOffsetLeftArm = robot.readOffset(Servo.LEFT_ARM);

            System.out.println("Right:");
            System.out.println(readOffsetRightShoulder);
            System.out.println(readOffsetRightElbow);
            System.out.println(readOffsetRightArm);
            System.out.println("Left:");
            System.out.println(readOffsetLeftShoulder);
            System.out.println(readOffsetLeftElbow);
            System.out.println(readOffsetLeftArm);

            robot.setOffset(Servo.LEFT_SHOULDER, new Offset(231));

            robot.implementActionList("default");
            Thread.sleep(2000);
        }
    }
}
