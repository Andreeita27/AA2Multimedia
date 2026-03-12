package com.svalero.com.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy {

    private Texture texture;
    @Getter
    private Rectangle bounds;
    private float speed;
    private float minX;
    private float maxX;
    private int direction;

    public Enemy(Texture texture, float x, float y, float width, float height, float speed, float minX, float maxX) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.direction = 1;
    }

    public void update(float delta) {
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
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
