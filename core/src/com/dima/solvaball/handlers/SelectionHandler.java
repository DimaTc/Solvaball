package com.dima.solvaball.handlers;

import com.dima.solvaball.components.Ball;
import com.dima.solvaball.components.tiles.Tile;

public interface SelectionHandler {
    void onTileSelected(Tile tile);

    void onBallSelected(Ball ball);
}
