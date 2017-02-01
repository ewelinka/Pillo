package uy.aguita.pillo.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by ewe on 1/15/17.
 */
public class AudioManager {
    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;
    private float defaultVol = 0.5f;
    // singleton: prevent instantiation from other classes
    private AudioManager () { }

    public void play (Sound sound) {
        play(sound, 1);
    }
    public void play (Sound sound, float volume) {
        play(sound, volume, 1);
    }
    public void play (Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }
    public void play (Sound sound, float volume, float pitch, float pan) {
        sound.play(defaultVol* volume, pitch, pan);
    }

    public void play (Music music) {
        stopMusic();
        playingMusic = music;

        music.setLooping(true);
        music.setVolume(defaultVol);
        music.play();

    }
    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

}