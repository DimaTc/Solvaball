package com.dima.solvaball.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.handlers.DialogActions;
import com.dima.solvaball.logic.SoundManager;
import com.dima.solvaball.utils.AssetLoader;
import com.dima.solvaball.utils.FontGenerator;

public class BaseDialog extends Component {

    public boolean active;
    private Button yesButton;
    private Button noButton;
    private BitmapFont titleFont;
    private GlyphLayout titleLayout;
    private BitmapFont detailFont;
    private GlyphLayout detailLayout;
    private String titleString = "Are you sure?";//default
    private String detailString = "";//default
    private AssetLoader assetLoader;
    private DialogActions callback;

    public BaseDialog(TextureRegion textureRegion, float x, float y, float width, float height) {
        super(textureRegion, x, y, width, height);
        assetLoader = AssetLoader.getInstance();
        init();
    }

    public boolean isActive() {
        return active;
    }

    public void show(String title, String detailString, DialogActions callback) {
        this.active = true;
        this.titleString = title;
        this.detailString = detailString;
        this.callback = callback;
        titleLayout.setText(titleFont, titleString);
        detailLayout.setText(detailFont, detailString);
    }

    public void hide() {
        this.active = false;
    }


    private void init() {
        if (!assetLoader.loadAssets())
            return;
        TextureRegion buttonTexture = assetLoader.getTexture(AssetLoader.Asset.DialogButton);
        titleLayout = new GlyphLayout();
        detailLayout = new GlyphLayout();
        titleFont = FontGenerator.getInstance().getFontBySize((int) getWidth() / 13);
        detailFont = FontGenerator.getInstance().getFontBySize((int) getWidth() / 18);

        float buttonOffset = getWidth() / 10;
        float ratio = buttonTexture.getRegionHeight() / (float) buttonTexture.getRegionWidth();
        float buttonWidth = getWidth() / 3.3f;
        float buttonHeight = buttonWidth * ratio;
        yesButton = new Button(buttonTexture, buttonOffset + getX()
                , getY() + buttonOffset, buttonWidth, buttonHeight);
        noButton = new Button(buttonTexture, getX() + getWidth() - buttonWidth - buttonOffset,
                getY() + buttonOffset, buttonWidth, buttonHeight);

        yesButton.setText("Yes");

        noButton.setText("No");
        titleLayout.setText(titleFont, titleString);
        detailLayout.setText(detailFont, detailString);
        yesButton.setStringSize(Button.FontSize.LARGE);
        noButton.setStringSize(Button.FontSize.LARGE);
    }

    @Override
    public void fireClick(int x, int y) {
        if (yesButton.contains(x, y))
            callback.onResult(DialogResult.OK);
        else if (noButton.contains(x, y))
            callback.onResult(DialogResult.CANCELED);
        else return;
        SoundManager.getInstance().playSound(SoundManager.Sounds.CLICK);
    }

    @Override
    public void draw(SpriteBatch sb) {
        super.draw(sb);
        if (yesButton != null)
            yesButton.draw(sb);
        if (noButton != null)
            noButton.draw(sb);
        titleFont.draw(sb, titleString, getX() + getWidth() / 2f - titleLayout.width / 2f,
                getY() + getHeight() - titleLayout.height * 2f);
        detailFont.draw(sb, detailString, getX() + getWidth() / 2f - detailLayout.width / 2f,
                getY() + getHeight() - titleLayout.height * 5f);
    }

    public void fill(ShapeRenderer sr) {
        sr.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    public void dispose() {
        if (noButton != null)
            noButton.dispose();
        if (yesButton != null)
            yesButton.dispose();
        titleFont.dispose();
        detailFont.dispose();
    }
}
