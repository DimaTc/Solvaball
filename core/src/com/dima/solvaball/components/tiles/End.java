package com.dima.solvaball.components.tiles;

import com.dima.solvaball.components.Ball;
import com.dima.solvaball.utils.AssetLoader;

public class End extends Tile {
    private Tile parentTile;

    public End(Tile tile) {
        super(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile), tile.getX(), tile.getY(),
                tile.getWidth(), tile.getHeight());
        setIndexX(tile.getIndexX());
        setIndexY(tile.getIndexY());
        this.parentTile = tile;
    }


    public void init() {
        setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile));
    }
}
