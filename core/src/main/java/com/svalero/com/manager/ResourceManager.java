package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.com.util.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceManager {

    private Texture playerSheet;

    // Animaciones y frames del jugador
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private TextureRegion jumpFrame;
    private TextureRegion fallFrame;

    // Animaciones y frames de los enemigos
    private Animation<TextureRegion> batAnimation;
    private Animation<TextureRegion> mouseAnimation;
    private TextureRegion mouseDeadFrame;
    private TextureRegion frogIdleFrame;
    private TextureRegion frogLeapFrame;

    // Carga y prepara los recursos graficos del jugador
    public void loadPlayerResources() {
        playerSheet = new Texture(Gdx.files.internal("playersheet.png"));

        // Divide el spritesheet en frames de 96x128 pixeles
        TextureRegion[][] frames = TextureRegion.split(playerSheet, 96, 128);

        // Frames concretos que se usan para cada estado del jugador
        TextureRegion idle = new TextureRegion(frames[0][0]);
        TextureRegion jump = new TextureRegion(frames[0][1]);
        TextureRegion fall = new TextureRegion(frames[0][2]);

        TextureRegion run0 = new TextureRegion(frames[2][6]);
        TextureRegion run1 = new TextureRegion(frames[2][7]);
        TextureRegion run2 = new TextureRegion(frames[2][8]);

        // Animacion de parada y carrera
        idleAnimation = new Animation<>(0.2f, idle);
        runAnimation = new Animation<>(0.12f, run0, run1, run2);
        // La de correr se repite en bucle mientras el jugador se mueva
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        jumpFrame = jump;
        fallFrame = fall;
    }

    // Carga y prepara los frames de los enemigos desde el spritesheet
    public void loadEnemyResources(Texture enemySheet) {
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

    // Devuelve el frame correcto del jugador segun su estado
    public TextureRegion getCurrentPlayerFrame(float stateTime, boolean onGround, Vector2 playerVelocity, boolean facingRight) {
        TextureRegion currentFrame;

        // Si esta en el aire se distingue entre salto y caida
        if (!onGround) {
            if (playerVelocity.y > 0) {
                currentFrame = new TextureRegion(jumpFrame);
            } else {
                currentFrame = new TextureRegion(fallFrame);
            }
        // Si esta en el suelo y se mueve horizontalmente, corre
        } else if (playerVelocity.x != 0) {
            currentFrame = new TextureRegion(runAnimation.getKeyFrame(stateTime, true));
        // Si esta quieto, animacion de parada
        } else {
            currentFrame = new TextureRegion(idleAnimation.getKeyFrame(stateTime, true));
        }

        // Voltea el sprite segun la direccion a la que corre el jugador
        if ((facingRight && currentFrame.isFlipX()) || (!facingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }

        return currentFrame;
    }

    // Devuelve el frame del murcielago segun el tiempo de juego
    public TextureRegion getBatFrame(float stateTime) {
        return new TextureRegion(batAnimation.getKeyFrame(stateTime, true));
    }

    // Devuelve el frame del raton si esta vivo
    public TextureRegion getMouseFrame(float stateTime, boolean alive) {
        if (alive) {
            return new TextureRegion(mouseAnimation.getKeyFrame(stateTime, true));
        }
        // Y sino pues esta muerto
        return new TextureRegion(mouseDeadFrame);
    }

    // Devuelve el frame de la rana segun su altura
    public TextureRegion getFrogFrame(float y) {
        if (y > Constants.GROUND_Y + 35) {
            return new TextureRegion(frogLeapFrame);
        }
        return new TextureRegion(frogIdleFrame);
    }

    // Libera recursos
    public void dispose() {
        if (playerSheet != null) {
            playerSheet.dispose();
        }
    }
}
