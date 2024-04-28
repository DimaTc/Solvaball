package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.logic.LevelManager;
import com.dima.solvaball.logic.PreferencesData;
import com.dima.solvaball.utils.AssetLoader;

import java.util.ArrayList;
import java.util.Collections;

public class LevelSelectState extends State {
    private final int BUTTONS_PER_PAGE = 25;
    private Component background;
    private ArrayList<Component> levelButtons;
    private AssetLoader assetLoader;
    private int maxPages, currentPage;
    private Button pageBackButton;
    private Button pageForwardButton;
    private Button returnButton;

    public LevelSelectState(GameStateManager gsm) {
        super(gsm);
        assetLoader = AssetLoader.getInstance();
        levelButtons = new ArrayList<>();

    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    void initComponents() {
        if (!assetLoader.loadAssets())
            return;
        levelButtons.clear();
        final ArrayList<Integer> levels = LevelManager.getInstance().getAllLevels();
        Collections.sort(levels);
        TextureRegion backgroundTexture = assetLoader.getTexture(AssetLoader.Asset.Background);
        final TextureRegion buttonTexture = assetLoader.getTexture(AssetLoader.Asset.SmallButton);
        TextureRegion backButtonTexture = assetLoader.getTexture(AssetLoader.Asset.BackButton);
        TextureRegion returnButtonTexture = assetLoader.getTexture(AssetLoader.Asset.ReturnButton);
        TextureRegion forwardButtonTexture = assetLoader.getTexture(AssetLoader.Asset.ForwardButton);
        initPages(levels.size());
        float buttonOffset = Gdx.graphics.getWidth() / 36f;
        float controlButtonRatio = forwardButtonTexture.getRegionHeight() / (float)
                forwardButtonTexture.getRegionWidth();
        float controlButtonWidth = Gdx.graphics.getWidth() / 8f;
        float controlButtonHeight = controlButtonRatio * controlButtonWidth;
        float returnButtonWidth = Gdx.graphics.getWidth() / 9f;
        background = new Component(backgroundTexture,
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ///////////////
        returnButton = new Button(returnButtonTexture, buttonOffset
                , Gdx.graphics.getHeight() - buttonOffset - returnButtonWidth,
                returnButtonWidth, returnButtonWidth);
        returnButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
            }
        });
        ///////////////
        pageBackButton = new Button(backButtonTexture, buttonOffset, buttonOffset * 4,
                controlButtonWidth, controlButtonHeight);
        pageBackButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                if (currentPage > 0)
                    loadLevelsPage(--currentPage, levels.get(levels.size() - 1), buttonTexture);

            }
        });
        ///////////////
        pageForwardButton = new Button(forwardButtonTexture, Gdx.graphics.getWidth() -
                controlButtonWidth - buttonOffset, buttonOffset * 4,
                controlButtonWidth, controlButtonHeight);
        pageForwardButton.setOnClickHandler(new ClickHandler() {
            @Override
            public void onClick(int x, int y) {
                if (currentPage < maxPages)
                    loadLevelsPage(++currentPage, levels.get(levels.size() - 1), buttonTexture);

            }
        });
        ///////////////


        loadLevelsPage(0, levels.get(levels.size() - 1), buttonTexture);

    }

    private void initPages(int levels) {
        currentPage = 0;
        maxPages = levels / BUTTONS_PER_PAGE;
    }

    private void loadLevelsPage(int page, int maxLevel, TextureRegion buttonTexture) {
        levelButtons.clear();
        pageForwardButton.setDisabled(page == maxPages);
        pageBackButton.setDisabled(page == 0);
        float ratio = buttonTexture.getRegionHeight() / (float) buttonTexture.getRegionWidth();
        float width = Gdx.graphics.getWidth() / 7.2f;
        float height = width * ratio;
        float offset = width / 3f;
        float maxHeight = Gdx.graphics.getHeight() / 1.8f;
        int maxItemsWidth = (int) (Gdx.graphics.getWidth() / (width + offset));

        for (int i = page * BUTTONS_PER_PAGE; i < (page + 1) * BUTTONS_PER_PAGE; i++) {
            if (i == maxLevel)
                break;
            int xOffset = (i - page * BUTTONS_PER_PAGE) % maxItemsWidth;
            float x = offset + (xOffset * (width + offset));
            float y = maxHeight + offset - ((float) ((i - page * BUTTONS_PER_PAGE) / maxItemsWidth) * (width + offset));
            Button button = new Button(buttonTexture, x, y, width, height);
            button.setText((i + 1) + "");
            button.setStringSize(Button.FontSize.LARGEST);
            if ((i + 1) > PreferencesData.getInstance().getLastLevel())
                button.setDisabled(true);
            final int levelNum = i + 1;

            button.setOnClickHandler(new ClickHandler() {
                @Override
                public void onClick(int x, int y) {
                    getGsm().setSelectedState(GameStateManager.GameState.PLAY, levelNum);
                }
            });
            levelButtons.add(button);
        }
    }

    @Override
    void init(int args) {
        try {
            initComponents();
            setStateLoaded(true);
        } catch (Exception e) {
            setStateLoaded(false);
            e.printStackTrace();
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch sb) {
        background.draw(sb);
        pageForwardButton.draw(sb);
        pageBackButton.draw(sb);
        returnButton.draw(sb);
        for (Component component : levelButtons)
            component.draw(sb);
    }

    @Override
    public void dispose() {
        for (Component component : levelButtons)
            component.dispose();
        pageForwardButton.dispose();
        pageBackButton.dispose();
        returnButton.dispose();
        background.dispose();
    }

    @Override
    public void fireClickEvent(int x, int y) {
        for (Component component : levelButtons)
            if (component.contains(x, y))
                component.fireClick(x, y);
        if (pageForwardButton.contains(x, y))
            pageForwardButton.fireClick(x, y);
        if (pageBackButton.contains(x, y))
            pageBackButton.fireClick(x, y);
        if (returnButton.contains(x, y))
            returnButton.fireClick(x, y);
    }

    @Override
    public void backPressed() {
        getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);

    }
}
