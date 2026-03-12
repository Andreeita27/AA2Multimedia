package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.com.MiJuego;
import com.svalero.com.manager.ConfigurationManager;

public class ConfigurationScreen implements Screen {

    private final MiJuego game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    public ConfigurationScreen(MiJuego game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            ConfigurationManager.musicEnabled = !ConfigurationManager.musicEnabled;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            ConfigurationManager.soundEnabled = !ConfigurationManager.soundEnabled;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        Gdx.gl.glClearColor(0.06f, 0.06f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        font.getData().setScale(1.8f);
        font.draw(batch, "CONFIGURACIÓN", 230, 340);

        font.getData().setScale(1.1f);
        font.draw(batch, "1 - Música: " + (ConfigurationManager.musicEnabled ? "ON" : "OFF"), 220, 250);
        font.draw(batch, "2 - Sonido: " + (ConfigurationManager.soundEnabled ? "ON" : "OFF"), 220, 210);
        font.draw(batch, "ESC - Volver al menú", 220, 140);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
