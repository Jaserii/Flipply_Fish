package io.github.flipply_fish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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


    @Override
    public void create() {
        player = new Player(Settings.playerSpriteFilePath);
        batch = new SpriteBatch();
        viewport = new FitViewport(Settings.worldWidth, Settings.worldHeight);
        gameOver = false;
        gameStart = false;  //  Becomes true once player taps the screen
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
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //  Draw sprites within this block
        batch.begin();
        player.draw(batch);
        batch.end();
    }
}
