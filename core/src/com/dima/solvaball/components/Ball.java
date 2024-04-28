package com.dima.solvaball.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.tiles.Tile;
import com.dima.solvaball.handlers.SelectionHandler;
import com.dima.solvaball.utils.AssetLoader;
import com.dima.solvaball.utils.FontGenerator;

public class Ball extends Component {

    private int ballMoves = 0;
    private int indexX = 0;
    private int indexY = 0;
    private float stringX = 0;
    private float stringY = 0;
    private GlyphLayout movesLayout;
    private BitmapFont bitmapFont;
    private boolean selected = false;
    private SelectionHandler handler;
    private Tile parentTile = null;

    public Ball(Tile tile) {
        super(null, tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
        this.indexX = tile.getIndexX();
        this.indexY = tile.getIndexY();
        parentTile = tile;
    }

    public Ball(Tile tile, int moves) {
        this(tile);
        this.ballMoves = moves;
    }

    public Ball(TextureRegion textureRegion, float x, float y) {
        super(textureRegion, x, y);
    }

    public Ball(TextureRegion textureRegion, float x, float y, float width, float height) {
        super(textureRegion, x, y, width, height);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getBallMoves() {
        return ballMoves;
    }

    public void setBallMoves(int ballMoves) {
        if (ballMoves == 0)
            setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.BallEmpty));
        else if (this.ballMoves == 0)
            setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.Ball));

        this.ballMoves = ballMoves < 0 ? 0 : ballMoves;
        updateText();

    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public void init() {
        if (ballMoves == 0)
            setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.BallEmpty));
        else
            setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.Ball));

        bitmapFont = FontGenerator.getInstance().getFontBySize((int) getWidth() / 5);
        movesLayout = new GlyphLayout();
        updateText();

    }

    public void updateText() {
        movesLayout.setText(bitmapFont, ballMoves + "");
        float assetOffset = getWidth() / 93f;//small offset because of the graphics
        stringX = getX() + getWidth() / 2f - movesLayout.width / 2f - assetOffset;
        stringY = getY() + getHeight() / 2f + movesLayout.height / 2f;
    }


    @Override
    public void draw(SpriteBatch sb) {
        if (selected) {
            sb.setColor(Color.CYAN);
            if (ballMoves == 0)
                sb.setColor(Color.ORANGE);
        }
        super.draw(sb);
        bitmapFont.draw(sb, ballMoves + "", stringX, stringY);
        sb.setColor(Color.WHITE);

    }

    public SelectionHandler getOnSelectHandler() {
        return handler;
    }

    public void setOnSelectHandler(SelectionHandler handler) {
        this.handler = handler;
    }

    public Tile getParentTile() {
        return parentTile;
    }

    public void setParentTile(Tile tile) {
        parentTile = tile;
        if (tile == null)
            return;
        indexY = tile.getIndexY();
        indexX = tile.getIndexX();
        setX(tile.getX());
        setY(tile.getY());
//        init();

    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
    }
}
