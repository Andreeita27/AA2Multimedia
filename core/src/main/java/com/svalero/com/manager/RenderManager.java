package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.svalero.com.domain.Collectible;
import com.svalero.com.domain.Enemy;
import com.svalero.com.domain.LevelExit;
import com.svalero.com.domain.Platform;
import com.svalero.com.ui.HudRenderer;
import com.svalero.com.util.Constants;

public class RenderManager {

    // SpriteBatch es el objeto de libgdx que dibuja texturas en la pantalla
    private final SpriteBatch batch;
    // Clase encargada de dibujar la info del hud
    private final HudRenderer hudRenderer;
    //Gestiona los frames de animacion del jugador y los enemigos
    private final ResourceManager resourceManager;

    public RenderManager(SpriteBatch batch, HudRenderer hudRenderer, ResourceManager resourceManager) {
        this.batch = batch;
        this.hudRenderer = hudRenderer;
        this.resourceManager = resourceManager;
    }

    // Metodo principal de renderizado
    // Dibuja en orden concreto: fondo, suelo, objetos, enemigos, jugador y hud
    public void render(
        float stateTime,
        LogicManager logicManager,
        Texture background,
        Texture ground,
        Array<Platform> platforms,
        Array<Collectible> collectibles,
        Array<Enemy> enemies,
        LevelExit levelExit,
        int totalGems,
        int levelNumber,
        float worldWidth
    ) {
        drawBackground(background, worldWidth);
        drawGround(ground, worldWidth);
        drawPlatforms(platforms);
        drawCollectibles(collectibles);
        drawEnemies(enemies, stateTime);
        levelExit.draw(batch);
        drawPlayer(logicManager, stateTime);
        drawHud(logicManager, totalGems, levelNumber);
    }

    // Fondo ocupando el ancho del nivel
    private void drawBackground(Texture background, float worldWidth) {
        batch.draw(background, 0, 0, worldWidth, Gdx.graphics.getHeight());
    }

    // Suelo desde abajo hasta la altura definida en constantes
    private void drawGround(Texture ground, float worldWidth) {
        batch.draw(ground, 0, 0, worldWidth, Constants.GROUND_Y);
    }

    // Plataformas
    private void drawPlatforms(Array<Platform> platforms) {
        for (Platform platform : platforms) {
            platform.draw(batch);
        }
    }

    // Gemas que aun no han sido recogidas
    private void drawCollectibles(Array<Collectible> collectibles) {
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                collectible.draw(batch);
            }
        }
    }

    // Enemigos con animacion distinta dependiendo del tipo
    private void drawEnemies(Array<Enemy> enemies, float stateTime) {
        for (Enemy enemy : enemies) {
            Rectangle bounds = enemy.getBounds();
            float x = bounds.x;
            float y = bounds.y;
            float width = bounds.width;
            float height = bounds.height;

            switch (enemy.getType()) {
                case BAT -> {
                    // Animacion en funcion del tiempo
                    TextureRegion frame = resourceManager.getBatFrame(stateTime);
                    batch.draw(frame, x, y, width, height);
                }

                case MOUSE -> {
                    // Frame distinto si esta vivo o muerto
                    TextureRegion frame = resourceManager.getMouseFrame(stateTime, enemy.isAlive());
                    batch.draw(frame, x, y, width, height);
                }

                case FROG -> {
                    // Cambia segun su posicion vertical
                    TextureRegion frame = resourceManager.getFrogFrame(y);
                    batch.draw(frame, x, y, width, height);
                }
            }
        }
    }

    // Dibuja al jugador con el frame adecuado segun su estado
    private void drawPlayer(LogicManager logicManager, float stateTime) {
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
    }

    // Dibuja el hud ajustandolo a la posicion de la camara
    private void drawHud(LogicManager logicManager, int totalGems, int levelNumber) {
        hudRenderer.draw(
            batch,
            logicManager.getCameraX() - Gdx.graphics.getWidth() / 2f,
            Gdx.graphics.getHeight(),
            logicManager.getLives(),
            Constants.INITIAL_LIVES,
            totalGems - logicManager.countRemainingGems(),
            totalGems,
            logicManager.getScore(),
            levelNumber
        );
    }
}
