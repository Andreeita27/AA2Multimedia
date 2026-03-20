package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.MiJuego;
import com.svalero.com.domain.Score;
import com.svalero.com.manager.ScoreManager;

public class VictoryScreen implements Screen {

    private final MiJuego game;
    private final int finalScore;
    private final int currentLives;
    private final boolean hasNextLevel;
    private final int nextLevel;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Texture pixel;

    private int selectedOption;

    private boolean enteringName;
    private String playerName;
    private Array<Score> topScores;

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

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        selectedOption = 0;

        enteringName = !hasNextLevel;
        playerName = "";
        topScores = ScoreManager.loadScores();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.04f, 0.07f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float panelX = 95;
        float panelY = 55;
        float panelWidth = screenWidth - 190;
        float panelHeight = screenHeight - 110;

        drawRect(panelX, panelY, panelWidth, panelHeight, new Color(0f, 0f, 0f, 0.35f));
        drawBorder(panelX, panelY, panelWidth, panelHeight, new Color(0.75f, 0.65f, 0.25f, 0.9f));

        // TÍTULO
        font.setColor(new Color(1f, 0.9f, 0.35f, 1f));
        font.getData().setScale(2.2f);
        font.draw(batch, "¡NIVEL COMPLETADO!", panelX + 95, panelY + panelHeight - 45);

        if (hasNextLevel) {
            font.setColor(Color.WHITE);
            font.getData().setScale(1.15f);
            font.draw(batch, "Puntuación actual: " + finalScore, panelX + 145, panelY + panelHeight - 105);
            font.draw(batch, "Vidas restantes: " + currentLives, panelX + 150, panelY + panelHeight - 140);

            font.getData().setScale(1.05f);
            font.setColor(new Color(0.9f, 0.95f, 0.9f, 1f));
            font.draw(batch, "Nivel superado. ¿Qué quieres hacer ahora?", panelX + 110, panelY + panelHeight - 195);

            drawMenuOption((selectedOption == 0 ? "> " : "  ") + "Siguiente nivel",
                panelX + 150, panelY + panelHeight - 255, selectedOption == 0);

            drawMenuOption((selectedOption == 1 ? "> " : "  ") + "Salir al menú principal",
                panelX + 150, panelY + panelHeight - 300, selectedOption == 1);

            font.getData().setScale(0.9f);
            font.setColor(new Color(0.8f, 0.85f, 0.8f, 1f));
            font.draw(batch, "Usa ARRIBA/ABAJO y pulsa ENTER", panelX + 120, panelY + 35);

        } else if (enteringName) {
            font.setColor(Color.WHITE);
            font.getData().setScale(1.15f);
            font.draw(batch, "Puntuación final: " + finalScore, panelX + 155, panelY + panelHeight - 115);
            font.draw(batch, "Vidas restantes: " + currentLives, panelX + 160, panelY + panelHeight - 150);

            font.getData().setScale(1.05f);
            font.setColor(new Color(0.9f, 0.95f, 0.9f, 1f));
            font.draw(batch, "Has completado el juego", panelX + 155, panelY + panelHeight - 205);

            font.getData().setScale(1f);
            font.setColor(Color.WHITE);
            font.draw(batch, "Introduce tu nombre:", panelX + 145, panelY + panelHeight - 255);

            drawRect(panelX + 135, panelY + panelHeight - 315, 300, 42, new Color(1f, 1f, 1f, 0.08f));
            drawBorder(panelX + 135, panelY + panelHeight - 315, 300, 42, new Color(0.75f, 0.65f, 0.25f, 0.9f));

            font.getData().setScale(1.1f);
            font.setColor(new Color(1f, 0.95f, 0.75f, 1f));
            font.draw(batch, playerName + "_", panelX + 150, panelY + panelHeight - 286);

            font.getData().setScale(0.9f);
            font.setColor(new Color(0.8f, 0.85f, 0.8f, 1f));
            font.draw(batch, "Pulsa ENTER para guardar tu puntuación", panelX + 100, panelY + 35);

        } else {
            // PANTALLA FINAL CON RANKING
            int visibleScores = Math.min(topScores.size, 5);

            float rankingX = panelX + 90;
            float rankingY = panelY + 70;
            float rankingWidth = panelWidth - 180;
            float rowHeight = 24f;
            float rankingHeight = 80 + visibleScores * rowHeight;

            drawRect(rankingX, rankingY, rankingWidth, rankingHeight, new Color(1f, 1f, 1f, 0.06f));
            drawBorder(rankingX, rankingY, rankingWidth, rankingHeight, new Color(0.75f, 0.65f, 0.25f, 0.8f));

            font.getData().setScale(1.15f);
            font.setColor(new Color(1f, 0.9f, 0.35f, 1f));
            font.draw(batch, "TOP 10 PUNTUACIONES", rankingX + 35, rankingY + rankingHeight - 18);

            font.getData().setScale(0.95f);
            font.setColor(new Color(0.85f, 0.85f, 0.85f, 1f));
            font.draw(batch, "Jugador", rankingX + 30, rankingY + rankingHeight - 48);
            font.draw(batch, "Puntos", rankingX + rankingWidth - 95, rankingY + rankingHeight - 48);

            float lineY = rankingY + rankingHeight - 78;

            for (int i = 0; i < visibleScores; i++) {
                Score entry = topScores.get(i);

                font.setColor(Color.WHITE);
                font.draw(batch, (i + 1) + ". " + entry.getName(), rankingX + 30, lineY);

                font.setColor(new Color(1f, 0.92f, 0.45f, 1f));
                font.draw(batch, String.valueOf(entry.getScore()), rankingX + rankingWidth - 85, lineY);

                lineY -= rowHeight;
            }

            drawMenuOption((selectedOption == 0 ? "> " : "  ") + "Salir al menú principal",
                panelX + 135, panelY + 30, true);
        }

        batch.end();
    }

    private void handleInput() {
        if (hasNextLevel) {
            int maxOption = 1;

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
                if (selectedOption == 0) {
                    goToNextLevel();
                } else {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
            return;
        }

        if (enteringName) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && playerName.length() > 0) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }

            for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
                if (Gdx.input.isKeyJustPressed(key) && playerName.length() < 12) {
                    playerName += (char) ('A' + (key - Input.Keys.A));
                }
            }

            for (int i = 0; i <= 9; i++) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0 + i) && playerName.length() < 12) {
                    playerName += i;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerName.length() < 12) {
                playerName += " ";
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                ScoreManager.saveScore(playerName, finalScore);
                topScores = ScoreManager.loadScores();
                enteringName = false;
                selectedOption = 0;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void goToNextLevel() {
        if (nextLevel == 2) {
            game.setScreen(new GameScreenLevel2(game, finalScore, currentLives));
        } else {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void drawMenuOption(String text, float x, float y, boolean selected) {
        font.getData().setScale(1.15f);

        if (selected) {
            font.setColor(new Color(1f, 0.92f, 0.45f, 1f));
        } else {
            font.setColor(new Color(0.9f, 0.9f, 0.9f, 1f));
        }

        font.draw(batch, text, x, y);
    }

    private void drawRect(float x, float y, float width, float height, Color color) {
        Color old = batch.getColor().cpy();
        batch.setColor(color);
        batch.draw(pixel, x, y, width, height);
        batch.setColor(old);
    }

    private void drawBorder(float x, float y, float width, float height, Color color) {
        drawRect(x, y, width, 2, color);
        drawRect(x, y + height - 2, width, 2, color);
        drawRect(x, y, 2, height, color);
        drawRect(x + width - 2, y, 2, height, color);
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
        pixel.dispose();
    }
}
