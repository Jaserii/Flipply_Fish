package io.github.flipply_fish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private Texture playerTexture;
    private Sprite player;
    private float touchCooldown = 0.0f;
    private boolean canControl = true;
    private float jumpInertia = 0.0f;
    private boolean hasDied;

    /**
     * When a Player object is created, assign a texture to represent the player
     * @param playerSpriteFilePath  The file to use as the player
     */
    public Player(String playerSpriteFilePath) {
        playerTexture = new Texture(playerSpriteFilePath);
        player = new Sprite(playerTexture);
        player.setSize(0.5f,0.5f);
        player.setY(Settings.worldHeight / 2f); // Sets starting height to middle of screen
        hasDied = false;
    }

    /**
     * Method that calls the draw function for the Sprite class
     * @param batch The SpriteBatch in use
     */
    public void draw(SpriteBatch batch){
        player.draw(batch);
    }

    /**
     * Every time Render() calls, this method updates the Player's position based on
     * if they are interacting with the screen or not, and if the Player collided
     * with the world
     */
    public void updatePos() {
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

        //  Add delay in tapping so the player doesn't just immediately fly upwards
        touchCooldown -= delta;
        if (touchCooldown <= 0) canControl = true;

        //  Handle collision with "ceiling" and floor
        if (player.getY() > Settings.worldHeight) {
            player.setY(Settings.worldHeight-1);
            jumpInertia = 0;
        }
        if (player.getY() < 0) {
            player.setY(0.0f);
            hasDied = true;
        }
    }

    /**
     * Check if the player has died
     * @return True if they died from colliding from something
     */
    public boolean hasDied(){
        return hasDied;
    }
}
