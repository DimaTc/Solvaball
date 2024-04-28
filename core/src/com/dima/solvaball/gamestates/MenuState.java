package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.dialogs.DialogManager;
import com.dima.solvaball.dialogs.DialogResult;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.handlers.DialogActions;
import com.dima.solvaball.logic.LevelManager;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;

public class MenuState extends State {


    private ArrayList<Component> components;

    private AssetLoader assetLoader = AssetLoader.getInstance();

    public MenuState(GameStateManager gsm) {
        super(gsm);
        components = new ArrayList<>();
        LevelManager.getInstance().loadLevel(1);
    }

    @Override
    void initComponents() {
        if (!assetLoader.loadAssets())
            return;
        Rectangle mainControlsBounds = new Rectangle();
        mainControlsBounds.setWidth(Gdx.graphics.getWidth() / 1.8f);
        mainControlsBounds.setHeight(Gdx.graphics.getHeight() / 1.42f);
        mainControlsBounds.setX(Gdx.graphics.getWidth() / 2f - mainControlsBounds.getWidth() / 2f);
        mainControlsBounds.setY(Gdx.graphics.getHeight() / 2f - mainControlsBounds.getHeight() / 2f);
        float buttonsOffset = Gdx.graphics.getWidth() / 36f;
        for (AssetLoader.Asset asset : AssetLoader.Asset.values()) {
            TextureRegion textureRegion = assetLoader.getTexture(asset);
            if (textureRegion == null) {
                Gdx.app.log("Error", asset.name());
                continue;
            }
            float ratio = textureRegion.getRegionHeight() / (float) textureRegion.getRegionWidth();
            float x, y;
            float width = -1;   //default is the original
            float height = -1;
            String text = "";
            float lastLevelY = Gdx.graphics.getHeight() / 2f - Gdx.graphics.getWidth()
                    / 4f * ratio / 2f; //anchor point
            int anchorOffset = Gdx.graphics.getHeight() / 20;
            ClickHandler listener = null;
            switch (asset) {
                case Background:
                    components.add(0, new Component(textureRegion, 0, 0,
                            Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
                    continue;

                case Title:
                    width = mainControlsBounds.getWidth();
                    height = width * ratio;
                    x = mainControlsBounds.getX();
                    y = mainControlsBounds.getHeight() + mainControlsBounds.getY() - height;
                    components.add(new Component(textureRegion, x, y, width, height));
                    continue;

                case InfoButton:
                    width = Gdx.graphics.getWidth() / 12f;
                    //noinspection SuspiciousNameCombination
                    height = width;
                    x = Gdx.graphics.getWidth() / 36f;
                    y = Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 36f - height;
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
//                            PlatformLogic.getInstance().getGoogleBridge().signIn();
                            Gdx.app.log("Log", "Info has been pressed");
                        }
                    };
                    continue;
                    //for now I will not show the info button
//                    break;

                case SettingsButton:
                    width = Gdx.graphics.getWidth() / 12f;
                    //noinspection SuspiciousNameCombination
                    height = width;
                    float offset = Gdx.graphics.getWidth() / 36f;
                    x = Gdx.graphics.getWidth() - offset - width;
                    y = Gdx.graphics.getHeight() - offset - height;
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            getGsm().setSelectedState(GameStateManager.GameState.OPTIONS, 0);
                        }
                    };
                    break;

                case LastLevelButton:
                    final int lastLevel = LevelManager.getInstance().getLastLevel();
                    width = mainControlsBounds.getWidth() - 2 * buttonsOffset;
                    height = width * ratio;
                    x = mainControlsBounds.getX() + buttonsOffset;
                    y = lastLevelY;
                    text = "" + lastLevel; //place holder - should be from level manager
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            getGsm().setSelectedState(GameStateManager.GameState.PLAY,
                                    lastLevel);
                        }
                    };
                    break;
                case AllLevelsButton:
                    width = mainControlsBounds.getWidth() - 2 * buttonsOffset;
                    height = width * ratio;
                    x = mainControlsBounds.getX() + buttonsOffset;
                    y = lastLevelY - anchorOffset - height;
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            getGsm().setSelectedState(GameStateManager.GameState.LEVELS, 0);
                        }
                    };
                    break;
                case AchievementsButton:
                    width = mainControlsBounds.getWidth() / 2 - 3 * buttonsOffset / 2;
                    height = width * ratio;
                    x = mainControlsBounds.getX() + buttonsOffset;
                    y = lastLevelY - 1.5f * anchorOffset - 2 * height;
                    //temp
                    continue;
                    //
//                    break;
                case ShopButton:
                    width = mainControlsBounds.getWidth() / 2 - 3 * buttonsOffset / 2;
                    height = width * ratio;
                    x = mainControlsBounds.getX() + 2 * buttonsOffset + width;
                    y = lastLevelY - 1.5f * anchorOffset - 2 * height;
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            //tmp
//                            getGsm().setSelectedState(GameStateManager.GameState.SHOP, 0);
                        }
                    };
                    //temp
                    continue;
//                    break;
                case WideButton:
                    width = mainControlsBounds.getWidth() - 2 * buttonsOffset;
                    height = width * ratio;
                    x = mainControlsBounds.getX() + buttonsOffset;
                    y = lastLevelY - 2.5f * anchorOffset - 2 * height;
                    text = "How To Play";
                    listener = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            getGsm().setSelectedState(GameStateManager.GameState.TUTORIAL, 0);
                        }
                    };
                    break;
                default:
                    continue;
            }
            Button button = new Button(textureRegion, x, y, width, height, text);
            if (listener != null)
                button.setOnClickHandler(listener);
            if (asset == AssetLoader.Asset.LastLevelButton)
                button.setStringY(button.getStringY() - button.getHeight() / 8f);
            if (asset == AssetLoader.Asset.WideButton)
                button.setStringSize(Button.FontSize.SMALL);
            components.add(button);
        }
        setStateLoaded(true);

    }

    @Override
    void init(int args) {
        try {
            components.clear();
            initComponents();
        } catch (Exception e) {
            setStateLoaded(false);
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!isStateLoaded())
            initComponents();
    }


    @Override
    public void draw(SpriteBatch sb) {
        for (Component component : components)
            component.draw(sb);
    }

    @Override
    public void dispose() {
        for (Component component : components)
            component.dispose();
    }

    @Override
    public void fireClickEvent(int x, int y) {
        for (Component component : components)
            if (component.contains(x, y))
                component.fireClick(x, y);
    }

    @Override
    public void backPressed() {
        DialogManager.getInstance().showConfirmDialog("Exit The Game?",
                "This will close the application", new DialogActions() {
                    @Override
                    public void onResult(DialogResult result) {
                        if (result == DialogResult.OK)
                            Gdx.app.exit();
                        else if (result == DialogResult.CANCELED)
                            DialogManager.getInstance().hideAll();
                    }
                });
    }
}
