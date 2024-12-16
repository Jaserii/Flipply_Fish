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
    //private Texture playerTexture;
    //private Sprite player;
    private Sound bounce;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Player player;


    //  Game resource filenames
    private String playerSpriteFilePath = "libgdx.png";


    //  Other game variables
    private float touchCooldown = 0.0f;
    private boolean canControl = true;
    private float jumpInertia = 0.0f;
    public static int worldWidth = 3;
    public static int worldHeight = 6;

    @Override
    public void create() {
        player = new Player(playerSpriteFilePath);
        batch = new SpriteBatch();
        viewport = new FitViewport(worldWidth, worldHeight);
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
        // organize code into three methods
        player.updatePos();
        draw();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //  Draw sprites within this block
        batch.begin();
        player.draw(batch);
        batch.end();
    }
}
