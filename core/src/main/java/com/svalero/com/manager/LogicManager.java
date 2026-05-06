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

    // Para comunicar cosas importantes a GameScreen
    public interface LogicCallbacks {
        void onGameOver();
        void onLevelCompleted(int score, int lives);
    }

    // Camara del jeugo
    private final OrthographicCamera camera;
    // Elementos del nivel cargados desde LevelManager
    private final Array<Platform> platforms;
    private final Array<Collectible> collectibles;
    private final Array<Enemy> enemies;
    private final LevelExit levelExit;
    private final LogicCallbacks callbacks;

    private final float worldWidth;
    // Posicion y velocidad del jugador
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    // Estado del jugador
    private boolean onGround;
    private boolean facingRight;

    // Variables de progreso y puntuacion
    private int score;
    private int lives;
    private int collectedGems;
    private int killedMice;

    // Mensajes temporales mostrados en pantalla
    private String message;
    private float messageTimer;
    // Tiempo de invulnerabilidad tras recibir daño
    private float invulnerableTimer;

    public LogicManager(
        OrthographicCamera camera,
        Array<Platform> platforms,
        Array<Collectible> collectibles,
        Array<Enemy> enemies,
        LevelExit levelExit,
        int initialScore,
        int initialLives,
        float worldWidth,
        LogicCallbacks callbacks
    ) {
        this.camera = camera;
        this.platforms = platforms;
        this.collectibles = collectibles;
        this.enemies = enemies;
        this.levelExit = levelExit;
        this.worldWidth = worldWidth;
        this.callbacks = callbacks;

        // Posicion y velocidad inicial del jugador
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

    // Gestiona el teclado y modifica la velocidad del jugador
    private void handleInput() {
        playerVelocity.x = 0;

        // Movimiento izquierda
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerVelocity.x = -Constants.PLAYER_SPEED;
            facingRight = false;
        }

        // Movimiento derecha
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerVelocity.x = Constants.PLAYER_SPEED;
            facingRight = true;
        }

        // Salta solo si el jugador esta tocando el suelo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocity.y = Constants.JUMP_FORCE;
            onGround = false;
            SoundManager.playJump();
        }
    }

    // Simula la gravedad recuciendo progresivamente la velocidad vertical
    private void applyGravity(float delta) {
        if (!onGround) {
            playerVelocity.y -= Constants.GRAVITY * delta;
        }
    }

    // Actualiza la posicion del jugador y limita el movimiento dentro del mapa
    private void updatePlayer(float delta) {
        playerPosition.x += playerVelocity.x * delta;
        playerPosition.y += playerVelocity.y * delta;

        // colision con el suelo
        if (playerPosition.y <= Constants.GROUND_Y) {
            playerPosition.y = Constants.GROUND_Y;
            playerVelocity.y = 0;
            onGround = true;
        }

        // Limite izquierdo
        if (playerPosition.x < 0) {
            playerPosition.x = 0;
        }

        // Limite derecho
        float maxX = worldWidth - Constants.PLAYER_WIDTH;
        if (playerPosition.x > maxX) {
            playerPosition.x = maxX;
        }

        // Limite superior
        float topLimit = Gdx.graphics.getHeight() - Constants.PLAYER_HEIGHT - 20f;
        if (playerPosition.y > topLimit) {
            playerPosition.y = topLimit;
            if (playerVelocity.y > 0) {
                playerVelocity.y = 0;
            }
        }
    }

    // Comprueba si el jugador aterriza sobre las plataformas
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

            // Comprueba solapamiento horizontal
            boolean horizontalOverlap = playerRight > platformLeft && playerLeft < platformRight;
            // Comprueba si cae encima de la plataforma
            boolean fallingOntoPlatform =
                playerBottom <= platformTop + 12 &&
                    playerBottom >= platformTop - 25 &&
                    playerTop > platformTop;

            if (horizontalOverlap && fallingOntoPlatform) {
                // Coloca al jugador justo encima de la plataforma
                playerPosition.y = platformTop;
                playerVelocity.y = 0;
                onGround = true;
                return;
            }
        }

        // Si no toca ninguna plataforma, vuelve a caer
        if (playerPosition.y > Constants.GROUND_Y + 1) {
            onGround = false;
        }
    }

    // Actualiza todos los enemigos
    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta, playerPosition.x);
        }
    }

    // Comprueba si el jugador recoge gemas
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
                // Suma puntos al marcador
                score += 10;
                SoundManager.playGem();

                message = "¡Gema recogida! +10";
                messageTimer = 1f;
            }
        }
    }

    // Gestiona las colisiones entre jugador y enemigos
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

                // Los ratones se mueren si saltas sobre ellos
                if (enemy.getType() == Enemy.EnemyType.MOUSE) {

                    boolean falling = playerVelocity.y < 0;

                    boolean hittingFromAbove =
                        playerPosition.y <= enemy.getBounds().y + enemy.getBounds().height + 20 &&
                            playerPosition.y >= enemy.getBounds().y + enemy.getBounds().height - 25;

                    if (falling && hittingFromAbove) {
                        enemy.kill();
                        killedMice++;

                        // Rebote tras aplastar al enemigo
                        playerVelocity.y = Constants.JUMP_FORCE * 0.6f;
                        onGround = false;

                        score += 25;
                        SoundManager.playStomp();

                        message = "¡Ratón aplastado!";
                        messageTimer = 1.2f;

                        return;
                    }
                }

                // Invulnerabilidad temporal para evitar daño continuo
                if (invulnerableTimer > 0) {
                    return;
                }

                // Resta vidas
                lives--;
                SoundManager.playHit();
                invulnerableTimer = 1.5f;

                message = "¡Ay! Te han golpeado";
                messageTimer = 1.5f;

                // Reinicio de posicion tras recibir daño
                playerPosition.x = 40;
                playerPosition.y = Constants.GROUND_Y;
                playerVelocity.set(0, 0);
                onGround = true;

                // Si no quedan vidas, muerto
                if (lives <= 0) {
                    callbacks.onGameOver();
                    return;
                }

                return;
            }
        }
    }

    // Comprueba si el jugador llega a la bandera final
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

    // Gestiona mensajes temporales en la pantalla
    private void updateMessage(float delta) {
        if (messageTimer > 0) {
            messageTimer -= delta;
            if (messageTimer <= 0) {
                message = "";
            }
        }
    }

    // Reduce progresivamente el tiempo de invulnerabilidad
    private void updateInvulnerability(float delta) {
        if (invulnerableTimer > 0) {
            invulnerableTimer -= delta;
        }
    }

    // Hace que la camara siga al jugador
    private void updateCamera() {
        float halfScreenWidth = Gdx.graphics.getWidth() / 2f;
        float halfScreenHeight = Gdx.graphics.getHeight() / 2f;

        camera.position.x = playerPosition.x + Constants.PLAYER_WIDTH / 2f;
        camera.position.y = halfScreenHeight;

        // Evita que la camara salga del mapa por la izquierda
        if (camera.position.x < halfScreenWidth) {
            camera.position.x = halfScreenWidth;
        }

        // Evita que la camara salga del mapa por la derecha
        float maxCameraX = worldWidth - halfScreenWidth;
        if (camera.position.x > maxCameraX) {
            camera.position.x = maxCameraX;
        }

        camera.update();
    }

    // Cuenta las gemas que quedan aun por recoger
    public int countRemainingGems() {
        int remaining = 0;
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                remaining++;
            }
        }
        return remaining;
    }

    // Devuelve la posicion X de la camara
    public float getCameraX() {
        return camera.position.x;
    }
}
