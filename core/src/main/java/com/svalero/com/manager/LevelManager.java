package com.svalero.com.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
public class LevelManager {

    private final int levelNumber;

    private Texture background;
    private Texture ground;
    private Texture platformTexture;
    private Texture gemTexture;
    private Texture exitTexture;
    private Texture enemyTexture;

    private Array<Platform> platforms;
    private Array<Collectible> collectibles;
    private Array<Enemy> enemies;
    private LevelExit levelExit;

    public LevelManager(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void loadLevel() {
        platforms = new Array<>();
        collectibles = new Array<>();
        enemies = new Array<>();

        if (levelNumber == 1) {
            loadLevel1();
        } else if (levelNumber == 2) {
            loadLevel2();
        } else {
            throw new IllegalArgumentException("Nivel no soportado: " + levelNumber);
        }
    }

    private void loadLevel1() {
        background = new Texture(Gdx.files.internal("grassbackground.png"));
        ground = new Texture(Gdx.files.internal("grassground.png"));
        platformTexture = new Texture(Gdx.files.internal("grassplatform.png"));
        gemTexture = new Texture(Gdx.files.internal("redgem.png"));
        exitTexture = new Texture(Gdx.files.internal("redflag.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemiessheet.png"));

        platforms.add(new Platform(platformTexture, 220, 160, 140, 40));
        platforms.add(new Platform(platformTexture, 430, 240, 140, 40));
        platforms.add(new Platform(platformTexture, 700, 190, 140, 40));
        platforms.add(new Platform(platformTexture, 930, 300, 140, 40));
        platforms.add(new Platform(platformTexture, 1160, 220, 140, 40));

        collectibles.add(new Collectible(gemTexture, 260, 210, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 470, 290, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 735, 240, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 970, 350, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1200, 270, 32, 32, Constants.POINTS_PER_GEM));

        levelExit = new LevelExit(exitTexture, 1320, Constants.GROUND_Y, 70, 100);

        Texture placeholderEnemyTexture = enemyTexture;

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            520,
            Constants.GROUND_Y + 20,
            48,
            48,
            90f,
            Constants.GROUND_Y + 20,
            Constants.GROUND_Y + 120,
            Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            1120,
            Constants.GROUND_Y + 20,
            48,
            48,
            90f,
            Constants.GROUND_Y + 20,
            Constants.GROUND_Y + 120,
            Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            940,
            360,
            52,
            40,
            120f,
            900,
            1120,
            Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            710,
            230,
            40,
            28,
            170f,
            700,
            840,
            Enemy.EnemyType.MOUSE
        ));
    }

    private void loadLevel2() {
        background = new Texture(Gdx.files.internal("sandbackground.png"));
        ground = new Texture(Gdx.files.internal("sandground.png"));
        platformTexture = new Texture(Gdx.files.internal("sandplatform.png"));
        gemTexture = new Texture(Gdx.files.internal("greengem.png"));
        exitTexture = new Texture(Gdx.files.internal("greenflag.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemiessheet.png"));

        platforms.add(new Platform(platformTexture, 180, 150, 130, 40));
        platforms.add(new Platform(platformTexture, 360, 240, 130, 40));
        platforms.add(new Platform(platformTexture, 560, 330, 130, 40));
        platforms.add(new Platform(platformTexture, 760, 250, 130, 40));
        platforms.add(new Platform(platformTexture, 960, 170, 130, 40));
        platforms.add(new Platform(platformTexture, 1130, 270, 130, 40));

        collectibles.add(new Collectible(gemTexture, 220, 200, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 400, 290, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 600, 380, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 800, 300, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1000, 220, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1170, 320, 32, 32, Constants.POINTS_PER_GEM));

        levelExit = new LevelExit(exitTexture, 1290, Constants.GROUND_Y, 70, 100);

        Texture placeholderEnemyTexture = enemyTexture;

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            315,
            Constants.GROUND_Y + 20,
            48,
            48,
            100f,
            Constants.GROUND_Y + 20,
            Constants.GROUND_Y + 130,
            Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            900,
            Constants.GROUND_Y + 20,
            48,
            48,
            100f,
            Constants.GROUND_Y + 20,
            Constants.GROUND_Y + 130,
            Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            500,
            400,
            52,
            40,
            115f,
            460,
            760,
            Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            1020,
            350,
            52,
            40,
            135f,
            980,
            1200,
            Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
            placeholderEnemyTexture,
            760,
            290,
            40,
            28,
            180f,
            760,
            890,
            Enemy.EnemyType.MOUSE
        ));
    }

    public int getTotalGems() {
        return collectibles.size;
    }

    public void dispose() {
        if (background != null) background.dispose();
        if (ground != null) ground.dispose();
        if (platformTexture != null) platformTexture.dispose();
        if (gemTexture != null) gemTexture.dispose();
        if (exitTexture != null) exitTexture.dispose();
        if (enemyTexture != null) enemyTexture.dispose();
    }
}
