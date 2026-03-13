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

public class VictoryScreen implements Screen {

    private final MiJuego game;
    private final int finalScore;
    private final int currentLives;
    private final boolean hasNextLevel;
    private final int nextLevel;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private int selectedOption;

    public VictoryScreen(MiJuego game, int finalScore, int currentLives, boolean hasNextLevel, int nextLevel) {
        this.game = game;
        this.finalScore = finalScore;
        this.currentLives = currentLives;
        this.hasNextLevel = hasNextLevel;
        this.nextLevel = nextLevel;
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

        Gdx.gl.glClearColor(0f, 0.15f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        font.getData().setScale(2f);
        font.draw(batch, "¡NIVEL COMPLETADO!", 160, 320);

        font.getData().setScale(1.2f);
        font.draw(batch, "Puntuación actual: " + finalScore, 220, 260);
        font.draw(batch, "Vidas restantes: " + currentLives, 230, 230);

        if (hasNextLevel) {
            drawOption((selectedOption == 0 ? "> " : "  ") + "Siguiente nivel", 235, 180);
            drawOption((selectedOption == 1 ? "> " : "  ") + "Salir al menú principal", 185, 140);
        } else {
            font.draw(batch, "Has completado el juego", 220, 180);
            drawOption((selectedOption == 0 ? "> " : "  ") + "Salir al menú principal", 185, 130);
        }

        font.getData().setScale(0.9f);
        font.draw(batch, "Usa ARRIBA/ABAJO y pulsa ENTER", 205, 85);

        batch.end();
    }

    private void handleInput() {
        int maxOption = hasNextLevel ? 1 : 0;

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
            if (hasNextLevel) {
                if (selectedOption == 0) {
                    goToNextLevel();
                } else {
                    game.setScreen(new MainMenuScreen(game));
                }
            } else {
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    private void goToNextLevel() {
        if (nextLevel == 2) {
            game.setScreen(new GameScreenLevel2(game, finalScore, currentLives));
        } else {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void drawOption(String text, float x, float y) {
        font.getData().setScale(1.2f);
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
