package com.svalero.com.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudRenderer {

    private final BitmapFont font;
    private final BitmapFont smallFont;
    private final Texture heartIcon;
    private final Texture gemIcon;
    private final Texture panelTexture;

    public HudRenderer() {
        font = new BitmapFont();
        font.getData().setScale(1.15f);
        font.setColor(Color.WHITE);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.15f);
        smallFont.setColor(new Color(0.92f, 0.92f, 0.92f, 1f));

        heartIcon = new Texture("hearthud.png");
        gemIcon = new Texture("gemhud.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0.45f);
        pixmap.fill();
        panelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void draw(
        SpriteBatch batch,
        float cameraLeft,
        float screenHeight,
        int lives,
        int maxLives,
        int collectedGems,
        int totalGems,
        int score,
        int level
    ) {
        float panelX = cameraLeft + 14;
        float panelY = screenHeight - 108;
        float panelWidth = 290;
        float panelHeight = 92;

        batch.draw(panelTexture, panelX, panelY, panelWidth, panelHeight);

        float topRowY = screenHeight - 28;
        float bottomRowY = screenHeight - 64;

        // Corazón
        batch.draw(heartIcon, panelX + 12, topRowY - 26, 32, 32);
        drawTextWithShadow(batch, font, lives + "/" + maxLives, panelX + 55, topRowY);

        // Gema
        batch.draw(gemIcon, panelX + 140, topRowY - 26, 32, 32);
        drawTextWithShadow(batch, font, collectedGems + "/" + totalGems, panelX + 185, topRowY);

        // Puntos
        drawTextWithShadow(batch, smallFont, "Puntos: " + score, panelX + 16, bottomRowY);

        // Nivel
        drawTextWithShadow(batch, smallFont, "Nivel " + level, panelX + 170, bottomRowY);
    }

    private void drawTextWithShadow(SpriteBatch batch, BitmapFont font, String text, float x, float y) {
        Color oldColor = font.getColor().cpy();

        font.setColor(0f, 0f, 0f, 0.7f);
        font.draw(batch, text, x + 1.5f, y - 1.5f);

        font.setColor(oldColor);
        font.draw(batch, text, x, y);
    }

    public void dispose() {
        font.dispose();
        smallFont.dispose();
        heartIcon.dispose();
        gemIcon.dispose();
        panelTexture.dispose();
    }
}
