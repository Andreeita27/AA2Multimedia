package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.domain.Collectible;
import com.svalero.com.domain.Enemy;
import com.svalero.com.domain.LevelExit;
import com.svalero.com.domain.Platform;
import com.svalero.com.util.Constants;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Texture background;
    private Texture player;
    private Texture ground;
    private Texture platformTexture;
    private Texture gemTexture;
    private Texture exitTexture;
    private Texture frogTexture;

    private Vector2 playerPosition;
    private Vector2 playerVelocity;

    private boolean onGround;

    private Array<Platform> platforms;
    private Array<Collectible> collectibles;
    private Array<Enemy> enemies;
    private LevelExit levelExit;

    private int score;
    private int totalGems;
    private int lives;

    private String message;
    private float messageTimer;

    private float invulnerableTimer;

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        background = new Texture(Gdx.files.internal("grassbackground.png"));
        player = new Texture(Gdx.files.internal("player.png"));
        ground = new Texture(Gdx.files.internal("grassground.png"));
        platformTexture = new Texture(Gdx.files.internal("grassplatform.png"));
        gemTexture = new Texture(Gdx.files.internal("redgem.png"));
        exitTexture = new Texture(Gdx.files.internal("finishflag.png"));
        frogTexture = new Texture(Gdx.files.internal("frog.png"));

        playerPosition = new Vector2(100, Constants.GROUND_Y);
        playerVelocity = new Vector2(0, 0);
        onGround = true;

        score = 0;
        lives = Constants.INITIAL_LIVES;
        message = "";
        messageTimer = 0f;
        invulnerableTimer = 0f;

        platforms = new Array<>();
        collectibles = new Array<>();
        enemies = new Array<>();

        platforms.add(new Platform(platformTexture, 220, 170, 140, 40));
        platforms.add(new Platform(platformTexture, 430, 250, 140, 40));
        platforms.add(new Platform(platformTexture, 700, 330, 140, 40));
        platforms.add(new Platform(platformTexture, 980, 260, 140, 40));
        platforms.add(new Platform(platformTexture, 1180, 180, 140, 40));

        collectibles.add(new Collectible(gemTexture, 250, 220, 32, 32, Constants.POINTS_PER_CRYSTAL));
        collectibles.add(new Collectible(gemTexture, 470, 300, 32, 32, Constants.POINTS_PER_CRYSTAL));
        collectibles.add(new Collectible(gemTexture, 740, 380, 32, 32, Constants.POINTS_PER_CRYSTAL));
        collectibles.add(new Collectible(gemTexture, 1010, 310, 32, 32, Constants.POINTS_PER_CRYSTAL));
        collectibles.add(new Collectible(gemTexture, 1230, 230, 32, 32, Constants.POINTS_PER_CRYSTAL));

        totalGems = collectibles.size;

        levelExit = new LevelExit(exitTexture, 1320, Constants.GROUND_Y, 60, 90);

        enemies.add(new Enemy(frogTexture, 260, Constants.GROUND_Y, 48, 48, 90f, 220, 420));
        enemies.add(new Enemy(frogTexture, 720, 370, 48, 48, 80f, 700, 840));
    }

    @Override
    public void render(float delta) {
        handleInput();
        applyGravity(delta);
        updatePlayer(delta);
        checkPlatformCollisions();
        updateEnemies(delta);
        checkCollectibles();
        checkEnemyCollisions();
        checkLevelExit();
        updateMessage(delta);
        updateInvulnerability(delta);
        updateCamera();

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
        batch.draw(player, playerPosition.x, playerPosition.y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        drawHud();

        batch.end();
    }

    private void handleInput() {
        playerVelocity.x = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerVelocity.x = -Constants.PLAYER_SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerVelocity.x = Constants.PLAYER_SPEED;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocity.y = Constants.JUMP_FORCE;
            onGround = false;
        }
    }

    private void applyGravity(float delta) {
        if (!onGround) {
            playerVelocity.y -= Constants.GRAVITY * delta;
        }
    }

    private void updatePlayer(float delta) {
        playerPosition.x += playerVelocity.x * delta;
        playerPosition.y += playerVelocity.y * delta;

        if (playerPosition.y <= Constants.GROUND_Y) {
            playerPosition.y = Constants.GROUND_Y;
            playerVelocity.y = 0;
            onGround = true;
        }

        if (playerPosition.x < 0) {
            playerPosition.x = 0;
        }

        float maxX = Constants.WORLD_WIDTH - Constants.PLAYER_WIDTH;
        if (playerPosition.x > maxX) {
            playerPosition.x = maxX;
        }
    }

    private void checkPlatformCollisions() {
        if (playerVelocity.y > 0) {
            return;
        }

        float playerBottom = playerPosition.y;
        float playerTop = playerPosition.y + Constants.PLAYER_HEIGHT;
        float playerLeft = playerPosition.x;
        float playerRight = playerPosition.x + Constants.PLAYER_WIDTH;

        for (Platform platform : platforms) {
            float platformLeft = platform.getBounds().x;
            float platformRight = platform.getBounds().x + platform.getBounds().width;
            float platformTop = platform.getBounds().y + platform.getBounds().height;

            boolean horizontalOverlap = playerRight > platformLeft && playerLeft < platformRight;
            boolean fallingOntoPlatform =
                playerBottom <= platformTop + 12 &&
                    playerBottom >= platformTop - 25 &&
                    playerTop > platformTop;

            if (horizontalOverlap && fallingOntoPlatform) {
                playerPosition.y = platformTop;
                playerVelocity.y = 0;
                onGround = true;
                return;
            }
        }

        if (playerPosition.y > Constants.GROUND_Y + 1) {
            onGround = false;
        }
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
    }

    private void checkCollectibles() {
        Rectangle playerBounds = new Rectangle(
            playerPosition.x,
            playerPosition.y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected() && collectible.getBounds().overlaps(playerBounds)) {
                collectible.collect();
                score += collectible.getPoints();
            }
        }
    }

    private void checkEnemyCollisions() {
        if (invulnerableTimer > 0) {
            return;
        }

        Rectangle playerBounds = new Rectangle(
            playerPosition.x,
            playerPosition.y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        for (Enemy enemy : enemies) {
            if (enemy.getBounds().overlaps(playerBounds)) {
                lives--;
                invulnerableTimer = 1.5f;
                message = "¡Ay! Te han golpeado";
                messageTimer = 1.5f;

                playerPosition.x = 100;
                playerPosition.y = Constants.GROUND_Y;
                playerVelocity.set(0, 0);
                onGround = true;

                if (lives <= 0) {
                    message = "Game Over";
                    messageTimer = 3f;
                }
                return;
            }
        }
    }

    private void checkLevelExit() {
        Rectangle playerBounds = new Rectangle(
            playerPosition.x,
            playerPosition.y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        if (playerBounds.overlaps(levelExit.getBounds())) {
            if (allCrystalsCollected()) {
                message = "¡Nivel completado!";
                messageTimer = 2f;
            } else {
                int remaining = countRemainingCrystals();
                message = "Te faltan " + remaining + " gemas";
                messageTimer = 2f;
            }
        }
    }

    private boolean allCrystalsCollected() {
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                return false;
            }
        }
        return true;
    }

    private int countRemainingCrystals() {
        int remaining = 0;
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                remaining++;
            }
        }
        return remaining;
    }

    private void updateMessage(float delta) {
        if (messageTimer > 0) {
            messageTimer -= delta;
            if (messageTimer <= 0) {
                message = "";
            }
        }
    }

    private void updateInvulnerability(float delta) {
        if (invulnerableTimer > 0) {
            invulnerableTimer -= delta;
        }
    }

    private void updateCamera() {
        float halfScreenWidth = Gdx.graphics.getWidth() / 2f;

        camera.position.x = playerPosition.x + Constants.PLAYER_WIDTH / 2f;
        camera.position.y = Gdx.graphics.getHeight() / 2f;

        if (camera.position.x < halfScreenWidth) {
            camera.position.x = halfScreenWidth;
        }

        float maxCameraX = Constants.WORLD_WIDTH - halfScreenWidth;
        if (camera.position.x > maxCameraX) {
            camera.position.x = maxCameraX;
        }

        camera.update();
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
            enemy.draw(batch);
        }
    }

    private void drawHud() {
        float cameraLeft = camera.position.x - Gdx.graphics.getWidth() / 2f;
        float hudX = cameraLeft + 20;
        float hudY = Gdx.graphics.getHeight() - 20;

        font.draw(batch, "Vidas: " + lives, hudX, hudY);
        font.draw(batch, "Puntos: " + score, hudX, hudY - 30);
        font.draw(batch, "Gemas: " + (totalGems - countRemainingCrystals()) + "/" + totalGems, hudX, hudY - 60);

        if (!message.isEmpty()) {
            font.draw(batch, message, cameraLeft + 260, Gdx.graphics.getHeight() - 50);
        }
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
        background.dispose();
        player.dispose();
        ground.dispose();
        platformTexture.dispose();
        gemTexture.dispose();
        exitTexture.dispose();
        frogTexture.dispose();
    }
}
