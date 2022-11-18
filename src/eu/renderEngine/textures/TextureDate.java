package eu.renderEngine.textures;

import java.nio.ByteBuffer;

/**
 * Created by adrian on 2/14/15.
 */
public class TextureDate {

    private int width;
    private int height;
    private ByteBuffer buffer;

    public TextureDate(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.height = height;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
