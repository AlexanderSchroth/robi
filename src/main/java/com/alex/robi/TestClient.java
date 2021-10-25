package com.alex.robi;

import com.alex.robi.function.AlphaRobot;
import com.alex.robi.function.Robot;

public class TestClient {

    private static String ROBOT_ID = "881B9912037D";

    public static void main(String[] args) throws Exception {
        try (Robot robot = new AlphaRobot(CommunicationFactory.create(ROBOT_ID))) {
            robot.handshake();
            robot.implementActionList("Move Leftward");
        }
    }
}
