package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dima.solvaball.logic.LevelManager;
import com.dima.solvaball.logic.PlatformLogic;

import java.util.HashMap;

public class GameStateManager {

    private HashMap<GameState, State> gameStates;
    private GameState currentState = GameState.MENU;
    private boolean stateInitiated = false;
    private float gameWidth = 0;
    private float gameHeight = 0;

    public GameStateManager() {
        this.gameStates = new HashMap<>();
    }

    public boolean isStateInitiated() {
        return stateInitiated;
    }

    public void setStateInitiated(boolean stateInitiated) {
        this.stateInitiated = stateInitiated;
    }

    public float getGameWidth() {
        return gameWidth;
    }


    public float getGameHeight() {
        return gameHeight;
    }

    private void initStates() {
        gameStates.put(GameState.MENU, new MenuState(this));
        gameStates.put(GameState.OPTIONS, new OptionsState(this));
        gameStates.put(GameState.PLAY, new PlayState(this));
        gameStates.put(GameState.TUTORIAL, new TutorialState(this));
        gameStates.put(GameState.LEVELS, new LevelSelectState(this));
        gameStates.put(GameState.SHOP, new ShopState(this));
    }

    public void update() {
        gameStates.get(currentState).update();

    }

    public void draw(SpriteBatch sb) {
        gameStates.get(currentState).draw(sb);
    }

    public void setSelectedState(GameState state, int args) {
        this.currentState = state;
        this.gameStates.get(currentState).init(args);
//        if (state == GameState.PLAY)
//            PlatformLogic.getInstance().getAdAdapter().showBannerAd();
//        else
//            PlatformLogic.getInstance().getAdAdapter().hideBannerAd();

    }

    public GameState getCurrentState() {
        return currentState;

    }

    public boolean init() {
        if (stateInitiated)
            return true;
        this.gameWidth = Gdx.graphics.getWidth();
        this.gameHeight = Gdx.graphics.getHeight();
        initStates();
        for (State state : gameStates.values()) {
            if (state instanceof PlayState)
                state.init(LevelManager.getInstance().getLastLevel());
            else
                state.init(0);
        }

        for (State state : gameStates.values()) {
            if (!state.isStateLoaded())
                return false;
        }
        stateInitiated = true;
        return true;
    }

    public void fireClickEvent(int x, int y) {
        gameStates.get(currentState).fireClickEvent(x, y);

    }

    public void dispose() {
        for (State state : gameStates.values())
            state.dispose();
    }

    public void backPressed() {
        gameStates.get(currentState).backPressed();
    }

    public enum GameState {
        MENU,
        PLAY,
        OPTIONS,
        TUTORIAL,
        LEVELS,
        SHOP
    }
}
