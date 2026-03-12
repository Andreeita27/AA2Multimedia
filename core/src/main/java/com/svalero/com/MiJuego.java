package com.svalero.com;

import com.badlogic.gdx.Game;
import com.svalero.com.screen.MainMenuScreen;

public class MiJuego extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
