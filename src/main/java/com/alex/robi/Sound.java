package com.alex.robi;

public interface Sound {

    void adjustVolume(Volume volume);

    void stop();

    void mute();

    void umute();

    void pause();

    void continuePlay();

    public static enum Volume {

        V_100(255), V_50(127), V_0(0);

        private int value;

        Volume(int value) {
            this.value = value;
        }
    }
}
