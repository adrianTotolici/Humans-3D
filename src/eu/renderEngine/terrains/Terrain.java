package eu.renderEngine.terrains;

import eu.renderEngine.Loader;
import eu.renderEngine.models.RawModel;
import eu.renderEngine.textures.ModelTexture;
import eu.renderEngine.textures.TerrainTexture;
import eu.renderEngine.textures.TerrainTexturePack;
import eu.toolBox.Maths;
import org.lwjgl.util.vector.Vector2f;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by adrian on 26.10.2014.
 */
public class Terrain {

    private float SIZE;
    private float MAX_HEIGHT=40;
    private static final float MAX_PIXEL_COLOUR=256*256*256;

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
        this.texturePack=texturePack;
        this.blendMap=blendMap;
        this.SIZE=800;
        this.MAX_HEIGHT=40;
        this.x=gridX*800;
        this.z=gridZ*800;
        this.model=generateTerrain(loader, "res/"+heightMap+".png");
    }

    public float getSIZE() {
        return SIZE;
    }

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap,
                    String heightMapPath, float maxHeight, float size){
        this.texturePack=texturePack;
        this.blendMap=blendMap;
        this.SIZE=size;
        this.MAX_HEIGHT=maxHeight;
        this.x=gridX*size;
        this.z=gridZ*size;
        this.model=generateTerrain(loader, heightMapPath);
    }

    private RawModel generateTerrain(Loader loader, String heightMap){

        BufferedImage image=null;
        try {
            image=ImageIO.read(new File(heightMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT=image.getHeight();
        heights=new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height=getHeight(j,i,image);
                heights[j][i]=height;
                vertices[vertexPointer*3+1] = getHeight(j,i,image);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal= calculateNormal(j,i,image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int z, BufferedImage image){
        if (x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()){
            return 0;
        }

        float height=image.getRGB(x,z);
        height +=MAX_PIXEL_COLOUR/2f;
        height /=MAX_PIXEL_COLOUR/2f;
        height *=MAX_HEIGHT;
        return height;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image){
        float heightL= getHeight(x-1, z,image);
        float heightR= getHeight(x+1, z,image);
        float heightD= getHeight(x, z-1,image);
        float heightU= getHeight(x, z+1,image);

        Vector3f normal=new Vector3f(heightL-heightR, 2f, heightD-heightU);
        normal.normalize();
        return normal;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX= worldX -this.x;
        float terrainZ= worldZ- this.z;
        float gridScuareSize= SIZE/((float)heights.length-1);
        int gridX= (int) Math.floor(terrainX/gridScuareSize);
        int gridZ= (int) Math.floor(terrainZ/gridScuareSize);
        if (gridX >= heights.length-1 || gridZ >= heights.length -1 || gridX<0 || gridZ <0){
            return 0;
        }

        float xCoord= (terrainX % gridScuareSize)/gridScuareSize;
        float zCoord= (terrainZ % gridScuareSize)/gridScuareSize;

        float answer;
        if (xCoord <= (1-zCoord)){
            answer= Maths.barryCentric(new org.lwjgl.util.vector.Vector3f(0,heights[gridX][gridZ],0),
                    new org.lwjgl.util.vector.Vector3f(1,heights[gridX+1][gridZ],0),
                            new org.lwjgl.util.vector.Vector3f(0,heights[gridX][gridZ+1],1),new Vector2f(xCoord,zCoord));
        }else {
            answer= Maths.barryCentric(new org.lwjgl.util.vector.Vector3f(1,heights[gridX+1][gridZ],0),
                    new org.lwjgl.util.vector.Vector3f(1,heights[gridX+1][gridZ+1],1),
                    new org.lwjgl.util.vector.Vector3f(0,heights[gridX][gridZ+1],1),new Vector2f(xCoord,zCoord));
        }

        return answer;
    }


}
