package com.alex.robi;

import com.alex.robi.api.AlphaRobot;
import com.alex.robi.api.Offset;
import com.alex.robi.api.Robot;
import com.alex.robi.api.Servo;
import com.alex.robi.communication.AlphaCommunication;
import com.alex.robi.communication.AlphaConnection;
import com.alex.robi.communication.Communication;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JFrame;

public class TestClient {

    private static String ROBOT = "881B9912037D";

    private static void test(Robot movement) {

        System.out.println("---------------------");
        movement.obtainActionList();
        // administration.implementActionList("Default");
        System.out.println("---------------------");

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

        sleep();
        //
        // RobotState state = administration.state();
        // System.out.println(state);
        //
        //
        // Offset readOffset1 = movement.readOffset(Servo.LEFT_ARM);
        // System.out.println(readOffset1);
        //
        // movement.move(Servo.RIGHT_ARM, new Angle(), new Time());
        // movement.move(Servo.LEFT_ARM, new Angle(), new Time());
        //
        // Offset readOffset2 = movement.readOffset(Servo.LEFT_ARM);
        // System.out.println(readOffset2);
        //

        //
        // sleep();
        // movement.move(Servo.LEFT_SHOULDER, new Offset(), new Time());
        // sleep();
        // movement.move(Servo.LEFT_ARM, new Angle(), new Time());
        // sleep();
        // movement.move(Servo.LEFT_ELBOW, new Angle(), new Time());
        // sleep();
        // movement.move(Servo.RIGHT_SHOULDER, new Angle(), new Time());
        // sleep();
        // movement.move(Servo.RIGHT_ARM, new Angle(), new Time());
        // sleep();
        // movement.move(Servo.RIGHT_ELBOW, new Angle(), new Time());

        // Console console = new Console(movement);
        // console.addWindowListener(new WindowAdapter() {
        // @Override
        // public void windowClosed(WindowEvent event) {
        // communication.close();
        // }
        // });
    }

    private static final class Console extends JFrame {

        private Robot movement;

        public Console(Robot movement) {
            this.movement = movement;

            getContentPane().setFocusable(true);
            getContentPane().addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    movement.offset(Servo.LEFT_ELBOW, new Offset(1));
                }
            });

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(100, 100);
            setVisible(true);
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
