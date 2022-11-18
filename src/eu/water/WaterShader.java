package eu.water;

import eu.enties.Camera;
import eu.renderEngine.shaders.ShaderProgram;
import eu.toolBox.EngineConstants;
import eu.toolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = EngineConstants.OPEN_GL_MODE_FILE_PATH +"waterVertex.txt";
	private final static String FRAGMENT_FILE = EngineConstants.OPEN_GL_MODE_FILE_PATH +"waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocation() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

    public void loadMoveFactor(float factor){
        super.loadFloat(location_moveFactor,factor);
    }

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
        super.loadVector(location_cameraPosition,camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

	public void connectTexureUnits(){
		super.loadInt(location_reflectionTexture,0);
		super.loadInt(location_refractionTexture,1);
        super.loadInt(location_dudvMap,2);
	}
}
