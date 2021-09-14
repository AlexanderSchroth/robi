package com.alex.robi.function;

public class RobotState {

    private SoundState soundState;
    private PlayState playState;
    private Volume volume;
    private ServoIndicateState servoIndicateState;
    private TfCardInsertion tfCardInsertion;

    public RobotState(SoundState soundState, PlayState playState, Volume volume, ServoIndicateState servoIndicateState, TfCardInsertion tfCardInsertion) {
        this.soundState = soundState;
        this.playState = playState;
        this.volume = volume;
        this.servoIndicateState = servoIndicateState;
        this.tfCardInsertion = tfCardInsertion;
    }

    public TfCardInsertion tfCardInsertion() {
        return tfCardInsertion;
    }

    public PlayState playState() {
        return playState;
    }

    public SoundState soundState() {
        return soundState;
    }

    public Volume volume() {
        return volume;
    }

    public ServoIndicateState servoIndicateState() {
        return servoIndicateState;
    }
}
