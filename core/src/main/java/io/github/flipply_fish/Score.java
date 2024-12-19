package io.github.flipply_fish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Score {
    private BitmapFont currentScoreFont;
    private BitmapFont highScoreFont;
    private int score;

    public Score(float scale) {
        score = 0;

        // Load the .ttf font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Settings.scoreFontFilePath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20; // Set the font size in pixels
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE; // Set font color

        currentScoreFont = generator.generateFont(parameter);
        currentScoreFont.setUseIntegerPositions(false);
        currentScoreFont.getData().setScale(scale);

        parameter.color = com.badlogic.gdx.graphics.Color.GRAY;
        highScoreFont = generator.generateFont(parameter);
        highScoreFont.setUseIntegerPositions(false);
        highScoreFont.getData().setScale(scale);
        generator.dispose();
    }

    public void increment() {
        score++;
    }

    public BitmapFont getScoreBitmap() {
        return currentScoreFont;
    }

    public BitmapFont getHighScoreBitmap() { return highScoreFont; }
    public String getValue(){
        return String.valueOf(score);
    }
    public int getLength() {
        return String.valueOf(score).length();
    }
}
