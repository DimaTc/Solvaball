package com.dima.solvaball.components.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dima.solvaball.utils.AssetLoader;

public class Sand extends Tile {
    private Tile parentTile;

    public Sand(Tile tile) {
        super(AssetLoader.getInstance().getTexture(AssetLoader.Asset.SandTile), tile.getX(), tile.getY(),
                tile.getWidth(), tile.getHeight());

        setIndexX(tile.getIndexX());
        setIndexY(tile.getIndexY());
        this.parentTile = tile;
    }

    public void init() {
        setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.SandTile));
    }

    @Override
    public void draw(SpriteBatch sb) {
        super.draw(sb);

    }
}
