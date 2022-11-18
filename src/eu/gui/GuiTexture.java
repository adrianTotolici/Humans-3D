package eu.gui;


import org.lwjgl.util.vector.Vector2f;

/**
 * Created by adrian on 2/6/15.
 */
public class GuiTexture {

    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private String name;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public GuiTexture(int texture, Vector2f position, Vector2f scale, String name) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.name = name;
    }

    public GuiTexture(int texture, Vector2f scale){
        this.texture = texture;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
