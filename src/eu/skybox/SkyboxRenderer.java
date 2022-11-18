package eu.skybox;

import eu.enties.Camera;
import eu.renderEngine.DisplayManager;
import eu.renderEngine.Loader;
import eu.renderEngine.MasterRenderer;
import eu.renderEngine.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by adrian on 2/14/15.
 */
public class SkyboxRenderer {

    private static final float SIZE = 60f;

    private static final float RED_NIGTH = 0.3f;
    private static final float GREEN_NIGHT = 0.32f;
    private static final float BLUE_NIGHT = 0.38f;

//    private static final float RED_DAY = 0f;
//    private static final float GREEN_DAY = 1f;
//    private static final float BLUE_DAY = 1f;


    private static final float RED_DAY = 0f;
    private static final float GREEN_DAY = 0.5f;
    private static final float BLUE_DAY = 0.6f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private static String[] TEXTURE_FILES = {"game/skybox/day/right","game/skybox/day/left","game/skybox/day/up","game/skybox/day/down","game/skybox/day/front","game/skybox/day/back"};
    private static String[] NIGHT_TEXTURE_FILES = {"game/skybox/night/right","game/skybox/night/left","game/skybox/night/up","game/skybox/night/down","game/skybox/night/front","game/skybox/night/back"};

    private RawModel cube;
    private int texture;
    private int nightTexture;
    private SkyboxShader shader;

    private float time = 0;

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
        cube = loader.loadToVAO(VERTICES,3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
        shader =new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, float r, float g, float b){
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColour(r, g, b);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures(){
        time += DisplayManager.getFrameTimeSeconds() * 1000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(time >= 0 && time < 5000){
            texture1 = nightTexture;
            texture2 = nightTexture;
            MasterRenderer.setFogColor(RED_NIGTH,GREEN_NIGHT,BLUE_NIGHT);
            blendFactor = (time - 0)/(5000);
        }else if(time >= 5000 && time < 8000){
            texture1 = nightTexture;
            texture2 = texture;
            MasterRenderer.setFogColor(RED_NIGTH,GREEN_NIGHT,BLUE_NIGHT);
            blendFactor = (time - 5000)/(8000 - 5000);
        }else if(time >= 8000 && time < 21000){
            texture1 = texture;
            texture2 = texture;
            MasterRenderer.setFogColor(RED_DAY,GREEN_DAY,BLUE_DAY);
            blendFactor = (time - 8000)/(21000 - 8000);
        }else{
            texture1 = texture;
            texture2 = nightTexture;
            MasterRenderer.setFogColor(RED_DAY,GREEN_DAY,BLUE_DAY);
            blendFactor = (time - 21000)/(24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }
}
