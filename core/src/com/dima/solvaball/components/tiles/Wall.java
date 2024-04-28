package com.dima.solvaball.components.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;

public class Wall extends Tile {
    private Tile parentTile;
    private ArrayList<Direction> directions;

    public Wall(Tile tile, int[] dirs) {
        super(AssetLoader.getInstance().getTexture(AssetLoader.Asset.WallTile), tile.getX(),
                tile.getY(), tile.getWidth(), tile.getHeight());

        this.directions = new ArrayList<>();
        setIndexX(tile.getIndexX());
        setIndexY(tile.getIndexY());
        this.parentTile = tile;
        for (int i : dirs)
            this.directions.add(Direction.values()[i]);
    }

    public void init() {
        setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.IceTile));
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    @Override
    public void draw(SpriteBatch sb) {
        for (Direction dir : directions) {
            switch (dir) {
                case UP: //up
                    sb.draw(getTexture(), getX(), getY(),
                            getWidth(), getHeight());
                    break;
                case RIGHT: //right
                    sb.draw(getTexture(), getX(), getY(), getWidth() / 2, getHeight() / 2,
                            getWidth(), getHeight(), 1, 1, 270);
                    break;
                case DOWN: //down
                    sb.draw(getTexture(), getX(), getY(), getWidth() / 2, getHeight() / 2,
                            getWidth(), getHeight(), 1, 1, 180);
                    break;
                case LEFT: //left
                    sb.draw(getTexture(), getX(), getY(), getWidth() / 2, getHeight() / 2,
                            getWidth(), getHeight(), 1, 1, 90);
                    break;
            }
        }
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
}
