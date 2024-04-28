package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.logic.PlatformLogic;
import com.dima.solvaball.logic.SoundManager;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;

class OptionsState extends State {

    private AssetLoader assetLoader;

    private ArrayList<Component> components;

    private String[] options = new String[]{
            "Sound",
//            "Google",
            "Privacy"
    };

    public OptionsState(GameStateManager gsm) {
        super(gsm);
        components = new ArrayList<>();
        assetLoader = AssetLoader.getInstance();
    }

    @Override
    void initComponents() {
        if (!assetLoader.loadAssets())
            return;
        TextureRegion backgroundTexture = assetLoader.getTexture(AssetLoader.Asset.Background);
        TextureRegion returnButtonTexture = assetLoader.getTexture(AssetLoader.Asset.ReturnButton);
        TextureRegion wideButtonTexture = assetLoader.getTexture(AssetLoader.Asset.WideButton);
        float ratio = wideButtonTexture.getRegionHeight()
                / (float) wideButtonTexture.getRegionWidth();
        float buttonWidth = Gdx.graphics.getWidth() / 1.33f;
        float buttonHeight = buttonWidth * ratio;
        float buttonX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float buttonOffset = Gdx.graphics.getHeight() / 36f;
        //
        float returnWidth = Gdx.graphics.getWidth() / 9f;
        @SuppressWarnings("SuspiciousNameCombination") float returnHeight = returnWidth;
        //
        Component backgroundComponent = new Component(backgroundTexture, 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        components.add(0, backgroundComponent);
        Button returnButton = new Button(returnButtonTexture, Gdx.graphics.getWidth() / 36f
                , Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 36f - returnHeight,
                returnWidth, returnHeight);
        returnButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
            }
        });
        components.add(returnButton);

        for (int i = 0; i < Option.values().length; i++) {
            Option option = Option.values()[i];
            float buttonY = Gdx.graphics.getHeight() / 2f - (i * (buttonHeight + buttonOffset));
            final Button button = new Button(wideButtonTexture, buttonX, buttonY,
                    buttonWidth, buttonHeight);
            final String text = option.text;
            ClickHandler handler = null;
            switch (option) {
                case Sound:
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            SoundManager.getInstance().toggleActive();
                            button.setText(text.concat(": <" + (SoundManager.getInstance().isActive() ? "ON" : "OFF") + ">"));
                        }
                    };
                    button.setText(text.concat(": <" + (SoundManager.getInstance().isActive() ? "ON" : "OFF") + ">"));
                    break;
//                case GooglePlayService: (when fixed I'll add it back)
//                    button.setText(text);
//                    button.setOnClickHandler(new ClickHandler() {
//                        @Override
//                        public void onClick(int x, int y) {
//                            PlatformLogic.getInstance().getGoogleBridge().signIn();
//
//                        }
//                    });
                    //temp or not
//                    button.setDisabled(true);
                    //
//                    break;
                case Privacy:
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            PlatformLogic.getInstance().showPrivacyPolicy();
                        }
                    };
                    button.setText(text);
                    break;
            }
            if (handler != null)
                button.setOnClickHandler(handler);
            button.setStringSize(Button.FontSize.SMALLEST);

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
            e.printStackTrace(System.err);
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch sb) {
        for (Component component : components)
            component.draw(sb);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void fireClickEvent(int x, int y) {
        for (Component component : components)
            if (component.contains(x, y))
                component.fireClick(x, y);
    }

    @Override
    public void backPressed() {
        getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
    }

    private enum Option {
        Sound("Sound"),
//        GooglePlayService("Google Play"),
        Privacy("Privacy Policy");

        private final String text;

        Option(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
