package io.github.flipply_fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    //  Game Resources
    private Texture reefImage;
    private Sprite[] topReefs;
    private Sprite[] bottomReefs;


    private Sound bounce;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Player player;
    private boolean gameOver;
    private boolean gameStart;
    private Stage stage;
    private Table table;
    private Skin startBtnSkin, retryBtnSkin;
    private Button startBtn, retryBtn;
    private Texture background1, background2;
    private float backgroundVelocity;
    private float backgroundX;

    private int numReefs = 5;
    private float reefSpace = 2.5f;
    private float  reefGap =3f;
    private float gapShrinkRate = 0.01f;
    private float minReefGap = 1.5f;



    //  Game resource filenames
    private String reefImageFile = "rectangle.png";
    private float reefSpeed = 2f;      // Speed of the reef moving left
    private float reefSpawnX = Settings.worldWidth;  // Starting X position of the reef (off-screen)
    private float reefY = 2f;          // Y position of the reef (can be randomized)



    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(Settings.worldWidth, Settings.worldHeight);

        //  Set stage for UI popups when needed
        stage  = new Stage(viewport);
        startBtnSkin = new Skin(Gdx.files.internal(Settings.startBtnSkinFilePath));
        retryBtnSkin = new Skin(Gdx.files.internal(Settings.retryBtnSkinFilePath));
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        //  Add listener on stage
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameStart && !gameOver) table.clearChildren();
                else if (gameOver) {
                    table.clearChildren();
                    resetGame();
                }
            }
        });

        //  Create reuseable Start adn Retry buttons
        startBtn = new Button(startBtnSkin);
        retryBtn = new Button(retryBtnSkin);

        //  Reset game
        resetGame();

        //  Setup infinitely moving background
        background1 = new Texture(Settings.backgroundFilePath);
        background2 = new Texture(Settings.backgroundFilePath);


        //  COLLISION branch
        reefImage = new Texture(reefImageFile);

        bottomReefs = new Sprite[numReefs];
        topReefs = new Sprite[numReefs];

        for (int i=0; i<numReefs;i++){
            bottomReefs[i]=new Sprite(reefImage);
            topReefs[i] = new Sprite(reefImage);


            bottomReefs[i].setSize(0.5f,2f);
            topReefs[i].setSize(0.5f,2f);

            topReefs[i].flip(false, true);

            float xPosition = Settings.worldWidth + i * reefSpace;
            resetReefPair(i, xPosition);

        }


    }

    /**
     * resize()
     * Desc:    Adjust the viewport whenever the game window gets resized
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        //  Wait for player input before starting
        drawScreen();
        gameStart = player.startGame(gameStart);

        //  Run main game loop if player has not died and player started the game
        if (!gameOver && gameStart) {
            player.updatePos();
            if (player.hasDied()) {
                gameOver = true;
                gameStart = false;
                table.add(retryBtn).center().width(3).height(3);
                backgroundX = backgroundVelocity = 0;
            }




            for (int i = 0; i < numReefs; i++) {
                // Move reefs to the left
                bottomReefs[i].translateX(-reefSpeed * delta);
                topReefs[i].translateX(-reefSpeed * delta);

                // Reset reef pair if it goes off-screen
                if (bottomReefs[i].getX() + bottomReefs[i].getWidth() < 0) {
                    float xPosition = bottomReefs[(i + numReefs - 1) % numReefs].getX() + reefSpace;
                    resetReefPair(i, xPosition);
                }
            }
            reefGap = Math.max(minReefGap, reefGap -gapShrinkRate *delta);
        }
    }

    private void drawScreen() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //  Draw sprites and background within this block
        batch.begin();
        batch.draw(background1, backgroundX, 0, Settings.worldWidth, Settings.worldHeight);
        batch.draw(background2, backgroundX + Settings.worldWidth, 0, Settings.worldWidth, Settings.worldHeight);
        player.draw(batch);

        for(int i=0;i<numReefs;i++){

            bottomReefs[i].draw(batch);
            topReefs[i].draw(batch);
        }


        batch.end();

        // Update and draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        backgroundX -= backgroundVelocity;
        if ((backgroundX + Settings.worldWidth) <= 0) {
            backgroundX = 0;
        }
    }

    private void resetReefPair(int index, float xPosition) {

        bottomReefs[index].setPosition(xPosition, 0.5f);

        // Randomize top reef position based on the shrinking gap
        float topReefY = bottomReefs[index].getHeight() + reefGap;
        topReefs[index].setPosition(xPosition, topReefY);
    }


    private void resetGame() {
        table.add(startBtn).center().width(3).height(3);
        gameOver = false;
        gameStart = false;  //  Becomes true once player taps the screen
        player = new Player(Settings.playerSpriteFilePath);

        //  Setup infinitely moving background
        backgroundX = 0;
        backgroundVelocity = 0.01f;
    }
}
