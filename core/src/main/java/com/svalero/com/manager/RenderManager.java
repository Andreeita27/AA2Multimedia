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

    private final SpriteBatch batch;
    private final HudRenderer hudRenderer;
    private final ResourceManager resourceManager;

    public RenderManager(SpriteBatch batch, HudRenderer hudRenderer, ResourceManager resourceManager) {
        this.batch = batch;
        this.hudRenderer = hudRenderer;
        this.resourceManager = resourceManager;
    }

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
        int levelNumber
    ) {
        drawBackground(background);
        drawGround(ground);
        drawPlatforms(platforms);
        drawCollectibles(collectibles);
        drawEnemies(enemies, stateTime);
        levelExit.draw(batch);
        drawPlayer(logicManager, stateTime);
        drawHud(logicManager, totalGems, levelNumber);
    }

    private void drawBackground(Texture background) {
        batch.draw(background, 0, 0, Constants.WORLD_WIDTH, Gdx.graphics.getHeight());
    }

    private void drawGround(Texture ground) {
        batch.draw(ground, 0, 0, Constants.WORLD_WIDTH, Constants.GROUND_Y);
    }

    private void drawPlatforms(Array<Platform> platforms) {
        for (Platform platform : platforms) {
            platform.draw(batch);
        }
    }

    private void drawCollectibles(Array<Collectible> collectibles) {
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected()) {
                collectible.draw(batch);
            }
        }
    }

    private void drawEnemies(Array<Enemy> enemies, float stateTime) {
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
