package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;

public class TutorialState extends State {

    private AssetLoader assetLoader;

    private Button returnToMenuButton;
    private Button nextButton;
    private Button backButton;
    private Component background;
    private ArrayList<Component> pages;
    private int currentPage = 0;
    private int maxPages = 3;

    public TutorialState(GameStateManager gsm) {
        super(gsm);
        pages = new ArrayList<>();
        assetLoader = AssetLoader.getInstance();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void initComponents() {
        if (!assetLoader.loadAssets())
            return;
        TextureRegion backgroundTexture = assetLoader.getTexture(AssetLoader.Asset.Background);
        TextureRegion returnButtonTexture = assetLoader.getTexture(AssetLoader.Asset.ReturnButton);
        final TextureRegion backButtonTexture = assetLoader.getTexture(AssetLoader.Asset.BackButton);
        TextureRegion forwardButtonTexture = assetLoader.getTexture(AssetLoader.Asset.ForwardButton);
        float controlRatio = forwardButtonTexture.getRegionHeight() / (float) forwardButtonTexture.getRegionWidth();
        float buttonOffset = Gdx.graphics.getWidth() / 36f;
        float returnWidth = Gdx.graphics.getWidth() / 9f;
        float controlWidth = Gdx.graphics.getWidth() / 8f;
        float controlHeight = controlWidth * controlRatio;
        background = new Component(backgroundTexture, 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        returnToMenuButton = new Button(returnButtonTexture, buttonOffset,
                Gdx.graphics.getHeight() - buttonOffset - returnWidth,
                returnWidth, returnWidth);

        nextButton = new Button(forwardButtonTexture,
                Gdx.graphics.getWidth() - 2 * buttonOffset - controlWidth,
                2 * buttonOffset, controlWidth, controlHeight);
        backButton = new Button(backButtonTexture, 2 * buttonOffset, 2 * buttonOffset,
                controlWidth, controlHeight);
        backButton.setDisabled(true);

        returnToMenuButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
            }
        });

        backButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                currentPage = currentPage <= 0 ? 0 : currentPage - 1;
                if (currentPage == 0)
                    backButton.setDisabled(true);
                if (nextButton.isDisabled())
                    nextButton.setDisabled(false);
            }
        });

        nextButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                currentPage = currentPage >= maxPages - 1 ? maxPages - 1 : currentPage + 1;
                if (currentPage == maxPages - 1)
                    nextButton.setDisabled(true);
                if (backButton.isDisabled())
                    backButton.setDisabled(false);
            }
        });

        for (int i = 1; i <= maxPages; i++) {
            AssetLoader.Asset pageAsset;
            try {
                pageAsset = AssetLoader.Asset.valueOf("TutorialPage" + i);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            TextureRegion pageTexture = assetLoader.getTexture(pageAsset);
            float ratio = pageTexture.getRegionHeight() / (float) pageTexture.getRegionWidth();
            float pageWidth = Gdx.graphics.getWidth();
            float pageHeight = pageWidth * ratio;   //should be ratio of 1
            float x = 0;
            float y = Gdx.graphics.getHeight() / 2f - pageHeight / 2f;
            pages.add(new Component(pageTexture, x, y, pageWidth, pageHeight));

        }


        setStateLoaded(true);
    }

    @Override
    public void init(int args) {
        initComponents();

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch sb) {
        background.draw(sb);
        pages.get(currentPage).draw(sb);
        returnToMenuButton.draw(sb);
        nextButton.draw(sb);
        backButton.draw(sb);

    }

    @Override
    public void dispose() {

    }

    @Override
    public void fireClickEvent(int x, int y) {
        if (returnToMenuButton.contains(x, y))
            returnToMenuButton.fireClick(x, y);
        else if (backButton.contains(x, y))
            backButton.fireClick(x, y);
        else if (nextButton.contains(x, y))
            nextButton.fireClick(x, y);
    }

    @Override
    public void backPressed() {
        this.getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
    }
}
