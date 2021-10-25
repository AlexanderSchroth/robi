# Java API for UBTECH Alpha 1P
While playing around with an Alpha 1P robot I missed a API to control its movement from my favorite programming language java. This project implements the documented [Alpha 1 Series Bluetooth communication protocol](https://ubtrobot.com/pages/alpha-robot-downloads) using the java Bluetooth API [bluecove](http://www.bluecove.org/). I tested it under windows with a Bluetooth paired Alpha 1P

## usage
You can use this project to write your own great client. You need to know your Alpha Bluetooth address and do something like this:
```java
    public static void main(String[] args) throws Exception {
        try (Robot robot = new AlphaRobot(CommunicationFactory.create(ROBOT_ID))) {
            robot.handshake();
            robot.implementActionList("Move Leftward");
        }
    }
```
asdf
