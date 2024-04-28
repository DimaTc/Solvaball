package com.dima.solvaball.gamestates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShopState extends State {
    public ShopState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    void initComponents() {

    }

    @Override
    void init(int args) {
        setStateLoaded(true);

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void fireClickEvent(int x, int y) {

    }

    @Override
    public void backPressed() {
        getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);

    }
}
