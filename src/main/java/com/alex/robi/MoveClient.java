package com.alex.robi;

import com.alex.robi.communication.Communication;
import com.alex.robi.function.AlphaRobot;
import com.alex.robi.function.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MoveClient {

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

        private static final Map<Integer, String> ACTIONS;
        static {
            ACTIONS = new HashMap<Integer, String>();
            ACTIONS.put(KeyEvent.VK_SPACE, "Default");
            ACTIONS.put(KeyEvent.VK_A, "Move Leftward");
            ACTIONS.put(KeyEvent.VK_D, "Move Rightward");
            ACTIONS.put(KeyEvent.VK_S, "Move back");
            ACTIONS.put(KeyEvent.VK_W, "Move forward");
            ACTIONS.put(KeyEvent.VK_Q, "Turn Left");
            ACTIONS.put(KeyEvent.VK_E, "Turn Right");
        }

        public Console(Robot robot) {
            getContentPane().setFocusable(true);
            getContentPane().addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    try {
                        if (!ACTIONS.containsKey(e.getKeyCode())) {
                            System.out.println("Key <" + e.getKeyCode() + "> not bound to action");
                            return;
                        }
                        String name = ACTIONS.get(e.getKeyCode());
                        robot.implementActionList(name);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            });

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().add(new JLabel("Use ASDW to move robot"));
            setSize(100, 100);
            setVisible(true);
        }
    }
}
