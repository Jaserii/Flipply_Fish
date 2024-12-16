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
    private Texture playerTexture;
    private Sprite player;
    private Sound bounce;
    private SpriteBatch batch;
    private FitViewport viewport;


    //  Game resource filenames
    private String playerTextureFile = "libgdx.png";


    //  Other game variables
    private float touchCooldown = 0.0f;
    private boolean canControl = true;
    private float jumpInertia = 0.0f;
    private int worldWidth = 3;
    private int worldHeight = 6;

    @Override
    public void create() {
        playerTexture = new Texture(playerTextureFile);
        player = new Sprite(playerTexture);
        player.setSize(1,1);

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
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 15f;
        float delta = Gdx.graphics.getDeltaTime();

        //  Let player bounce up if they touched the screen and the cooldown is not in effect
        if (Gdx.input.isTouched() && canControl){
            jumpInertia = speed * delta;
            player.translateY(jumpInertia);
            touchCooldown = 0.25f;  // Start cooldown
            canControl = false;
        }
        //  Keep bouncing up from the last bounce but with less and less force
        else {
            jumpInertia -= delta;
            player.translateY(jumpInertia);
        }
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        //  Add delay in tapping so the player doesn't just immediately fly upwards
        touchCooldown -= delta;
        if (touchCooldown <= 0) canControl = true;

        //  Handle collision with "ceiling" and floor
        if (player.getY() > worldHeight) {
            player.setY(worldHeight-1);
            jumpInertia = 0;
        }
        if (player.getY() < 0) {
            player.setY(0.0f);
        }
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

    /*
    private SpriteBatch batch;
    private Texture image;


    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 800);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

     */
}
