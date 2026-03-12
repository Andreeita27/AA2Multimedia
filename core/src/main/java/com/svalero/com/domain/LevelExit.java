package com.svalero.com.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelExit {

    private Texture texture;
    @Getter
    private Rectangle bounds;

    public LevelExit(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
