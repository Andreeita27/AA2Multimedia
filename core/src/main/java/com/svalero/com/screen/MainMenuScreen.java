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

public class MainMenuScreen implements Screen {

    private final MiJuego game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private int selectedOption;

    public MainMenuScreen(MiJuego game) {
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

        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        font.getData().setScale(2f);
        font.draw(batch, "ADVENTURER PLATFORM", 170, 360);

        font.getData().setScale(1.2f);
        drawOption((selectedOption == 0 ? "> " : "  ") + "Jugar", 280, 270);
        drawOption((selectedOption == 1 ? "> " : "  ") + "Instrucciones", 280, 230);
        drawOption((selectedOption == 2 ? "> " : "  ") + "Configuración", 280, 190);
        drawOption((selectedOption == 3 ? "> " : "  ") + "Salir", 280, 150);

        font.getData().setScale(0.9f);
        font.draw(batch, "Usa ARRIBA/ABAJO y pulsa ENTER", 235, 95);

        batch.end();
    }

    private void handleInput() {
        int maxOption = 3;

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
                    game.setScreen(new GameScreen(game));
                    break;
                case 1:
                    game.setScreen(new InstructionsScreen(game));
                    break;
                case 2:
                    game.setScreen(new ConfigurationScreen(game));
                    break;
                case 3:
                    Gdx.app.exit();
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
