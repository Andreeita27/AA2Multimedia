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

    public Enemy(Texture texture, float x, float y, float width, float height, float speed, float minX, float maxX, EnemyType type) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.direction = 1;
        this.alive = true;
        this.type = type;
    }

    public void update(float delta) {

        if (!alive) {
            return;
        }

        switch (type) {

            case FROG:
                // salto en el sitio
                bounds.y += speed * direction * delta;

                if (bounds.y >= maxX) {
                    direction = -1;
                }

                if (bounds.y <= minX) {
                    direction = 1;
                }

                break;

            case BAT:
            case MOUSE:
                // movimiento horizontal
                bounds.x += speed * direction * delta;

                if (bounds.x <= minX) {
                    bounds.x = minX;
                    direction = 1;
                }

                if (bounds.x + bounds.width >= maxX) {
                    bounds.x = maxX - bounds.width;
                    direction = -1;
                }

                break;
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
