package com.dima.solvaball.components.tiles;

import com.dima.solvaball.utils.AssetLoader;

public class Power extends Tile {
    private Tile parentTile;

    public Power(Tile tile) {
        super(AssetLoader.getInstance().getTexture(AssetLoader.Asset.PowerTile), tile.getX(), tile.getY(),
                tile.getWidth(), tile.getHeight());
        setIndexX(tile.getIndexX());
        setIndexY(tile.getIndexY());
        this.parentTile = tile;
    }

    public void init() {
        setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.PowerTile));
    }
}
