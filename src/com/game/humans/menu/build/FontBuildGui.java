package com.game.humans.menu.build;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.UnicodeFont;

/**
 * Class used to build all fonts in GUI
 */
public class FontBuildGui {

    private Vector2f position;
    private UnicodeFont font;
    private String text;

    /**
     * Font gui constructor used to place text on screen
     *
     * @param position , coordinates in 2D of writing on screen
     * @param font , fonts used for writing
     * @param text , text value
     */
    public FontBuildGui(Vector2f position, UnicodeFont font, String text) {
        this.position = calculateFontPozition(position);
        this.font = font;
        this.text = text;
    }

    private Vector2f calculateFontPozition(Vector2f position){
        float y1 = position.getY();
        float x1 = position.getX();

        float y = (y1)*-100;
        float fontY = ((Display.getHeight()) * y) / 100;

        float x = (x1)*-100;
        float fontX =(((Display.getWidth()) * x)/100)*(-1);

        return new Vector2f((fontX),(fontY));
    }

    /**
     * Method used to get font used in writing
     *
     * @return unicode font
     */
    public UnicodeFont getFont() {
        return font;
    }

    /**
     * Method used to set font to text in game
     *
     * @param font font to be set
     */
    public void setFont(UnicodeFont font) {
        this.font = font;
    }

    /**
     * Method used to get text to font
     *
     * @return sting whit text used
     */
    public String getText() {
        return text;
    }

    /**
     * Method used to get position of font in GUI
     *
     * @return vector 2f whit position of text in GUI
     */
    public Vector2f getPosition() {
        return position;
    }
}
