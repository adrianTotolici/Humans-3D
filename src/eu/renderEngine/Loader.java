package eu.renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import eu.renderEngine.models.RawModel;
import eu.renderEngine.textures.TextureDate;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store and load data in OpenGL.
 */
public class Loader {

    private List<Integer> vaos=new ArrayList<Integer>();
    private List<Integer> vbos=new ArrayList<Integer>();
    private List<Integer> textures=new ArrayList<Integer>();

    /**
     * Method used to generate raw model and load to VAO.
     *
     * @param positions position of model in 3D space
     * @param textureCoords the coordinates of texture on model
     * @param normals normals vector of models
     * @param indices indices of models
     * @return a raw model.
     */
    public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals, int[] indices){
        int vaoID=createVAO();
        bindIndecesBuffer(indices);
        storeDataInAtributeList(0,3,positions);
        storeDataInAtributeList(1,2,textureCoords);
        storeDataInAtributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    /**
     * Method used to load model into VAO
     *
     * @param position position of object in 3D space
     * @param dimensions dimensions of models
     * @return a raw model
     */
    public RawModel loadToVAO(float[] position, int dimensions){
        int vaoID=createVAO();
        this.storeDataInAtributeList(0,dimensions,position);
        unbindVAO();
        return new RawModel(vaoID,position.length/dimensions);
    }

    /**
     * Method used to load texture in OpenGL
     *
     * @param fileName name of file
     * @return integer address whit texture loaded
     */
    public int loadTexture(String fileName){
        Texture texture=null;
        try {
            texture= TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID=texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    /**
     * Method used to load sky-cube of the world around player
     *
     * @param textureFiles texture files used as sky
     * @return integer address of textured sky-cube
     */
    public int loadCubeMap(String[] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFiles.length; i++) {
            String textureFile = textureFiles[i];
            TextureDate date = decodeTextureFile("res/" + textureFile + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, date.getWidth(), date.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, date.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        textures.add(texID);
        return texID;
    }

    private TextureDate decodeTextureFile(String fileName){
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream inputStream=new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(inputStream);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width *4, Format.RGBA);
            buffer.flip();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work.");
            System.exit(-1);
        }
        return new TextureDate(buffer,width,height);
    }

    private int createVAO(){
        int vaoID= GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public void cleanUp(){
        for (Integer vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (Integer vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for (Integer texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private void storeDataInAtributeList(int attributeNumber,int coordonateSize, float[] data){
        int vboID= GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer= storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordonateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndecesBuffer(int[] indices){
        int vboID= GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer=storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer= BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer= BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
