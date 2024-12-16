package io.github.flipply_fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    //  Game Resources
    private Sound bounce;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Player player;
    private boolean gameOver;
    private boolean gameStart;
    private Stage stage;
    private Skin menuSkin;
    private Table popup;
    private Camera camera;


    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Settings.worldWidth, Settings.worldHeight, camera);
        gameOver = false;
        gameStart = false;  //  Becomes true once player taps the screen
        player = new Player(Settings.playerSpriteFilePath);

        //  Set stage for UI popups when needed
        stage  = new Stage(viewport);
        menuSkin = new Skin(Gdx.files.internal(Settings.menuSkinFilePath));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button button = new Button(menuSkin);
        table.add(button).center().width(3).height(3);
        // Set the stage as input processor
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Remove the popup when the user clicks anywhere
                button.remove();
            }
        });
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
        //  Wait for player input before starting
        drawScreen();
        gameStart = player.startGame(gameStart);

        //  Run main game loop if player has not died and player started the game
        if (!gameOver && gameStart){
            player.updatePos();
            if (player.hasDied()) gameOver = true;
        }
    }

    private void drawScreen() {
        ScreenUtils.clear(Color.BLACK);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //  Draw sprites within this block
        batch.begin();
        player.draw(batch);
        batch.end();

        // Update and draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
