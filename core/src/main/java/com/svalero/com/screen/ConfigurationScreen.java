package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.com.MiJuego;
import com.svalero.com.manager.ConfigurationManager;

public class ConfigurationScreen implements Screen {

    // Referencia al juego principal para poder cambiar de pantalla
    private final MiJuego game;
    // Elementos necesarios para dibujar texto en pantalla
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private GlyphLayout layout;

    // indice de la opcion seleccionada en el menu
    private int selectedOption;

    public ConfigurationScreen(MiJuego game) {
        this.game = game;
    }

    // Se ejecuta al entrar en esta pantalla
    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        layout = new GlyphLayout();

        selectedOption = 0;
    }

    // Se eejcuta constantemente mientras esta pantalla esta activa
    @Override
    public void render(float delta) {
        handleInput();

        // Limpia la pantalla y pinta el fondo
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float titleY = screenHeight / 2f + 130;
        float firstOptionY = screenHeight / 2f + 30;
        float optionSpacing = 45f;
        float helpY = screenHeight / 2f - 140;

        // Titulo
        font.getData().setScale(1.8f);
        drawCenteredText("CONFIGURACIÓN", screenWidth, titleY);

        // Opcion para activar/desactivar musica
        font.getData().setScale(1.1f);
        drawOption(
            (selectedOption == 0 ? "> " : "  ") + "Música: " +
                (ConfigurationManager.musicEnabled ? "ON" : "OFF"),
            screenWidth,
            firstOptionY
        );

        // Opcion para activar/desactivar sonidos
        drawOption(
            (selectedOption == 1 ? "> " : "  ") + "Sonido: " +
                (ConfigurationManager.soundEnabled ? "ON" : "OFF"),
            screenWidth,
            firstOptionY - optionSpacing
        );

        // Opcion para volver al menu principal
        drawOption(
            (selectedOption == 2 ? "> " : "  ") + "Volver al menú principal",
            screenWidth,
            firstOptionY - optionSpacing * 2
        );

        // Texto de ayuda
        font.getData().setScale(0.9f);
        drawCenteredText("Usa ARRIBA/ABAJO y pulsa ENTER", screenWidth, helpY);

        batch.end();
    }

    // Gestiona la navegacion por teclado dentro del menu
    private void handleInput() {
        int maxOption = 2;

        // Mover seleccion arriba
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption--;
            // Si pasa de la primera opcion, vuelve a la ultima
            if (selectedOption < 0) {
                selectedOption = maxOption;
            }
        }

        // Mover seleccion abajo
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption++;
            // Si pasa de la ultima, vuelve a la primera
            if (selectedOption > maxOption) {
                selectedOption = 0;
            }
        }

        // Ejecuta la opcion seleccionada
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

    // Opcion centrada horizontalmente
    private void drawOption(String text, float screenWidth, float y) {
        layout.setText(font, text);
        font.draw(batch, text, (screenWidth - layout.width) / 2f, y);
    }

    // Texto centrado horizontalmente
    private void drawCenteredText(String text, float screenWidth, float y) {
        layout.setText(font, text);
        font.draw(batch, text, (screenWidth - layout.width) / 2f, y);
    }

    // Ajusta la camara si cambia el tamaño de la pantalla
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

    // Libera recursos
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
