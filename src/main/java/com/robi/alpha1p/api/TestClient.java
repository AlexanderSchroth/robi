package com.robi.alpha1p.api;

import com.robi.alpha1p.api.function.AlphaRobot;
import com.robi.alpha1p.api.function.Robot;

public class TestClient {

    private static String ROBOT_ID = "881B9912037D";

    public static void main(String[] args) throws Exception {
        try (Robot robot = new AlphaRobot(CommunicationFactory.create(ROBOT_ID))) {
            robot.handshake();
            robot.implementActionList("Move Leftward");
        }
    }
}
