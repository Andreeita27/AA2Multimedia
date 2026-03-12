package com.svalero.com;

import com.badlogic.gdx.Game;
import com.svalero.com.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MiJuego extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
