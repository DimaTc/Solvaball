package com.dima.solvaball.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dima.solvaball.handlers.DialogActions;
import com.dima.solvaball.utils.AssetLoader;

public class DialogManager {

    private static DialogManager instance;
    private boolean active;
    private boolean alwaysOnTop = false;
    private BaseDialog dialog;
    private ShapeRenderer sr;
    private SpriteBatch sb;
    private TextureRegion dialogBody;
    private boolean isInit = false;

    private DialogManager() {
        sr = new ShapeRenderer();
        sb = new SpriteBatch();
    }

    public static DialogManager getInstance() {
        if (instance == null)
            instance = new DialogManager();
        return instance;

    }

    public boolean init() {
        if (isInit)
            return true;
        if (dialogBody == null)
            if (AssetLoader.getInstance().loadAssets())
                dialogBody = AssetLoader.getInstance().getTexture(AssetLoader.Asset.DialogBody);
            else
                return false;
        float ratio = dialogBody.getRegionHeight() / (float) dialogBody.getRegionWidth();
        float dialogWidth = Gdx.graphics.getWidth() / 1.1f;
        float dialogHeight = dialogWidth * ratio;
        float dialogX = Gdx.graphics.getWidth() / 2f - dialogWidth / 2f;
        float dialogY = Gdx.graphics.getHeight() / 2f - dialogHeight / 2f;
        dialog = new BaseDialog(dialogBody, dialogX, dialogY, dialogWidth, dialogHeight);
        isInit = true;
        return true;
    }

    public boolean isActive() {
        return dialog != null && dialog.isActive();
    }

    public void showConfirmDialog(String title, String det, DialogActions callback) {
        dialog.show(title, det, callback);
    }

    public void fireClickEvent(int x, int y) {
        if (dialog.isActive())
            if (dialog.contains(x, y))
                dialog.fireClick(x, y);
            else if (!alwaysOnTop)
                dialog.hide();
    }

    public void draw() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (dialog.isActive())
            dialog.fill(sr);
        sr.end();

        sb.begin();
        if (dialog.isActive())
            dialog.draw(sb);
        sb.end();
    }

    public void backPressed() {
        if (!alwaysOnTop)
            dialog.hide();
    }


    public void dispose() {
        dialog.dispose();
        sb.dispose();
        sr.dispose();
        instance = null;
    }

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

    public void hideAll() {
        if (dialog != null && !alwaysOnTop) //if more crashes occur, publish this fix (dialog != null)
            dialog.hide();
    }
}
