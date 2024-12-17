package io.github.flipply_fish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Score {
    private BitmapFont font;
    private int score;

    public Score(float scale) {
        score = 0;
        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(scale);
    }

    public BitmapFont getScoreBitmap() {
        return font;
    }

    public String getValue(){
        return String.valueOf(score);
    }
    public int getLength() {
        return String.valueOf(score).length();
    }
}
