package eu.water;

import java.util.List;


import com.game.humans.utils.HumansConstants;
import com.game.humans.world.TerrainFilePaths;
import eu.enties.Camera;
import eu.renderEngine.DisplayManager;
import eu.renderEngine.Loader;
import eu.renderEngine.models.RawModel;
import eu.toolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;


public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers waterFrameBuffers;

	private float moveFactor = 0;
	private int dudvTexture;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers waterFrameBuffers) {
		this.shader = shader;
		this.waterFrameBuffers = waterFrameBuffers;
		dudvTexture = loader.loadTexture(TerrainFilePaths.DuDvMaps.WATER_DUDV.getWaterDUDVPath());

		shader.start();
		shader.connectTexureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera) {
		prepareRender(camera);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += HumansConstants.WAVE_SPEED* DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFrameBuffers.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFrameBuffers.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
