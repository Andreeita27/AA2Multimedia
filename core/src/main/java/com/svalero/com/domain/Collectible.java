package com.svalero.com.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Collectible {

    private Texture texture;
    private Rectangle bounds;
    @Getter
    private boolean collected;
    @Getter
    private int points;

    public Collectible(Texture texture, float x, float y, float width, float height, int points) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.collected = false;
        this.points = points;
    }

    public void draw(SpriteBatch batch) {
        if (!collected) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public void collect() {
        collected = true;
    }
}
