package com.dima.solvaball.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.tiles.Tile;
import com.dima.solvaball.logic.GameLogic;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;

public class GameGrid extends Component {
    private int gridSize;
    private AssetLoader assetLoader = AssetLoader.getInstance();
    private ArrayList<Tile> tiles;

    public GameGrid(int gridSize, float x, float y, float width, float height) {
        super(null, x, y, width, height);
        this.gridSize = gridSize;
        tiles = new ArrayList<>();
        init();
    }


    public void init() {
        TextureRegion tileTexture = assetLoader.getTexture(AssetLoader.Asset.WoodTile);
        if(tiles.size() > 0)
            tiles.clear();
        if (tileTexture == null)
            return;
        for (int x = 0; x < gridSize; x++)
            for (int y = 0; y < gridSize; y++) {
                float tileWidth = getWidth() / gridSize;
                float tileHeight = getHeight() / gridSize;
                Tile tile = new Tile(tileTexture, getX() + x * tileWidth,
                        getY() + getHeight() - tileHeight - y * tileHeight, //reverse to be the same as level editor
                        tileWidth, tileHeight);
                tile.setIndexX(x);
                tile.setIndexY(y);
                tiles.add(tile);
                tile.setOnSelectHandler(GameLogic.getInstance());
            }
    }

    public Tile getTile(int indexX, int indexY) {
        for (Tile tile : tiles)
            if (tile.getIndexX() == indexX && tile.getIndexY() == indexY)
                return tile;
        return null;
    }

    @Override
    public void draw(SpriteBatch sb) {
        for (Tile tile : tiles)
            tile.draw(sb);
//        tiles.get(1).draw(sb);
    }

    @Override
    public void fireClick(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.contains(x, y))
                tile.fireClick(x, y);
        }
    }

    public ArrayList<Tile> getAllTiles() {
        return tiles;
    }


    public ArrayList<Tile> getAdjacentTiles(Tile tile) {
        ArrayList<Tile> adjacentTiles = new ArrayList<>();
        int sourceX = tile.getIndexX();
        int sourceY = tile.getIndexY();

        for (int x = sourceX - 1; x <= sourceX + 1; x++)
            for (int y = sourceY - 1; y <= sourceY + 1; y++) {
                if (x == sourceX && y == sourceY || x < 0 || y < 0
                        || y >= gridSize || x >= gridSize)
                    continue;
                if (x != sourceX && y != sourceY)
                    continue;
                adjacentTiles.add(getTile(x, y));
            }

        return adjacentTiles;
    }

    public void reset(int gridSize) {
        this.gridSize = gridSize;
        for (Tile tile : tiles)
            tile.reset();
        init();
    }

    public void reset() {
        reset(gridSize);
    }


    @Override
    public void dispose() {
        for (Tile tile : tiles)
            tile.dispose();
    }
}
