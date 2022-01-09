package com.robi.alpha1p.api;

import com.robi.alpha1p.api.communication.Communication;
import com.robi.alpha1p.api.function.AlphaRobot;
import com.robi.alpha1p.api.function.Offset;
import com.robi.alpha1p.api.function.Robot;
import com.robi.alpha1p.api.function.Servo;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AdjustServoClient {

    private static String ROBOT = "881B9912037D";

    public static void main(String[] args) throws IOException {
        Communication communication = CommunicationFactory.create(ROBOT);

        Robot robot = new AlphaRobot(communication);
        robot.handshake();

        Console console = new Console(robot);
        console.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                communication.close();
            }
        });
    }

    private static final class Console extends JFrame {

        private static final Map<Integer, Servo> SERVO_MAPPING;
        private JLabel servoLabel;
        private JLabel valueLabel;
        static {
            SERVO_MAPPING = new HashMap<Integer, Servo>();
            SERVO_MAPPING.put(KeyEvent.VK_1, Servo.LEFT_SHOULDER);
            SERVO_MAPPING.put(KeyEvent.VK_2, Servo.LEFT_ELBOW);
            SERVO_MAPPING.put(KeyEvent.VK_3, Servo.LEFT_ARM);
            SERVO_MAPPING.put(KeyEvent.VK_4, Servo.RIGHT_SHOULDER);
            SERVO_MAPPING.put(KeyEvent.VK_5, Servo.RIGHT_ELBOW);
            SERVO_MAPPING.put(KeyEvent.VK_6, Servo.RIGHT_ARM);
        }

        public Console(Robot robot) {
            getContentPane().setFocusable(true);
            getContentPane().addKeyListener(new KeyAdapter() {

                private Servo currentServo;

                public void keyPressed(KeyEvent e) {
                    try {
                        if (SERVO_MAPPING.containsKey(e.getKeyCode())) {
                            currentServo = SERVO_MAPPING.get(e.getKeyCode());
                            servoLabel.setText("Selected servo: " + currentServo);
                            Offset o = robot.readOffset(currentServo);
                            valueLabel.setText(o.toString());
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PLUS) {
                            Offset delta = Offset.ZERO;
                            if (e.isShiftDown()) {
                                delta = delta.sum(10);
                            } else {
                                delta = delta.sum(1);
                            }
                            Offset o = robot.readOffset(currentServo);
                            Offset sum = o.sum(delta);
                            robot.setOffset(currentServo, sum);
                            valueLabel.setText(sum.toString());
                        }
                        if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                            Offset delta = Offset.ZERO;
                            if (e.isShiftDown()) {
                                delta = delta.sum(-10);
                            } else {
                                delta = delta.sum(-1);
                            }
                            Offset o = robot.readOffset(currentServo);
                            Offset sum = o.sum(delta);
                            robot.setOffset(currentServo, sum);
                            valueLabel.setText(sum.toString());
                        }
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            robot.implementActionList("default");
                        }

                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            });

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            servoLabel = new JLabel("");
            valueLabel = new JLabel("");
            getContentPane().setLayout(new FlowLayout());
            getContentPane().add(servoLabel);
            getContentPane().add(valueLabel);
            setSize(100, 100);
            setVisible(true);
        }
    }
}
