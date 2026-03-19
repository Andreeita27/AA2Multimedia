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

public class InstructionsScreen implements Screen {

    private final MiJuego game;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private int selectedOption;

    public InstructionsScreen(MiJuego game) {
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

        font.getData().setScale(1.8f);
        font.draw(batch, "CÓMO JUGAR", 250, 360);

        font.getData().setScale(1f);

        font.draw(batch, "Mueve al personaje con las flechas izquierda y derecha para llegar a la meta.", 120, 300);
        font.draw(batch, "Salta con la barra espaciadora.", 120, 270);
        font.draw(batch, "Recoge gemas: +10 puntos.", 120, 240);
        font.draw(batch, "Evita enemigos o salta sobre los ratones para eliminarlos y consigue +25 puntos.", 120, 210);

        font.getData().setScale(1.2f);

        drawOption(
            (selectedOption == 0 ? "> " : "  ") + "Volver al menú principal",
            220,
            150
        );

        font.getData().setScale(0.9f);
        font.draw(batch, "Usa ARRIBA/ABAJO y pulsa ENTER", 235, 90);

        batch.end();
    }

    private void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
