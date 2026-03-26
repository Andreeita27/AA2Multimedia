package com.svalero.com.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy {

    public enum EnemyType {
        FROG,
        BAT,
        MOUSE
    }

    private Texture texture;
    @Getter
    private Rectangle bounds;
    private float speed;
    private float minX;
    private float maxX;
    private int direction;
    @Getter
    private boolean alive;
    @Getter
    private EnemyType type;

    // Para recordar la posición inicial de la rana
    private float initialX;
    private float initialY;

    public Enemy(Texture texture, float x, float y, float width, float height, float speed, float minX, float maxX, EnemyType type) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.direction = 1;
        this.alive = true;
        this.type = type;

        this.initialX = x;
        this.initialY = y;
    }

    public void update(float delta, float playerX) {

        if (!alive) {
            return;
        }

        switch (type) {

            case FROG:
                updateFrog(delta, playerX);
                break;

            case BAT:
            case MOUSE:
                updateHorizontalEnemy(delta);
                break;
        }
    }

    private void updateFrog(float delta, float playerX) {
        float frogCenterX = bounds.x + bounds.width / 2f;
        float distanceToPlayer = Math.abs(playerX - frogCenterX);

        // Distancia mayor para que empiece a reaccionar antes
        float activationDistance = 260f;

        if (distanceToPlayer <= activationDistance) {
            bounds.y += speed * direction * delta;

            if (bounds.y >= maxX) {
                bounds.y = maxX;
                direction = -1;
            }

            if (bounds.y <= minX) {
                bounds.y = minX;
                direction = 1;
            }
        } else {
            // Si el jugador se aleja, la rana vuelve a su posición inicial
            if (bounds.y > initialY) {
                bounds.y -= speed * delta;

                if (bounds.y <= initialY) {
                    bounds.y = initialY;
                }
            } else if (bounds.y < initialY) {
                bounds.y += speed * delta;

                if (bounds.y >= initialY) {
                    bounds.y = initialY;
                }
            }

            // Cuando vuelve a la posición inicial, se queda preparada para saltar hacia arriba
            if (bounds.y == initialY) {
                direction = 1;
            }
        }
    }

    private void updateHorizontalEnemy(float delta) {
        bounds.x += speed * direction * delta;

        if (bounds.x <= minX) {
            bounds.x = minX;
            direction = 1;
        }

        if (bounds.x + bounds.width >= maxX) {
            bounds.x = maxX - bounds.width;
            direction = -1;
        }
    }

    public void draw(SpriteBatch batch) {
        if (alive) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public void kill() {
        alive = false;
    }
}
