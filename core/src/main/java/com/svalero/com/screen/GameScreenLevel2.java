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

    private Texture playerSheet;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private TextureRegion jumpFrame;
    private TextureRegion fallFrame;
    private float stateTime;
    private boolean facingRight;

    private Texture ground;
    private Texture platformTexture;
    private Texture gemTexture;
    private Texture exitTexture;
    private Texture enemySheet;
    private Animation<TextureRegion> batAnimation;
    private Animation<TextureRegion> mouseAnimation;
    private TextureRegion mouseDeadFrame;
    private TextureRegion frogIdleFrame;
    private TextureRegion frogLeapFrame;

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
    private int collectedGems;
    private int killedMice;

    private String message;
    private float messageTimer;

    private float invulnerableTimer;
    private LevelManager levelManager;

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

        playerSheet = new Texture(Gdx.files.internal("playersheet.png"));
        loadPlayerAnimations();
        stateTime = 0f;
        facingRight = true;

        levelManager = new LevelManager(2);
        levelManager.loadLevel();

        background = levelManager.getBackground();
        ground = levelManager.getGround();
        platformTexture = levelManager.getPlatformTexture();
        gemTexture = levelManager.getGemTexture();
        exitTexture = levelManager.getExitTexture();
        enemySheet = levelManager.getEnemyTexture();

        loadEnemyAnimations();

        playerPosition = new Vector2(40, Constants.GROUND_Y);
        playerVelocity = new Vector2(0, 0);
        onGround = true;

        score = initialScore;
        lives = initialLives;
        message = "";
        messageTimer = 0f;
        invulnerableTimer = 0f;
        collectedGems = 0;
        killedMice = 0;

        platforms = levelManager.getPlatforms();
        collectibles = levelManager.getCollectibles();
        enemies = levelManager.getEnemies();
        levelExit = levelManager.getLevelExit();
        totalGems = levelManager.getTotalGems();
    }

    private void loadPlayerAnimations() {
        TextureRegion[][] frames = TextureRegion.split(playerSheet, 96, 128);

        TextureRegion idle = new TextureRegion(frames[0][0]);     // idle
        TextureRegion jump = new TextureRegion(frames[0][1]);     // jump
        TextureRegion fall = new TextureRegion(frames[0][2]);     // fall

        TextureRegion run0 = new TextureRegion(frames[2][6]);     // run0
        TextureRegion run1 = new TextureRegion(frames[2][7]);     // run1
        TextureRegion run2 = new TextureRegion(frames[2][8]);     // run2

        idleAnimation = new Animation<>(0.2f, idle);
        runAnimation = new Animation<>(0.12f, run0, run1, run2);
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        jumpFrame = jump;
        fallFrame = fall;
    }

    private TextureRegion getCurrentPlayerFrame() {
        TextureRegion currentFrame;

        if (!onGround) {
            if (playerVelocity.y > 0) {
                currentFrame = new TextureRegion(jumpFrame);
            } else {
                currentFrame = new TextureRegion(fallFrame);
            }
        } else if (playerVelocity.x != 0) {
            currentFrame = new TextureRegion(runAnimation.getKeyFrame(stateTime, true));
        } else {
            currentFrame = new TextureRegion(idleAnimation.getKeyFrame(stateTime, true));
        }

        if ((facingRight && currentFrame.isFlipX()) || (!facingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }

        return currentFrame;
    }

    private void loadEnemyAnimations() {
        TextureRegion batFrame1 = new TextureRegion(enemySheet, 71, 235, 70, 47);
        TextureRegion batFrame2 = new TextureRegion(enemySheet, 0, 0, 88, 37);

        batAnimation = new Animation<>(0.15f, batFrame1, batFrame2);
        batAnimation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion mouseFrame1 = new TextureRegion(enemySheet, 197, 475, 59, 35);
        TextureRegion mouseFrame2 = new TextureRegion(enemySheet, 256, 475, 58, 35);
        mouseDeadFrame = new TextureRegion(enemySheet, 202, 206, 59, 35);

        mouseAnimation = new Animation<>(0.18f, mouseFrame1, mouseFrame2);
        mouseAnimation.setPlayMode(Animation.PlayMode.LOOP);

        frogIdleFrame = new TextureRegion(enemySheet, 257, 45, 58, 39);
        frogLeapFrame = new TextureRegion(enemySheet, 197, 336, 61, 54);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
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
        TextureRegion currentFrame = getCurrentPlayerFrame();
        batch.draw(currentFrame, playerPosition.x, playerPosition.y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        drawHud();

        batch.end();
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
                    game.setScreen(new GameOverScreen(game));
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
            game.setScreen(new VictoryScreen(game, score, lives, false, 2));
        }
    }

    private boolean allGemsCollected() {
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                return false;
            }
        }
        return true;
    }

    private int countRemainingGems() {
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
            Rectangle bounds = enemy.getBounds();
            float x = bounds.x;
            float y = bounds.y;
            float width = bounds.width;
            float height = bounds.height;

            switch (enemy.getType()) {
                case BAT -> {
                    TextureRegion frame = new TextureRegion(batAnimation.getKeyFrame(stateTime, true));
                    batch.draw(frame, x, y, width, height);
                }

                case MOUSE -> {
                    TextureRegion frame;

                    if (enemy.isAlive()) {
                        frame = new TextureRegion(mouseAnimation.getKeyFrame(stateTime, true));
                    } else {
                        frame = new TextureRegion(mouseDeadFrame);
                    }

                    batch.draw(frame, x, y, width, height);
                }

                case FROG -> {
                    TextureRegion frame;

                    if (y > Constants.GROUND_Y + 35) {
                        frame = new TextureRegion(frogLeapFrame);
                    } else {
                        frame = new TextureRegion(frogIdleFrame);
                    }

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
                lives,
                Constants.INITIAL_LIVES,
                totalGems - countRemainingGems(),
                totalGems,
                score,
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
        playerSheet.dispose();

        if (levelManager != null) {
            levelManager.dispose();
        }
    }
}
