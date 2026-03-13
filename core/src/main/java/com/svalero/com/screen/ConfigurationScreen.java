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

    private int selectedOption;

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

        selectedOption = 0;
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.06f, 0.06f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        font.getData().setScale(1.8f);
        font.draw(batch, "CONFIGURACIÓN", 230, 340);

        font.getData().setScale(1.1f);
        drawOption(
            (selectedOption == 0 ? "> " : "  ") + "Música: " +
                (ConfigurationManager.musicEnabled ? "ON" : "OFF"),
            220, 250
        );

        drawOption(
            (selectedOption == 1 ? "> " : "  ") + "Sonido: " +
                (ConfigurationManager.soundEnabled ? "ON" : "OFF"),
            220, 210
        );

        drawOption(
            (selectedOption == 2 ? "> " : "  ") + "Volver al menú principal",
            220, 170
        );

        font.getData().setScale(0.9f);
        font.draw(batch, "Usa ARRIBA/ABAJO y pulsa ENTER", 210, 110);
        font.draw(batch, "Pulsa ESC para volver al menú", 225, 80);

        batch.end();
    }

    private void handleInput() {
        int maxOption = 2;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption--;
            if (selectedOption < 0) {
                selectedOption = maxOption;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption++;
            if (selectedOption > maxOption) {
                selectedOption = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedOption) {
                case 0:
                    ConfigurationManager.musicEnabled = !ConfigurationManager.musicEnabled;
                    break;
                case 1:
                    ConfigurationManager.soundEnabled = !ConfigurationManager.soundEnabled;
                    break;
                case 2:
                    game.setScreen(new MainMenuScreen(game));
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void drawOption(String text, float x, float y) {
        font.draw(batch, text, x, y);
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
