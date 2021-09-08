package com.alex.robi;

public interface Movement {

    public static enum Servo {

        LEFT_SHOULDER(1), LEFT_ARM(2), LEFT_ELBOW(3), RIGHT_SHOULDER(4), RIGHT_ARM(5), RIGHT_ELBOW(6);

        private int value;

        Servo(int value) {
            this.value = value;
        }

        Parameter asParameter() {
            return Parameter.of(value);
        }
    }

    void move(Servo s, Angle angle, Time time);

    void offset(Servo servo, Offset offset);
}
