package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static Sound jumpSound;
    private static Sound gemSound;
    private static Sound hitSound;
    private static Sound stompSound;
    private static Sound winSound;
    private static Sound loseSound;

    private static Music backgroundMusic;

    private SoundManager() {
    }

    public static void load() {
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
        gemSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gem.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
        stompSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stomp.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/lose.wav"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.wav"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.4f);
    }

    public static void playJump() {
        if (ConfigurationManager.soundEnabled && jumpSound != null) {
            jumpSound.play(0.8f);
        }
    }

    public static void playGem() {
        if (ConfigurationManager.soundEnabled && gemSound != null) {
            gemSound.play(0.8f);
        }
    }

    public static void playHit() {
        if (ConfigurationManager.soundEnabled && hitSound != null) {
            hitSound.play(0.8f);
        }
    }

    public static void playStomp() {
        if (ConfigurationManager.soundEnabled && stompSound != null) {
            stompSound.play(0.8f);
        }
    }

    public static void playWin() {
        if (ConfigurationManager.soundEnabled && winSound != null) {
            winSound.play(0.9f);
        }
    }

    public static void playLose() {
        if (ConfigurationManager.soundEnabled && loseSound != null) {
            loseSound.play(0.9f);
        }
    }

    public static void updateMusic() {
        if (backgroundMusic == null) {
            return;
        }

        if (ConfigurationManager.musicEnabled) {
            if (!backgroundMusic.isPlaying()) {
                backgroundMusic.play();
            }
        } else {
            backgroundMusic.pause();
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public static void dispose() {
        if (jumpSound != null) jumpSound.dispose();
        if (gemSound != null) gemSound.dispose();
        if (hitSound != null) hitSound.dispose();
        if (stompSound != null) stompSound.dispose();
        if (winSound != null) winSound.dispose();
        if (loseSound != null) loseSound.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
