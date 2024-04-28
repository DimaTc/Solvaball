package com.dima.solvaball.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dima.solvaball.components.Button;
import com.dima.solvaball.components.Component;
import com.dima.solvaball.components.GameGrid;
import com.dima.solvaball.components.tiles.Tile;
import com.dima.solvaball.dialogs.DialogManager;
import com.dima.solvaball.dialogs.DialogResult;
import com.dima.solvaball.handlers.ClickHandler;
import com.dima.solvaball.handlers.DialogActions;
import com.dima.solvaball.handlers.GameListener;
import com.dima.solvaball.handlers.RewardCallback;
import com.dima.solvaball.logic.GameLogic;
import com.dima.solvaball.logic.LevelManager;
import com.dima.solvaball.logic.PlatformLogic;
import com.dima.solvaball.utils.AssetLoader;
import com.dima.solvaball.utils.FontGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

class PlayState extends State implements GameListener, RewardCallback {

    private GameGrid grid;
    private ArrayList<Button> buttons;
    private Component resetArrow;
    private BitmapFont levelFont;
    private BitmapFont infoFont;
    private TextureRegion background;
    private LevelManager levelManager;
    private GameLogic gameLogic;
    private String currentLevel = "Level ";
    private String ballsStatus = "";
    private GlyphLayout levelStringLayout;
    private GlyphLayout ballStatusLayout;
    private int animationCounter = 0;
    private int retries = 0;
    private int lostCount = 0;
    private float animationTiming = 0;
    private boolean won = false;
    private boolean rewardAdLoaded = false;
    private boolean lost = false;
    private boolean lostAnimationDone = false;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        levelManager = LevelManager.getInstance();
        buttons = new ArrayList<>();
        gameLogic = GameLogic.getInstance();
        gameLogic.setGameListener(this);
        levelStringLayout = new GlyphLayout();
        ballStatusLayout = new GlyphLayout();
        PlatformLogic.getInstance().getAdAdapter().setRewardCallback(this);

    }

    private void updateText() {
        levelFont = FontGenerator.getInstance().getFontBySize(Gdx.graphics.getWidth() / 10);
        infoFont = FontGenerator.getInstance().getFontBySize(Gdx.graphics.getWidth() / 13);
        if (grid == null)
            return;
        int endTileCount = 0;
        currentLevel = "Level " + LevelManager.getInstance().getCurrentLevel();
        for (Tile tile : grid.getAllTiles())
            if (tile.getTileType() == Tile.TileType.END)
                endTileCount++;
        ballsStatus = String.format(Locale.US, "Balls: %d/%d", gameLogic.getBalls().size(), endTileCount);
        ballStatusLayout.setText(infoFont, ballsStatus);
        if (gameLogic.getBalls().size() == endTileCount)
            infoFont.setColor(Color.GREEN);
        else
            infoFont.setColor(new Color(1f, 0.15f, 0f, 1f));

    }

    @Override
    void initComponents() {
        buttons.clear();
        updateText();
        retries = 0;
        lostCount = 0;
//        ballsStatus = gameLogic.getBalls().size() + "/";
        levelStringLayout.setText(levelFont, currentLevel);
        final float gridX, gridY, gridWidth, gridHeight;
        float buttonsOffset = Gdx.graphics.getWidth() / 12.7f;
        if (!AssetLoader.getInstance().loadAssets())
            return;
        if (Gdx.graphics.getWidth() < Gdx.graphics.getHeight()) { //if somehow the app will be used in landscape
            gridHeight = gridWidth = Gdx.graphics.getWidth();
            gridX = 0;
            gridY = Gdx.graphics.getHeight() / 2f - gridWidth / 2f;

        } else {
            gridWidth = gridHeight = Gdx.graphics.getHeight();
            gridY = 0;
            gridX = Gdx.graphics.getWidth() / 2f - gridHeight / 2f;
        }
        grid = new GameGrid(levelManager.getGridSize(), gridX, gridY, gridWidth, gridHeight);

        lost = false;
        lostAnimationDone = false;
        for (AssetLoader.Asset asset : AssetLoader.Asset.values()) {
            TextureRegion texture = AssetLoader.getInstance().getTexture(asset);
            float ratio = texture.getRegionHeight() / (float) texture.getRegionWidth();
            float h = Gdx.graphics.getHeight() / 18.64f;
            float w = h / ratio;
            float y = Gdx.graphics.getHeight() - h - buttonsOffset;
            float x;
            ClickHandler handler = null;
            switch (asset) {
                case MenuButton:
                    x = buttonsOffset;
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            returnToMenu();
                        }
                    };
                    break;
                case UndoButton:
                    x = Gdx.graphics.getWidth() - w - buttonsOffset;
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            gameLogic.undo();
                            lost = false;
                            lostAnimationDone = false;
                            updateText();
                        }
                    };
                    break;
                case ResetButton:
                    x = Gdx.graphics.getWidth() / 2f - w / 2f;
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            if (gameLogic.getTurnHistory() == null || gameLogic.getTurnHistory().size() == 0)
                                return;
                            PlatformLogic.getInstance().getFirebaseBridge().retryLevel(
                                    levelManager.getCurrentLevel(), retries
                            );
                            gameLogic.resetLevel();
                            retries++;
                            lost = false;
                            lostAnimationDone = false;
                            updateText();
                        }
                    };
                    break;
                case SkipButton:
                    w = Gdx.graphics.getWidth() / 8.44f;
                    h = ratio * w;
                    x = buttonsOffset / 2f;
                    y = grid.getY() - buttonsOffset / 2f - h;
                    handler = new ClickHandler() {
                        @Override
                        public void onClick(int x, int y) {
                            if (levelManager.getCurrentLevel() == levelManager.getAllLevels().size())
                                PlatformLogic.getInstance().getAdAdapter().showError("You're at the last level");
                            else if (rewardAdLoaded)
                                DialogManager.getInstance().showConfirmDialog("Skip the level?",
                                        "Watch an ad to skip", new DialogActions() {
                                            @Override
                                            public void onResult(DialogResult result) {
                                                if (result == DialogResult.OK) {
                                                    PlatformLogic.getInstance().getAdAdapter().showRewardAd();
                                                }
                                                DialogManager.getInstance().hideAll();

                                            }
                                        });
                            else
                                PlatformLogic.getInstance().getAdAdapter().showNoAds();
                        }
                    };
                    break;
                default:
                    continue;
            }
            if (asset == AssetLoader.Asset.ResetButton) {
                TextureRegion resetArrowTexture = AssetLoader.getInstance().getTexture(AssetLoader.Asset.ResetArrow);
                resetArrow = new Component(resetArrowTexture,
                        x + w, y - resetArrowTexture.getRegionHeight());
            }
            Button newButton = new Button(texture, x, y, w, h);
            newButton.setOnClickHandler(handler);
            buttons.add(newButton);

        }

        animationCounter = 0;
        animationTiming = 0;
        won = false;
        background = AssetLoader.getInstance().getTexture(AssetLoader.Asset.GameBackground);
        gameLogic.setGameGrid(grid);


    }

    @Override
    void init(int args) {
        try {
            initComponents();
            levelManager.loadLevel(args);
//            levelManager.updateTiles();
//            levelManager.updateBalls();
            if (!AssetLoader.getInstance().loadAssets())
                return;

            updateText();
            setStateLoaded(true);
        } catch (Exception e) {
            setStateLoaded(false);
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (won) {
            boolean finished = animateWinning();
            if (finished) {
                won = false;
                if (!levelManager.nextLevel()) {
                    if (checkLastLevel()) {
                        DialogManager.getInstance().setAlwaysOnTop(true);
                        DialogManager.getInstance().showConfirmDialog(
                                "Nice! You won the game",
                                "Would you like to rate the game?",
                                new DialogActions() {
                                    @Override
                                    public void onResult(DialogResult result) {
                                        if (result == DialogResult.OK)
                                            PlatformLogic.getInstance().showStorePage();
                                        getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
                                        DialogManager.getInstance().setAlwaysOnTop(false);
                                        DialogManager.getInstance().hideAll();

                                    }
                                }
                        );

                    }
                } else {
                    updateText();
                    retries = 0;
                }
            }

        }

        if (lost)
            if (!lostAnimationDone)
                lostAnimationDone = animateLosing();
    }

    private boolean checkLastLevel() {
//        int level = levelManager.getLastLevel();
        int level = levelManager.getCurrentLevel();
//                    int trueLevel = levelManager.getAllLevels().get(levelManager.getAllLevels().size() - 1);
        int trueLevel = Collections.max(levelManager.getAllLevels());
        return level >= trueLevel;
    }

    private boolean animateWinning() {
        if (animationCounter >= 6) {
            animationCounter = 0;
            return true;
        }
        animationTiming += Gdx.graphics.getDeltaTime();
        if (animationTiming >= 0.15) {
            animationCounter++;
            animationTiming = 0;
            for (Tile tile : grid.getAllTiles())
                if (tile.getTileType() == Tile.TileType.END)
                    if (tile.getExtraTile().getTexture() == AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile))
                        tile.getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTileOk));
                    else
                        tile.getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile));
        }
        return false;

    }

    private boolean animateLosing() {
        if (animationCounter >= 6) {
            animationCounter = 0;
            return true;
        }
        animationTiming += Gdx.graphics.getDeltaTime();
        if (animationTiming >= 0.15) {
            animationCounter++;
            animationTiming = 0;
            for (Tile tile : grid.getAllTiles())
                if (tile.getTileType() == Tile.TileType.END)
                    if (tile.getExtraTile().getTexture() == AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile))
                        tile.getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTileFail));
                    else
                        tile.getExtraTile().setTexture(AssetLoader.getInstance().getTexture(AssetLoader.Asset.EndTile));
        }
        return false;

    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        grid.draw(sb);
        for (Button button : buttons)
            button.draw(sb);
        levelFont.draw(sb, currentLevel, grid.getWidth() / 2f - levelStringLayout.width / 2
                , grid.getY() + levelStringLayout.height * 1.5f + grid.getHeight());
        infoFont.draw(sb, ballsStatus,
                grid.getWidth() / 2f - ballStatusLayout.width / 2,
                grid.getY() - ballStatusLayout.height);
        if (lost)
            resetArrow.draw(sb);

    }

    @Override
    public void dispose() {
        levelFont.dispose();
        infoFont.dispose();
        grid.dispose();
        for (Button button : buttons)
            button.dispose();

    }

    @Override
    public void fireClickEvent(int x, int y) {
        if (DialogManager.getInstance().isActive())
            return;
        if (!lost)
            grid.fireClick(x, y);
        for (Button button : buttons)
            if (button.contains(x, y))
                button.fireClick(x, y);
    }

    @Override
    public void backPressed() {
        returnToMenu();
    }

    @Override
    public void onWin() {
        //will be changed
        won = true;
        PlatformLogic.getInstance().getFirebaseBridge().wonLevel(levelManager.getCurrentLevel()
                , retries, lostCount);
        retries = 0;
        lostCount = 0;

    }

    private void returnToMenu() {
        DialogManager.getInstance().showConfirmDialog("Return to the menu?",
                "Level Progress will be lost",
                new DialogActions() {
                    @Override
                    public void onResult(DialogResult result) {
                        if (result == DialogResult.OK) {
                            gameLogic.resetLevel();
                            getGsm().setSelectedState(GameStateManager.GameState.MENU, 0);
                        }
                        DialogManager.getInstance().hideAll();
                    }
                });
    }


    @Override
    public void onNoMoreMoves() {
        lost = true;
        lostCount++;
        PlatformLogic.getInstance().getFirebaseBridge().lostLevel(levelManager.getCurrentLevel());

    }

    @Override
    public void ballEaten() {
        updateText();
    }

    @Override
    public void onRewardCompleted() {
        PlatformLogic.getInstance().
                getFirebaseBridge().logSkippedLevel(levelManager.getCurrentLevel(),
                retries, lostCount);
        gameLogic.skipLevel();
        updateText();
        retries = 0;
        lostCount = 0;
    }

    @Override
    public void onRewardFailed() {
        this.rewardAdLoaded = false;
    }

    @Override
    public void rewardIsLoaded(boolean loaded) {
        this.rewardAdLoaded = loaded;
    }
}
