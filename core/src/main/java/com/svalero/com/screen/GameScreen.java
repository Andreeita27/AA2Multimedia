package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.MiJuego;
import com.svalero.com.domain.Collectible;
import com.svalero.com.domain.Enemy;
import com.svalero.com.domain.LevelExit;
import com.svalero.com.domain.Platform;
import com.svalero.com.manager.ConfigurationManager;
import com.svalero.com.manager.LevelManager;
import com.svalero.com.manager.LogicManager;
import com.svalero.com.manager.RenderManager;
import com.svalero.com.manager.ResourceManager;
import com.svalero.com.manager.SoundManager;
import com.svalero.com.ui.HudRenderer;
import com.svalero.com.util.Constants;

public class GameScreen implements Screen {

    private final MiJuego game;
    private final int levelNumber;
    private final int initialScore;
    private final int initialLives;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private BitmapFont font;
    private GlyphLayout layout;
    private ShapeRenderer shapeRenderer;

    private Texture background;
    private Texture ground;
    private Texture platformTexture;
    private Texture gemTexture;
    private Texture exitTexture;

    private Array<Platform> platforms;
    private Array<Collectible> collectibles;
    private Array<Enemy> enemies;
    private LevelExit levelExit;
    private float worldWidth;

    private int totalGems;
    private float stateTime;

    private boolean paused;
    private int selectedPauseOption;

    private HudRenderer hudRenderer;
    private LevelManager levelManager;
    private ResourceManager resourceManager;
    private LogicManager logicManager;
    private RenderManager renderManager;

    public GameScreen(MiJuego game) {
        this(game, 1, 0, Constants.INITIAL_LIVES);
    }

    public GameScreen(MiJuego game, int levelNumber, int initialScore, int initialLives) {
        this.game = game;
        this.levelNumber = levelNumber;
        this.initialScore = initialScore;
        this.initialLives = initialLives;
    }

    @Override
    public void show() {
        SoundManager.resumeMusic();

        hudRenderer = new HudRenderer();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();

        resourceManager = new ResourceManager();
        resourceManager.loadPlayerResources();
        stateTime = 0f;

        paused = false;
        selectedPauseOption = 0;

        levelManager = new LevelManager(levelNumber);
        levelManager.loadLevel();

        background = levelManager.getBackground();
        ground = levelManager.getGround();
        platformTexture = levelManager.getPlatformTexture();
        gemTexture = levelManager.getGemTexture();
        exitTexture = levelManager.getExitTexture();

        resourceManager.loadEnemyResources(levelManager.getEnemyTexture());

        platforms = levelManager.getPlatforms();
        collectibles = levelManager.getCollectibles();
        enemies = levelManager.getEnemies();
        levelExit = levelManager.getLevelExit();
        totalGems = levelManager.getTotalGems();

        worldWidth = levelManager.getWorldWidth();

        logicManager = new LogicManager(
            camera,
            platforms,
            collectibles,
            enemies,
            levelExit,
            initialScore,
            initialLives,
            worldWidth,
            new LogicManager.LogicCallbacks() {
                @Override
                public void onGameOver() {
                    game.setScreen(new GameOverScreen(game));
                }

                @Override
                public void onLevelCompleted(int score, int lives) {
                    int maxLevel = 4;

                    if (levelNumber < maxLevel) {
                        game.setScreen(new GameScreen(game, levelNumber + 1, score, lives));
                    } else {
                        game.setScreen(new VictoryScreen(game, score, lives));
                    }
                }
            }
        );

        renderManager = new RenderManager(batch, hudRenderer, resourceManager);
    }

    @Override
    public void render(float delta) {
        handlePauseToggle();

        if (!paused) {
            stateTime += delta;
            logicManager.update(delta);
        } else {
            handlePauseMenuInput();
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        renderManager.render(
            stateTime,
            logicManager,
            background,
            ground,
            platforms,
            collectibles,
            enemies,
            levelExit,
            totalGems,
            levelNumber,
            worldWidth
        );

        batch.end();

        if (paused) {
            drawPauseOverlay();
            drawPauseMenu();
        }
    }

    private void handlePauseToggle() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
            if (paused) {
                selectedPauseOption = 0;
            }
        }
    }

    private void handlePauseMenuInput() {
        int maxOption = 4;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedPauseOption--;
            if (selectedPauseOption < 0) {
                selectedPauseOption = maxOption;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedPauseOption++;
            if (selectedPauseOption > maxOption) {
                selectedPauseOption = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedPauseOption) {
                case 0:
                    paused = false;
                    break;
                case 1:
                    ConfigurationManager.musicEnabled = !ConfigurationManager.musicEnabled;
                    SoundManager.updateMusic();
                    break;
                case 2:
                    ConfigurationManager.soundEnabled = !ConfigurationManager.soundEnabled;
                    break;
                case 3:
                    game.setScreen(new MainMenuScreen(game));
                    break;
                case 4:
                    Gdx.app.exit();
                    break;
            }
        }
    }

    private void drawPauseOverlay() {
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.60f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
    }

    private void drawPauseMenu() {
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float titleY = screenHeight / 2f + 120f;
        float firstOptionY = screenHeight / 2f + 40f;
        float optionSpacing = 42f;
        float helpY = screenHeight / 2f - 180f;

        font.getData().setScale(1.8f);
        drawCenteredText("JUEGO EN PAUSA", screenWidth, titleY);

        font.getData().setScale(1.15f);
        drawCenteredText((selectedPauseOption == 0 ? "> " : "  ") + "Continuar", screenWidth, firstOptionY);
        drawCenteredText((selectedPauseOption == 1 ? "> " : "  ") + "Música: " +
            (ConfigurationManager.musicEnabled ? "ON" : "OFF"), screenWidth, firstOptionY - optionSpacing);
        drawCenteredText((selectedPauseOption == 2 ? "> " : "  ") + "Sonido: " +
            (ConfigurationManager.soundEnabled ? "ON" : "OFF"), screenWidth, firstOptionY - optionSpacing * 2);
        drawCenteredText((selectedPauseOption == 3 ? "> " : "  ") + "Volver al menú principal", screenWidth, firstOptionY - optionSpacing * 3);
        drawCenteredText((selectedPauseOption == 4 ? "> " : "  ") + "Salir del juego", screenWidth, firstOptionY - optionSpacing * 4);

        font.getData().setScale(0.9f);
        drawCenteredText("Usa ARRIBA/ABAJO y pulsa ENTER", screenWidth, helpY);
        drawCenteredText("Pulsa ESC o P para reanudar", screenWidth, helpY - 28f);

        batch.end();
    }

    private void drawCenteredText(String text, float screenWidth, float y) {
        layout.setText(font, text);
        font.draw(batch, text, (screenWidth - layout.width) / 2f, y);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        uiCamera.setToOrtho(false, width, height);
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
        if (hudRenderer != null) {
            hudRenderer.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        if (font != null) {
            font.dispose();
        }

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (resourceManager != null) {
            resourceManager.dispose();
        }

        if (levelManager != null) {
            levelManager.dispose();
        }
    }
}
