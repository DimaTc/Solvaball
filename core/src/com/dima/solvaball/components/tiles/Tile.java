package com.dima.solvaball.components.tiles;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.Ball;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.handlers.SelectionHandler;

public class Tile extends Component {

    private int indexX = -1;
    private int indexY = -1;
    private SelectionHandler handler;
    private TileType tileType = null;
    private Tile extraTile = null;
    private Ball ball = null;
    private boolean suggested = false;
    private boolean selected = false;

    public Tile(TextureRegion textureRegion, float x, float y) {
        super(textureRegion, x, y);
    }

    public Tile(TextureRegion textureRegion, float x, float y, float width, float height) {
        super(textureRegion, x, y, width, height);
    }

    public Tile getExtraTile() {
        return extraTile;
    }

    public void setExtraTile(Tile extraTile) {
        this.extraTile = extraTile;
    }

    public SelectionHandler getOnSelectHandler() {
        return handler;
    }

    public void setOnSelectHandler(SelectionHandler handler) {
        this.handler = handler;
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public void reset() {
        ball = null;
        tileType = null;
        extraTile = null;
    }

    public void removeBall() {
        if (ball != null)
            ball.setParentTile(null);
        setBall(null);
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public void setTileType(TileType tileType, int[] args) {
        this.tileType = tileType;
        switch (tileType) {
            case END:
                extraTile = new End(this);
                break;
            case ICE:
                extraTile = new Ice(this);
                break;
            case SAND:
                extraTile = new Sand(this);
                break;
            case POWER:
                extraTile = new Power(this);
                break;
            case WALL:
                if (args.length != 0)
                    extraTile = new Wall(this, args);
                else this.tileType = null;
                break;
        }
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
//        if (tileType == TileType.END)
//            if (ball != null)
//                getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTileOk));
//            else if (this.ball != null)
//                getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile));
        this.ball = ball;
        if (ball != null)
            ball.setParentTile(this);


    }


    @Override
    public void draw(SpriteBatch sb) {
        if (suggested) {
            sb.setColor(Color.CYAN);
        } else if (selected)
            sb.setColor(new Color(0.7f, 0.7f, 1, 1));
        super.draw(sb);
        if (tileType != null && tileType == TileType.ICE && suggested)
            sb.setColor(Color.ORANGE);
        if (extraTile != null)
            extraTile.draw(sb);
        sb.setColor(Color.WHITE);

        if (ball != null)
            ball.draw(sb);
    }

    @Override
    public void fireClick(int x, int y) {
        if (ball != null)
            handler.onBallSelected(ball);
        else if (handler != null)
            handler.onTileSelected(this);

    }

    public TileType getTileType() {
        return tileType;
    }

    public int getDistance(Tile tile) {
        int dx = tile.getIndexX() - this.indexX;
        int dy = tile.getIndexY() - this.indexY;
        return (dx == 0 ? dy : dx);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void dispose() {
        if (ball != null)
            ball.dispose();
        if (extraTile != null)
            extraTile.dispose();
    }

    public enum TileType {
        END,
        ICE,
        POWER,
        SAND,
        WALL

    }
}
