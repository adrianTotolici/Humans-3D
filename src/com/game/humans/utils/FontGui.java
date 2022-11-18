package com.game.humans.utils;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;

/**
 * Created by adrian on 3/18/15.
 */
public class FontGui {

    private Vector2f position;
    private UnicodeFont font;
    private int number;
    private boolean selected;

    public FontGui(Vector2f position, UnicodeFont font, int number) {
        this.position = calculateFontPozition(position);
        this.font = font;
        this.number = number;
        this.selected = false;
    }

    private Vector2f calculateFontPozition(Vector2f position){
        float y1 = position.getY();
        float x1 = position.getX();

        float y = (y1-0.025f)*-100;
        float fontY = ((Display.getHeight()) * y) / 100;

        float x = (x1-0.05f)*-100;
        float fontX = ((Display.getWidth()) * x)/100;

        return new Vector2f((fontX),(fontY));
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public UnicodeFont getFont() {
        return font;
    }

    public void setFont(UnicodeFont font) {
        this.font = font;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
