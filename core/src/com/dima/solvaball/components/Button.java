package com.dima.solvaball.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.logic.SoundManager;
import com.dima.solvaball.utils.FontGenerator;

public class Button extends Component {

    private final int MAX_SIZE = 99;
    private String buttonText = "";
    private BitmapFont font;
    private float stringX = 0;
    private float stringY = 0;
    private FontSize fontSize = FontSize.MEDIUM;
    private boolean disabled = false;

    public Button(TextureRegion textureRegion, float x, float y) {
        super(textureRegion, x, y);
    }

    public Button(TextureRegion textureRegion, float x, float y, float width, float height) {
        super(textureRegion, x, y, width, height);
    }

    public Button(TextureRegion textureRegion, float x, float y, float width, float height, String text) {
        this(textureRegion, x, y, width, height);
        setText(text);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        if (font != null)
            if (disabled)
                font.setColor(Color.GRAY);
            else
                font.setColor(Color.WHITE);
    }

    public float getStringX() {
        return stringX;
    }

    public void setStringX(float stringX) {
        this.stringX = stringX;
    }

    public float getStringY() {
        return stringY;
    }

    public void setStringY(float stringY) {
        this.stringY = stringY;

    }

    public void setStringSize(FontSize fontSize) {
        this.fontSize = fontSize;
        setText(buttonText, true);

    }

    @Override
    public void fireClick(int x, int y) {
        if (!disabled) {
            super.fireClick(x, y);
            SoundManager.getInstance().playSound(SoundManager.Sounds.CLICK);
        }
    }

    public String getText() {
        return buttonText;
    }

    public void setText(String text) {
        setText(text, false);
    }

    public void setText(String text, boolean force) {
        buttonText = text;
        if (text.length() > 0 && font == null || force) {
            int tmpSize = (int) getWidth() / fontSize.size > MAX_SIZE ? MAX_SIZE : (int) getWidth() / fontSize.size;//changed to fix font bug (max size = MAX_SIZE)
            BitmapFont tmpFont = FontGenerator.getInstance().getFontBySize(tmpSize);
            font = new BitmapFont(tmpFont.getData(), tmpFont.getRegion(), tmpFont.usesIntegerPositions());
            setDisabled(disabled); //in case set Text called after setDisabled - this will keep the color
            GlyphLayout layout = new GlyphLayout(font, text);
            stringX = getX() + getWidth() / 2 - layout.width / 2;
            stringY = getY() + getHeight() / 2 + layout.height / 2;
        }
    }

    @Override
    public void draw(SpriteBatch sb) {
        if (font == null && disabled)
            sb.setColor(Color.GRAY);
        if (getWidth() == -1 || getHeight() == -1)
            sb.draw(getTexture(), getX(), getY());
        else
            sb.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
        if (font != null)
            font.draw(sb, buttonText, stringX, stringY);

        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    public enum FontSize {
        SMALLEST(15),
        SMALL(10),
        MEDIUM(8),
        LARGE(5),
        LARGEST(3);

        private final int size;

        FontSize(int size) {
            this.size = size;
        }
    }
}
