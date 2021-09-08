package com.alex.robi;

import com.alex.robi.Movement.Servo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JFrame;

public class Alex {

    private static String ROBOT = "881B9912037D";

    public static void main(String[] args) throws IOException {

        StreamConnection open3 = (StreamConnection) Connector.open("btspp://" + ROBOT + ":6");
        OutputStream outputStream = open3.openOutputStream();
        InputStream inputStream = open3.openInputStream();

        AlphaConnection connection = new AlphaConnection(outputStream);
        Communication communication = new AlphaCommunication(connection);

        Movement movement = new AlphaMovement(communication);
        Adminstration administration = new AlphaAdministration(communication);

        ResponseReader target = new ResponseReader(inputStream);
        new Thread(target).start();

        // sleep();
        administration.handshake();

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

        Console console = new Console(movement);
        console.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                try {
                    target.stop();
                    outputStream.close();
                    inputStream.close();
                    open3.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static final class Console extends JFrame {

        private Movement movement;

        public Console(Movement movement) {
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
                    movement.offset(Servo.LEFT_ELBOW, new Offset());
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
