package com.dima.solvaball.gamestates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {

    private GameStateManager gsm;
    private boolean stateLoaded = false;

    public boolean isStateLoaded() {
        return stateLoaded;
    }

    public void setStateLoaded(boolean stateLoaded) {
        this.stateLoaded = stateLoaded;
    }

    public State(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public GameStateManager getGsm() {
        return gsm;
    }

    public void setGsm(GameStateManager gsm) {
        this.gsm = gsm;
    }

    abstract void initComponents();

    abstract void init(int args);

    public abstract void update();

    public abstract void draw(SpriteBatch sb);

    public abstract void dispose();

    public abstract void fireClickEvent(int x, int y);

    public abstract void backPressed();
}
