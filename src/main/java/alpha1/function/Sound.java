package alpha1.function;

public interface Sound {

    void adjustVolume(Volume volume);

    void stop();

    void mute();

    void umute();

    void pause();

    void continuePlay();
}
