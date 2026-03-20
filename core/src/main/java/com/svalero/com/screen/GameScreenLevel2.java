package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.MiJuego;
import com.svalero.com.domain.Collectible;
import com.svalero.com.domain.Enemy;
import com.svalero.com.domain.LevelExit;
import com.svalero.com.domain.Platform;
import com.svalero.com.manager.LevelManager;
import com.svalero.com.manager.LogicManager;
import com.svalero.com.manager.ResourceManager;
import com.svalero.com.manager.SoundManager;
import com.svalero.com.ui.HudRenderer;
import com.svalero.com.util.Constants;

public class GameScreenLevel2 implements Screen {

    private final MiJuego game;
    private final int initialScore;
    private final int initialLives;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Texture background;

    private float stateTime;

    private Texture ground;
    private Texture platformTexture;
    private Texture gemTexture;
    private Texture exitTexture;

    private Array<Platform> platforms;
    private Array<Collectible> collectibles;
    private Array<Enemy> enemies;
    private LevelExit levelExit;

    private int totalGems;

    private LevelManager levelManager;
    private ResourceManager resourceManager;
    private LogicManager logicManager;

    private HudRenderer hudRenderer;

    public GameScreenLevel2(MiJuego game, int initialScore, int initialLives) {
        this.game = game;
        this.initialScore = initialScore;
        this.initialLives = initialLives;
    }

    @Override
    public void show() {
        SoundManager.resumeMusic();
        hudRenderer = new HudRenderer();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        resourceManager = new ResourceManager();
        resourceManager.loadPlayerResources();
        stateTime = 0f;

        levelManager = new LevelManager(2);
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
        logicManager = new LogicManager(
            camera,
            platforms,
            collectibles,
            enemies,
            levelExit,
            initialScore,
            initialLives,
            new LogicManager.LogicCallbacks() {
                @Override
                public void onGameOver() {
                    game.setScreen(new GameOverScreen(game));
                }

                @Override
                public void onLevelCompleted(int score, int lives) {
                    game.setScreen(new VictoryScreen(game, score, lives, false, 2));
                }
            }
        );
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        logicManager.update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        drawBackground();
        drawGround();
        drawPlatforms();
        drawCollectibles();
        drawEnemies();
        levelExit.draw(batch);

        TextureRegion currentFrame = resourceManager.getCurrentPlayerFrame(
            stateTime,
            logicManager.isOnGround(),
            logicManager.getPlayerVelocity(),
            logicManager.isFacingRight()
        );

        batch.draw(
            currentFrame,
            logicManager.getPlayerPosition().x,
            logicManager.getPlayerPosition().y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        drawHud();

        batch.end();
    }

    private void drawBackground() {
        batch.draw(background, 0, 0, Constants.WORLD_WIDTH, Gdx.graphics.getHeight());
    }

    private void drawGround() {
        for (int x = 0; x < Constants.WORLD_WIDTH; x += 64) {
            batch.draw(ground, x, 0, 64, 80);
        }
    }

    private void drawPlatforms() {
        for (Platform platform : platforms) {
            platform.draw(batch);
        }
    }

    private void drawCollectibles() {
        for (Collectible collectible : collectibles) {
            collectible.draw(batch);
        }
    }

    private void drawEnemies() {
        for (Enemy enemy : enemies) {
            Rectangle bounds = enemy.getBounds();
            float x = bounds.x;
            float y = bounds.y;
            float width = bounds.width;
            float height = bounds.height;

            switch (enemy.getType()) {
                case BAT -> {
                    TextureRegion frame = resourceManager.getBatFrame(stateTime);
                    batch.draw(frame, x, y, width, height);
                }

                case MOUSE -> {
                    TextureRegion frame = resourceManager.getMouseFrame(stateTime, enemy.isAlive());
                    batch.draw(frame, x, y, width, height);
                }

                case FROG -> {
                    TextureRegion frame = resourceManager.getFrogFrame(y);
                    batch.draw(frame, x, y, width, height);
                }
            }
        }
    }

    private void drawHud() {
        hudRenderer.draw(
            batch,
            camera.position.x - Gdx.graphics.getWidth() / 2f,
            Gdx.graphics.getHeight(),
            logicManager.getLives(),
            Constants.INITIAL_LIVES,
            totalGems - logicManager.countRemainingGems(),
            totalGems,
            logicManager.getScore(),
            2
        );
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
        hudRenderer.dispose();
        batch.dispose();
        font.dispose();

        if (resourceManager != null) {
            resourceManager.dispose();
        }

        if (levelManager != null) {
            levelManager.dispose();
        }
    }
}
