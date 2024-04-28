package com.dima.solvaball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dima.solvaball.dialogs.DialogManager;
import com.dima.solvaball.gamestates.GameStateManager;
import com.dima.solvaball.handlers.AppAdapter;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.handlers.GameInput;
import com.dima.solvaball.logic.GameLogic;
import com.dima.solvaball.logic.LevelManager;
import com.dima.solvaball.logic.PlatformLogic;
import com.dima.solvaball.logic.SoundManager;
import com.dima.solvaball.utils.AssetLoader;
import com.dima.solvaball.utils.FontGenerator;

public class SolvaBallGame extends ApplicationAdapter {
    private static final String TAG = "Main";
    private SpriteBatch batch;
    private AssetLoader assetLoader;
    private Texture loadingScreen;
    private GameStateManager gsm;
    private DialogManager dialogManager;
    private boolean loaded = false;

    public SolvaBallGame(AppAdapter gameAdapter) {
        PlatformLogic.getInstance().setGameAdapter(gameAdapter);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        loadingScreen = new Texture(Gdx.files.internal("loading.png"));
        init();
//        initHeavy();
    }

//    private void initHeavy(){
//        assetLoader = AssetLoader.getInstance();
//        gsm = new GameStateManager();
//        gsm.init();
//
//    }

    private void init() {

        dialogManager = DialogManager.getInstance();
        GameInput gameInput = new GameInput();
        Gdx.input.setInputProcessor(gameInput);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        gameInput.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                if (dialogManager.isActive())
                    dialogManager.fireClickEvent(x, Gdx.graphics.getHeight() - y);
                else
                    gsm.fireClickEvent(x, Gdx.graphics.getHeight() - y);
            }
        });

        assetLoader = AssetLoader.getInstance();
        gsm = new GameStateManager();
        gsm.init();
    }

    @Override
    public void resume() {
        super.resume();
        assetLoader.onResume();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0x30 / 255f, 0x6C / 255f, 0xCA / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (!loaded) {
            batch.draw(loadingScreen, Gdx.graphics.getWidth() / 2f - loadingScreen.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f - loadingScreen.getHeight() / 2f);
        }
        if (assetLoader != null && gsm != null && dialogManager != null)
            if (assetLoader.loadAssets() && gsm.init() && dialogManager.init()) {
                loaded = true;
                update();
                gsm.draw(batch);
            }


//        if (dialogManager != null && dialogManager.isActive()) {
//            Gdx.gl.glClearColor(1, 0, 0, 0.8f);
//        }
        batch.end();
        if (dialogManager != null && dialogManager.isActive()) {
            //gl stuff for the dark background
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dialogManager.draw();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void update() {
        if (gsm == null)
            return;
//        if (!loaded)
//            if (gsm.getGameWidth() != Gdx.graphics.getWidth() || gsm.getGameHeight() != Gdx.graphics.getHeight()) {
//                gsm.setStateInitiated(false);
//                init();
//                return;
//            }
        gsm.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (dialogManager.isActive())
                dialogManager.backPressed();
            else
                gsm.backPressed();
        }
    }

    @Override
    public void dispose() {
        try {
            batch.dispose();
            LevelManager.getInstance().dispose();
            FontGenerator.getInstance().dispose();
            AssetLoader.getInstance().dispose();
            GameLogic.getInstance().dispose();
            SoundManager.getInstance().dispose();
            DialogManager.getInstance().dispose();
            gsm.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
