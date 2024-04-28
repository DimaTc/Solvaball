package com.dima.solvaball.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.dima.solvaball.components.Ball;
import com.dima.solvaball.components.tiles.Tile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class LevelManager {
    private static LevelManager instance;
    private GameLogic gameLogic;
    private int lastLevel = 1;
    private int currentLevel = 1;
    private HashMap<String, String> levelData;

    private LevelManager() {
        gameLogic = GameLogic.getInstance();
        levelData = new HashMap<>();
    }

    public static LevelManager getInstance() {
        if (instance == null)
            instance = new LevelManager();
        return instance;
    }

    public int getLastLevel() {
        lastLevel = PreferencesData.getInstance().getLastLevel();
        if (lastLevel > getAllLevels().size()) {
            PreferencesData.getInstance().saveLastLevel(getAllLevels().size());
            return getAllLevels().size();
        }
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getGridSize() {
        return Integer.valueOf(levelData.get(Key.SIZE.key));
    }

    public void updateTiles() {
        if (GameLogic.getInstance().getGameGrid() == null)
            return;
        String rawTiles = levelData.get(Key.TILE.key);
        String[] rawTilesArray = rawTiles.split(",");
        for (String rawTile : rawTilesArray) {
            String[] tmpSplit = rawTile.split(" ");
            String type = tmpSplit[0];
            int[] values = new int[tmpSplit.length - 1];
            for (int i = 0; i < values.length; i++)
                values[i] = Integer.valueOf(tmpSplit[i + 1]);
            int indexX = values[0];
            int indexY = values[1];
            int[] args = Arrays.copyOfRange(values, 2, values.length);
            Tile tile = GameLogic.getInstance().getGameGrid().getTile(indexX, indexY);
            if (tile == null)
                continue;
            switch (type) {
                case "e":
                    tile.setTileType(Tile.TileType.END, args);
                    break;
                case "i":
                    tile.setTileType(Tile.TileType.ICE, args);
                    break;
                case "s":
                    tile.setTileType(Tile.TileType.SAND, args);
                    break;
                case "b":
                    tile.setTileType(Tile.TileType.POWER, args);
                    break;
                case "w":
                    tile.setTileType(Tile.TileType.WALL, args);
                    break;

            }

        }
    }

    public void updateBalls() { //later maybe will be entities
        if (GameLogic.getInstance().getGameGrid() == null)
            return;
        GameLogic.getInstance().resetBalls();
        String rawBalls = levelData.get(Key.ENTITIES.key);
        String[] rawBallsArray = rawBalls.split(",");
        for (String rawBall : rawBallsArray) {
            String[] tmpSplit = rawBall.split(" ");
            String type = tmpSplit[0];
            if (!type.equals("b"))
                continue;
            int[] values = new int[tmpSplit.length - 1];
            for (int i = 0; i < values.length; i++)
                values[i] = Integer.valueOf(tmpSplit[i + 1]);
            Tile tile = GameLogic.getInstance().getGameGrid().getTile(values[0], values[1]);
            if (tile == null)
                continue;
            int moves = 0;
            if (values.length > 2)
                moves = values[2];
            Ball ball = new Ball(tile, moves);
            ball.setOnSelectHandler(gameLogic);
            ball.init();
            GameLogic.getInstance().getBalls().add(ball);
            tile.setBall(ball);

        }
    }

    public boolean loadLevel(int level) {
        currentLevel = level;
        FileHandle fileHandle = Gdx.files.internal(String.format(Locale.US, "levels/level%d.dat", level));
//        FileHandle fileHandle = Gdx.files.internal("levels/test2.dat");
        if (!fileHandle.exists())
            return false;
        levelData = parseFile(fileHandle);
        if(gameLogic.getGameGrid() != null)
        {
            gameLogic.getGameGrid().reset(getGridSize());
        }
        updateTiles();
        updateBalls();
        return true;
    }

    private HashMap<String, String> parseFile(FileHandle file) {
        HashMap<String, String> rawData = new HashMap<>();
        InputStream stream = file.read();
        String rawString = "";
        try {
            int byteData = stream.read();
            while (byteData != -1) {
                rawString = rawString.concat(String.valueOf((char) byteData));
                byteData = stream.read();
            }
            if (rawString.length() == 0)
                return null;

            String[] lines = rawString.split("\n");
            for (String line : lines) {
                String[] tmpSplit = line.split(":");
                String key = tmpSplit[0];
                String values = tmpSplit[1];
                rawData.put(key, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

    public boolean nextLevel() {
        int savedLevel = PreferencesData.getInstance().getLastLevel();
        boolean res = loadLevel(currentLevel + 1);
        if (currentLevel > savedLevel && res)
            PreferencesData.getInstance().saveLastLevel(currentLevel);
        lastLevel = currentLevel;
        gameLogic.resetLevel();
        return res;
    }

    public ArrayList<Integer> getAllLevels() {
        ArrayList<Integer> levels = new ArrayList<>();
        FileHandle[] files = Gdx.files.internal("levels").list();
        if (files == null)
            return null;
        for (FileHandle file : files) {
            String tmpStr = file.name().replace("level", "");
            tmpStr = tmpStr.replace(".dat", "");
            levels.add(Integer.parseInt(tmpStr));

        }
        return levels;
    }

    public void dispose() {
        instance = null;
    }

    public enum Key {
        SIZE("s"),
        TILE("t"),
        ENTITIES("e");

        private final String key;

        Key(String key) {
            this.key = key;
        }
    }
}
