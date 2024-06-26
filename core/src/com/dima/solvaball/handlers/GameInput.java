package com.dima.solvaball.handlers;

import com.badlogic.gdx.InputProcessor;

public class GameInput implements InputProcessor {
    private ClickHandler clickHandler = null;
    private boolean pressedDown = false;

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!pressedDown) {
            pressedDown = true;
        } else
            return false;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pressedDown) {
            pressedDown = false;
            clickHandler.onClick(screenX, screenY);
        } else
            return false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setOnClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
}
