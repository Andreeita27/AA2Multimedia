package com.svalero.com.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.svalero.com.util.Constants;

public class GameScreen implements Screen {

    private SpriteBatch batch;

    private Texture background;
    private Texture player;
    private Texture ground;

    private Vector2 playerPosition;
    private Vector2 playerVelocity;

    private boolean onGround;

    @Override
    public void show() {
        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("grassbackground.png"));
        player = new Texture(Gdx.files.internal("player.png"));
        ground = new Texture(Gdx.files.internal("grassground.png"));

        playerPosition = new Vector2(100, Constants.GROUND_Y);
        playerVelocity = new Vector2(0, 0);

        onGround = true;
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        applyGravity(delta);
        updatePlayer(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // suelo simple repetido
        for (int x = 0; x < Gdx.graphics.getWidth(); x += 64) {
            batch.draw(ground, x, 0, 64, 80);
        }

        batch.draw(player, playerPosition.x, playerPosition.y, 64, 64);

        batch.end();
    }

    private void handleInput(float delta) {
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

        float maxX = Gdx.graphics.getWidth() - 64;
        if (playerPosition.x > maxX) {
            playerPosition.x = maxX;
        }
    }

    @Override
    public void resize(int width, int height) {
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
        background.dispose();
        player.dispose();
        ground.dispose();
    }
}
