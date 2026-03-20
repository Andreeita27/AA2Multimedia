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

    private float worldWidth;

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

        switch (levelNumber) {
            case 1 -> loadLevel1();
            case 2 -> loadLevel2();
            case 3 -> loadLevel3();
            case 4 -> loadLevel4();
            default -> throw new IllegalArgumentException("Nivel " + levelNumber + " no existe.");
        }
    }

    private void loadLevel1() {
        worldWidth = 1390f;
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
        worldWidth = 1390f;
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

    private void loadLevel3() {
        worldWidth = 1750f;
        background = new Texture(Gdx.files.internal("snowbackground.png"));
        ground = new Texture(Gdx.files.internal("snowground.png"));
        platformTexture = new Texture(Gdx.files.internal("snowplatform.png"));
        gemTexture = new Texture(Gdx.files.internal("bluegem.png"));
        exitTexture = new Texture(Gdx.files.internal("blueflag.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemiessheet.png"));

        platforms.add(new Platform(platformTexture, 210, 155, 125, 40));
        platforms.add(new Platform(platformTexture, 400, 250, 120, 40));
        platforms.add(new Platform(platformTexture, 610, 345, 120, 40));
        platforms.add(new Platform(platformTexture, 840, 270, 120, 40));
        platforms.add(new Platform(platformTexture, 1060, 375, 120, 40));
        platforms.add(new Platform(platformTexture, 1290, 290, 120, 40));
        platforms.add(new Platform(platformTexture, 1500, 210, 120, 40));

        collectibles.add(new Collectible(gemTexture, 245, 205, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 435, 300, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 645, 395, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 875, 320, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1095, 425, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1325, 340, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1535, 260, 32, 32, Constants.POINTS_PER_GEM));

        levelExit = new LevelExit(exitTexture, 1680, Constants.GROUND_Y, 70, 100);

        Texture placeholderEnemyTexture = enemyTexture;

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                360,
                Constants.GROUND_Y + 20,
                48,
                48,
                115f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 145,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                930,
                Constants.GROUND_Y + 20,
                48,
                48,
                115f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 145,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1450,
                Constants.GROUND_Y + 20,
                48,
                48,
                120f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 150,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                700,
                420,
                52,
                40,
                135f,
                680,
                900,
                Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1180,
                440,
                52,
                40,
                150f,
                1120,
                1420,
                Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                835,
                310,
                40,
                28,
                200f,
                830,
                960,
                Enemy.EnemyType.MOUSE
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1490,
                250,
                40,
                28,
                210f,
                1480,
                1605,
                Enemy.EnemyType.MOUSE
        ));
    }

    private void loadLevel4() {
        worldWidth = 2120f;
        background = new Texture(Gdx.files.internal("stonebackground.png"));
        ground = new Texture(Gdx.files.internal("stoneground.png"));
        platformTexture = new Texture(Gdx.files.internal("stoneplatform.png"));
        gemTexture = new Texture(Gdx.files.internal("yellowgem.png"));
        exitTexture = new Texture(Gdx.files.internal("yellowflag.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemiessheet.png"));

        platforms.add(new Platform(platformTexture, 200, 160, 120, 40));
        platforms.add(new Platform(platformTexture, 380, 255, 120, 40));
        platforms.add(new Platform(platformTexture, 560, 350, 120, 40));
        platforms.add(new Platform(platformTexture, 760, 240, 120, 40));
        platforms.add(new Platform(platformTexture, 960, 365, 120, 40));
        platforms.add(new Platform(platformTexture, 1180, 285, 120, 40));
        platforms.add(new Platform(platformTexture, 1390, 400, 120, 40));
        platforms.add(new Platform(platformTexture, 1620, 300, 120, 40));
        platforms.add(new Platform(platformTexture, 1840, 215, 120, 40));

        collectibles.add(new Collectible(gemTexture, 235, 210, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 415, 305, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 595, 400, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 795, 290, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 995, 415, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1215, 335, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1425, 450, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1655, 350, 32, 32, Constants.POINTS_PER_GEM));
        collectibles.add(new Collectible(gemTexture, 1875, 265, 32, 32, Constants.POINTS_PER_GEM));

        levelExit = new LevelExit(exitTexture, 2050, Constants.GROUND_Y, 70, 100);

        Texture placeholderEnemyTexture = enemyTexture;

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                360,
                Constants.GROUND_Y + 20,
                48,
                48,
                130f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 155,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                930,
                Constants.GROUND_Y + 20,
                48,
                48,
                130f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 155,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1540,
                Constants.GROUND_Y + 20,
                48,
                48,
                135f,
                Constants.GROUND_Y + 20,
                Constants.GROUND_Y + 160,
                Enemy.EnemyType.FROG
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                520,
                430,
                52,
                40,
                160f,
                500,
                760,
                Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1100,
                445,
                52,
                40,
                165f,
                1040,
                1325,
                Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1720,
                390,
                52,
                40,
                175f,
                1660,
                1940,
                Enemy.EnemyType.BAT
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                755,
                280,
                40,
                28,
                220f,
                750,
                890,
                Enemy.EnemyType.MOUSE
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1200,
                325,
                40,
                28,
                180f,
                1180,
                1300,
                Enemy.EnemyType.MOUSE
        ));

        enemies.add(new Enemy(
                placeholderEnemyTexture,
                1835,
                255,
                40,
                28,
                235f,
                1830,
                1955,
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
