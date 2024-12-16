package io.github.flipply_fish;

import static io.github.flipply_fish.Main.worldHeight;

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

    public Player(String playerSpriteFilePath) {
        playerTexture = new Texture(playerSpriteFilePath);
        player = new Sprite(playerTexture);
        player.setSize(1,1);
    }

    public void draw(SpriteBatch batch){
        player.draw(batch);
    }

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
        if (player.getY() > worldHeight) {
            player.setY(worldHeight-1);
            jumpInertia = 0;
        }
        if (player.getY() < 0) {
            player.setY(0.0f);
        }
    }
}
