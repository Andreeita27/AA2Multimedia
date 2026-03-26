package com.svalero.com;

import com.badlogic.gdx.Game;
import com.svalero.com.manager.SoundManager;
import com.svalero.com.screen.MainMenuScreen;

public class MiJuego extends Game {

    @Override
    public void create() {
        SoundManager.load();
        SoundManager.updateMusic();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        SoundManager.updateMusic();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        SoundManager.dispose();
    }
}
