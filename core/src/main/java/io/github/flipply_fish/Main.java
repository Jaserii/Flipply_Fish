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
    private Texture fishImage;
    private Texture vReefImage;

    private Sprite player;
    private Sprite vReef;

    private Sound bounce;

    private SpriteBatch batch;
    private FitViewport viewport;






    //  Game resource filenames
    private String fishImageFile = "libgdx.png";
    private String reefImageFile = "rectangle.png";


    //  Other game variables
    private float touchCooldown = 0.0f;
    private boolean canControl = true;
    private float jumpInertia = 0.0f;
    private float reefIntertia =0.0f;
    private int worldWidth = 3;
    private int worldHeight = 6;

    private float reefSpeed = 2f;      // Speed of the reef moving left
    private float reefSpawnX = worldWidth;  // Starting X position of the reef (off-screen)
    private float reefY = 2f;          // Y position of the reef (can be randomized)


    @Override
    public void create() {
        fishImage = new Texture(fishImageFile);
        vReefImage = new Texture(reefImageFile);
        player = new Sprite(fishImage);
        vReef = new Sprite(vReefImage);
        vReef.setSize((float)0.5,2);
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
        float gravity = 7f;
        float delta = Gdx.graphics.getDeltaTime();

        //  Let player bounce up if they touched the screen and the cooldown is not in effect
        if (Gdx.input.justTouched() && canControl){
            jumpInertia = speed * delta;
            player.translateY(jumpInertia);
            touchCooldown = 0.25f;  // Start cooldown
            canControl = false;
        }
        //  Keep bouncing up from the last bounce but with less and less force
        else if (jumpInertia >= 0.0f) {
            jumpInertia -= delta;
            player.translateY(jumpInertia);
        }
        //  Let gravity kick in after awhile
        else {
            jumpInertia = 0;
            player.translateY(gravity * delta * (-1));
        }
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        //  Add delay in tapping so the player doesn't just immediately fly upwards
        touchCooldown -= delta;
        if (touchCooldown <= 0) canControl = true;

        //  Handle collision with "ceiling" and floor
        if (player.getY() +player.getHeight() > worldHeight) {
            player.setY(worldHeight-player.getHeight());
            jumpInertia = 0;
        }

        if (player.getY() < 0) {
            player.setY(0.0f);
        }

        vReef.translateX(-reefSpeed * delta);

        // Check if reef is off-screen
        if (vReef.getX() + vReef.getWidth() < 0) {
            resetReef();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        //  Draw sprites within this block
        batch.begin();
        player.draw(batch);
        vReef.draw(batch);
        batch.end();
    }

    private void resetReef() {
        // Move reef back to the right side of the screen
        vReef.setX(reefSpawnX);

        // Randomize reef's vertical position (optional)
        reefY = (float) (Math.random() * (worldHeight - vReef.getHeight()));
        vReef.setY(reefY);
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
