package com.dima.solvaball.logic;

import com.dima.solvaball.components.Ball;
import com.dima.solvaball.components.tiles.Tile;

public class TurnVersion {
    private Tile oldTile;
    private Tile newTile;
    private Ball eatenBall;
    private Tile eatenTile;
    private int originMoves;

    public TurnVersion(Tile oldTile, Tile newTile, Ball eatenBall) {
        originMoves = oldTile.getBall().getBallMoves();
        this.oldTile = oldTile;
        this.newTile = newTile;
        setEatenBall(eatenBall);
    }

    public int getOriginMoves() {
        return originMoves;
    }

    public Tile getOldTile() {
        return oldTile;
    }

    public Tile getNewTile() {
        return newTile;
    }

    public Ball getEatenBall() {
        return eatenBall;
    }

    public void setEatenBall(Ball eatenBall) {
        this.eatenBall = eatenBall;
        if (eatenBall != null)
            eatenTile = eatenBall.getParentTile();
    }

    public Tile getEatenTile() {
        return eatenTile;
    }
}
