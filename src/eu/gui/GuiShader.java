package eu.gui;

import eu.renderEngine.shaders.ShaderProgram;
import eu.toolBox.EngineConstants;
import org.lwjgl.util.vector.Matrix4f;


public class GuiShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = EngineConstants.OPEN_GL_MODE_FILE_PATH +"guiVertexShader.txt";
	private static final String FRAGMENT_FILE = EngineConstants.OPEN_GL_MODE_FILE_PATH +"guiFragmentShader.txt";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

    @Override
    protected void getAllUniformLocation() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
