package com.dima.solvaball.logic;

import com.dima.solvaball.components.Ball;
import com.dima.solvaball.components.GameGrid;
import com.dima.solvaball.components.tiles.Tile;
import com.dima.solvaball.components.tiles.Wall;
import com.dima.solvaball.handlers.GameListener;
import com.dima.solvaball.handlers.SelectionHandler;

import java.util.ArrayList;
import java.util.Stack;

public class GameLogic implements SelectionHandler {
    private static GameLogic instance;

    private GameGrid gameGrid;
    private ArrayList<Ball> balls;
    private Ball selectedBall = null;
    private GameListener gameListener;
    private Stack<TurnVersion> turnHistory;
    private SoundManager soundManager;

    private GameLogic() {
        balls = new ArrayList<>();
        turnHistory = new Stack<>();
        soundManager = SoundManager.getInstance();
    }

    public static GameLogic getInstance() {
        if (instance == null)
            instance = new GameLogic();
        return instance;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }

    public void setGameGrid(GameGrid gameGrid) {
        this.gameGrid = gameGrid;
    }

    public void resetBalls() {
//        for (Ball ball : balls)
//            gameGrid.getTile(ball.getIndexX(), ball.getIndexY()).removeBall();
        balls.clear();
    }

    public void deselectAll() {
        for (Tile tile : gameGrid.getAllTiles()) {
            tile.setSuggested(false);
            tile.setSelected(false);
            if (tile.getBall() != null)
                tile.getBall().setSelected(false);
        }
        selectedBall = null;
    }

    @Override
    public void onTileSelected(Tile tile) {
        if (!tile.isSuggested() || selectedBall == null) {
            deselectAll();
            return;
        }
        if (tile.getTileType() == Tile.TileType.ICE)
            soundManager.playSound(SoundManager.Sounds.ICE_TILE);
        Tile actualTile = moveSelectedBall(tile);
        boolean win = false;
        if (actualTile.getTileType() != null)
            switch (actualTile.getTileType()) {
                case END:
                    win = checkWin();
                    soundManager.playSound(SoundManager.Sounds.TILE);

                    if (win) {
                        gameListener.onWin();
                    }
                    break;
                case SAND:
                    soundManager.playSound(SoundManager.Sounds.SAND_TILE);
                    selectedBall.setBallMoves(selectedBall.getBallMoves() - 1);
                    break;
                case POWER:
                    soundManager.playSound(SoundManager.Sounds.POWER_TILE);
                    selectedBall.setBallMoves(selectedBall.getBallMoves() + 1);
                    break;
                case WALL:
                    soundManager.playSound(SoundManager.Sounds.TILE);
            }
        else
            soundManager.playSound(SoundManager.Sounds.TILE);
        deselectAll();
        if (!win)
            if (checkLose())
                gameListener.onNoMoreMoves();

    }


    private Tile moveSelectedBall(Tile dest) {
        return moveSelectedBall(dest, false);
    }

    private Tile moveSelectedBall(Tile dest, boolean history) {
        Tile srcTile = selectedBall.getParentTile();
        int dx = dest.getIndexX() - srcTile.getIndexX();
        int dy = dest.getIndexY() - srcTile.getIndexY();
        dx = dx == 0 ? 0 : dx / Math.abs(dx);
        dy = dy == 0 ? 0 : dy / Math.abs(dy);
        while (dest.getTileType() == Tile.TileType.ICE && !history) {
            Tile tmpTile = gameGrid.getTile(dest.getIndexX() + dx, dest.getIndexY() + dy);
            if (isTileOk(tmpTile, srcTile))
                dest = tmpTile;
            else
                break;
        }
        TurnVersion step = new TurnVersion(srcTile, dest, null);
        int distance = srcTile.getDistance(dest);
        if (Math.abs(distance) > 1) {
            Tile tileInBetween;

            int direction = Math.abs(distance) / (distance);
            if (srcTile.getIndexX() == dest.getIndexX())
                tileInBetween = gameGrid.getTile(srcTile.getIndexX(), srcTile.getIndexY() + direction);
            else
                tileInBetween = gameGrid.getTile(srcTile.getIndexX() + direction, srcTile.getIndexY());
            if (tileInBetween != null) {
                step.setEatenBall(tileInBetween.getBall());
                balls.remove(tileInBetween.getBall());
                gameListener.ballEaten();
                tileInBetween.removeBall();
            }
        }
        turnHistory.push(step);
        selectedBall.getParentTile().setBall(null);
        selectedBall.setParentTile(null);
        dest.setBall(selectedBall);
        selectedBall.setBallMoves(selectedBall.getBallMoves() - 1);
        return dest;
//        selectedBall = null;

    }

    @Override
    public void onBallSelected(Ball ball) {
        if (ball.isSelected()) {
            ball.setSelected(false);
            deselectAll();
            return;
        }
        soundManager.playSound(SoundManager.Sounds.BALL_CLICK);
        deselectAll();
        ball.setSelected(true);
        if (ball.getBallMoves() <= 0)
            return;
        int sourceX = ball.getIndexX();
        int sourceY = ball.getIndexY();
        Tile sourceTile = gameGrid.getTile(sourceX, sourceY);
        ArrayList<Tile> suggestedTiles
                = logicFilterTiles(gameGrid.getAdjacentTiles(sourceTile),
                sourceTile);
        sourceTile.setSelected(true);
        selectedBall = ball;
        for (Tile tile : suggestedTiles
        ) {
            tile.setSuggested(true);
        }
        //logic get tile and etc

    }

    private ArrayList<Tile> logicFilterTiles(ArrayList<Tile> tiles, Tile sourceTile) {
        ArrayList<Tile> newTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            int dx = tile.getIndexX() - sourceTile.getIndexX();
            int dy = tile.getIndexY() - sourceTile.getIndexY();
            if (isTileOk(tile, sourceTile))
                newTiles.add(tile);
            if (tile.getBall() != null && !isWallBlocked(tile, sourceTile) &&
                    !isWallBlocked(sourceTile, tile)) {
                int nextX = tile.getIndexX() + dx;
                int nextY = tile.getIndexY() + dy;
                Tile nextTile = gameGrid.getTile(nextX, nextY);
                if (isTileOk(nextTile, sourceTile) && !isWallBlocked(tile, nextTile))
                    newTiles.add(nextTile);
            }


        }
        return newTiles;
    }

    private boolean checkWin() {
        ArrayList<Tile> endTiles = new ArrayList<>();
        for (Tile tile : gameGrid.getAllTiles())
            if (tile.getTileType() == Tile.TileType.END) {
                if (tile.getBall() == null)
                    return false;
                endTiles.add(tile);
            }
        return balls.size() == endTiles.size();
    }

    private boolean checkLose() {
        ArrayList<Tile> endTiles = new ArrayList<>();
        boolean endTilesOk = true;
        for (Tile tile : gameGrid.getAllTiles())
            if (tile.getTileType() == Tile.TileType.END) {
                if (tile.getBall() == null)
                    endTilesOk = false;
                endTiles.add(tile);
            }
        if (balls.size() < endTiles.size())
            return true;
        for (Ball ball : balls)
            if (ball.getBallMoves() != 0)
                return false;

        if (balls.size() == endTiles.size())
            return !endTilesOk;
        return true;
    }

    private boolean isTileOk(Tile targetTile, Tile sourceTile) {
        if (targetTile == null)
            return false;
        if (targetTile.getBall() != null)
            return false;
        if (isWallBlocked(targetTile, sourceTile) || isWallBlocked(sourceTile, targetTile))
            return false;

        return true;
    }

    public void resetLevel() {
        deselectAll();
        turnHistory.clear();
        gameGrid.reset();
        LevelManager levelManager = LevelManager.getInstance();
        levelManager.loadLevel(levelManager.getCurrentLevel()); //current Level
//        levelManager.updateTiles(); //current Level
//        levelManager.updateBalls(); //current Level

    }

    private boolean isWallBlocked(Tile targetTile, Tile sourceTile) {
        if (targetTile.getExtraTile() instanceof Wall) {
            int dx = targetTile.getIndexX() - sourceTile.getIndexX();
            int dy = targetTile.getIndexY() - sourceTile.getIndexY();
            for (Wall.Direction direction : ((Wall) targetTile.getExtraTile()).getDirections())
                switch (direction) {
                    case UP:
                        if (dy > 0)
                            return true;
                        break;
                    case DOWN:
                        if (dy < 0)
                            return true;
                        break;
                    case LEFT:
                        if (dx > 0)
                            return true;
                        break;
                    case RIGHT:
                        if (dx < 0)
                            return true;
                        break;
                }
        }
        return false;
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public void undo() {
        if (turnHistory.size() == 0)
            return;
        TurnVersion lastStep = turnHistory.pop();
        selectedBall = lastStep.getNewTile().getBall();
        moveSelectedBall(lastStep.getOldTile(), true);
        selectedBall.setBallMoves(lastStep.getOriginMoves());
        if (lastStep.getEatenBall() != null) {
            Tile eatenTile = lastStep.getEatenTile();
            eatenTile.setBall(lastStep.getEatenBall());
            balls.add(lastStep.getEatenBall());
        }
        deselectAll();

        turnHistory.pop(); //pop the extra turn from the move function
    }

    public void dispose() {
        instance = null;
    }

    public Stack<TurnVersion> getTurnHistory() {
        return turnHistory;
    }

    public void skipLevel() {
        LevelManager.getInstance().nextLevel();
    }
}
