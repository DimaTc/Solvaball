package com.dima.solvaball.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    public static AssetLoader instance;
    private final String GAME_ASSETS = "GameAssets.atlas";
    private final String UTIL_ASSETS = "UtilAssets.atlas";
    //Tmp
    private AssetManager assetManager;
    private Texture loadingScreen;

    public AssetLoader() {
        assetManager = new AssetManager();
        assetManager.load(GAME_ASSETS, TextureAtlas.class);
        assetManager.load(UTIL_ASSETS, TextureAtlas.class);
        Texture.setAssetManager(assetManager);
    }

    public static AssetLoader getInstance() {
        if (instance == null)
            instance = new AssetLoader();
        return instance;
    }

    public Texture getLoadingScreen() {
        return loadingScreen;
    }

    public boolean loadAssets() {
        return assetManager.update();

    }

    public TextureRegion getTexture(Asset asset) {
        TextureRegion res = null;
        try {
            String assetName = asset.name();
            TextureAtlas atlas = assetManager.get(GAME_ASSETS);
            res = atlas.findRegion(assetName);
            if (res == null) {
                atlas = assetManager.get(UTIL_ASSETS);
                res = atlas.findRegion(assetName);
                if (res == null)
                    return null;
            }
        } catch (Exception e) {
            Gdx.app.log("Assets", e.getMessage());
        }
        return res;
    }

    public void dispose() {
        assetManager.dispose();
        instance = null;
        try {
            loadingScreen.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        assetManager.update();
    }

    public enum Asset {
        Ball,
        BallEmpty,
        EndTile,
        EndTileOk,
        EndTileFail,
        IceTile,
        PowerTile,
        SandTile,
        WallTile,
        WoodTile,
        AchievementsButton,
        AllLevelsButton,
        BackButton,
        Background,
        GameBackground,
        UndoButton,
        InfoButton,
        LastLevelButton,
        MenuButton,
        ResetButton,
        ReturnButton,
        ForwardButton,
        SettingsButton,
        ShopButton,
        SmallButton,
        Title,
        WideButton,
        DialogButton,
        DialogBody,
        TutorialPage1,
        TutorialPage2,
        TutorialPage3,
        ResetArrow,
        SkipButton



    }


}
