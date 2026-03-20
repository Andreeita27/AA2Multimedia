package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.domain.Collectible;
import com.svalero.com.domain.Enemy;
import com.svalero.com.domain.LevelExit;
import com.svalero.com.domain.Platform;
import com.svalero.com.util.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogicManager {

    public interface LogicCallbacks {
        void onGameOver();
        void onLevelCompleted(int score, int lives);
    }

    private final OrthographicCamera camera;
    private final Array<Platform> platforms;
    private final Array<Collectible> collectibles;
    private final Array<Enemy> enemies;
    private final LevelExit levelExit;
    private final LogicCallbacks callbacks;

    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    private boolean onGround;
    private boolean facingRight;

    private int score;
    private int lives;
    private int collectedGems;
    private int killedMice;

    private String message;
    private float messageTimer;
    private float invulnerableTimer;

    public LogicManager(
        OrthographicCamera camera,
        Array<Platform> platforms,
        Array<Collectible> collectibles,
        Array<Enemy> enemies,
        LevelExit levelExit,
        int initialScore,
        int initialLives,
        LogicCallbacks callbacks
    ) {
        this.camera = camera;
        this.platforms = platforms;
        this.collectibles = collectibles;
        this.enemies = enemies;
        this.levelExit = levelExit;
        this.callbacks = callbacks;

        this.playerPosition = new Vector2(40, Constants.GROUND_Y);
        this.playerVelocity = new Vector2(0, 0);

        this.onGround = true;
        this.facingRight = true;

        this.score = initialScore;
        this.lives = initialLives;
        this.collectedGems = 0;
        this.killedMice = 0;

        this.message = "";
        this.messageTimer = 0f;
        this.invulnerableTimer = 0f;
    }

    public void update(float delta) {
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
    }

    private void handleInput() {
        playerVelocity.x = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerVelocity.x = -Constants.PLAYER_SPEED;
            facingRight = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerVelocity.x = Constants.PLAYER_SPEED;
            facingRight = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocity.y = Constants.JUMP_FORCE;
            onGround = false;
            SoundManager.playJump();
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
                collectedGems++;
                score += 10;
                SoundManager.playGem();

                message = "¡Gema recogida! +10";
                messageTimer = 1f;
            }
        }
    }

    private void checkEnemyCollisions() {
        Rectangle playerBounds = new Rectangle(
            playerPosition.x,
            playerPosition.y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        for (Enemy enemy : enemies) {

            if (!enemy.isAlive()) {
                continue;
            }

            if (enemy.getBounds().overlaps(playerBounds)) {

                if (enemy.getType() == Enemy.EnemyType.MOUSE) {

                    boolean falling = playerVelocity.y < 0;

                    boolean hittingFromAbove =
                        playerPosition.y <= enemy.getBounds().y + enemy.getBounds().height + 20 &&
                            playerPosition.y >= enemy.getBounds().y + enemy.getBounds().height - 25;

                    if (falling && hittingFromAbove) {
                        enemy.kill();
                        killedMice++;

                        playerVelocity.y = Constants.JUMP_FORCE * 0.6f;
                        onGround = false;

                        score += 25;
                        SoundManager.playStomp();

                        message = "¡Ratón aplastado!";
                        messageTimer = 1.2f;

                        return;
                    }
                }

                if (invulnerableTimer > 0) {
                    return;
                }

                lives--;
                SoundManager.playHit();
                invulnerableTimer = 1.5f;

                message = "¡Ay! Te han golpeado";
                messageTimer = 1.5f;

                playerPosition.x = 40;
                playerPosition.y = Constants.GROUND_Y;
                playerVelocity.set(0, 0);
                onGround = true;

                if (lives <= 0) {
                    callbacks.onGameOver();
                    return;
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
            SoundManager.playWin();
            callbacks.onLevelCompleted(score, lives);
        }
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

    public int countRemainingGems() {
        int remaining = 0;
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                remaining++;
            }
        }
        return remaining;
    }

    public boolean allGemsCollected() {
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                return false;
            }
        }
        return true;
    }
}
