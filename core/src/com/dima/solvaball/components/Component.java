package com.dima.solvaball.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.handlers.ClickHandler;

public class Component {

    private float x;
    private float y;
    private float width = -1;
    private float height = -1;

    private TextureRegion textureRegion;

    private ClickHandler clickHandler;

    public Component(TextureRegion textureRegion, float x, float y) {
        this.x = x;
        this.y = y;
        this.textureRegion = textureRegion;
    }

    public Component(TextureRegion textureRegion, float x, float y, float width, float height) {
        this(textureRegion, x, y);
        this.width = width;
        this.height = height;
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    public void setTexture(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void draw(SpriteBatch sb) {
        if (width == -1 || height == -1)
            sb.draw(textureRegion, x, y);
        else
            sb.draw(textureRegion, x, y, width, height);
    }

    public void dispose() {
    }

    public void fireClick(int x, int y) {
        if (clickHandler != null)
            clickHandler.onClick(x, y);
    }

    public void setOnClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }


    public boolean contains(int x, int y) {
        if (width == -1 || height == -1)
            return x >= getX() && x <= getTexture().getRegionWidth() + getX()
                    && y >= getY() && y <= getTexture().getRegionHeight() + getY();
        return x >= getX() && x <= getWidth() + getX()
                && y >= getY() && y <= getHeight() + getY();
    }
    //Click
}
