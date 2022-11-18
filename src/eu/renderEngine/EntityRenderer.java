package eu.renderEngine;

import eu.enties.Entity;
import eu.renderEngine.models.RawModel;
import eu.renderEngine.models.TextureModel;
import eu.renderEngine.shaders.StaticShader;
import eu.renderEngine.textures.ModelTexture;
import eu.toolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

/**
 * Class used to render entity whit texture in the world
 */
public class EntityRenderer {

    private StaticShader shader;

    /**
     * Constructor for Entity renderer class
     *
     * @param shader , program used to implement shader for graphic card
     * @param projectionMatrix , matrix used for projection when entity is render
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader=shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Method used to render a list of entities on the map
     *
     * @param entities list of entities whit associated textures as keys
     */
    public void render(Map<TextureModel, List<Entity>> entities){
        for (TextureModel model : entities.keySet()) {
            prepareTexureModel(model);
            List<Entity> batch=entities.get(model);
            for (Entity entity : batch) {
               prepareInstance(entity);
               GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    private void  prepareTexureModel(TextureModel model){
        RawModel rawModel=model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture=model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());

        if (texture.isHasTransaparency()){
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightngVariable(texture.isUseFakeLightung());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindTextureModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);

    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix= Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(),entity.getTextureYOffset());
    }


}
