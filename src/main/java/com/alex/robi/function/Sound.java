package com.alex.robi.api;

public interface Sound {

    void adjustVolume(Volume volume);

    void stop();

    void mute();

    void umute();

    void pause();

    void continuePlay();
}
