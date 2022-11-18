package eu.renderEngine.shaders;

import eu.enties.Camera;
import eu.enties.Light;
import eu.toolBox.EngineConstants;
import eu.toolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

/**
 * Created by adrian on 07.09.2014.
 */
public class StaticShader extends ShaderProgram {
    private static final int MAX_LIGHTS=4;

    private static final String VERTEX_FILE= EngineConstants.OPEN_GL_MODE_FILE_PATH +"vertexShader.txt";
    private static final String FRAGMENT_FILE=EngineConstants.OPEN_GL_MODE_FILE_PATH +"fragmentShader.txt";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_attenuation[];

    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighing;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;

    private int location_plane;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocation() {
       locationTransformationMatrix=super.getUniformLocation("transformationMatrix");
       locationProjectionMatrix=super.getUniformLocation("projectionMatrix");
       locationViewMatrix=super.getUniformLocation("viewMatrix");
       location_shineDamper=super.getUniformLocation("shineDamper");
       location_reflectivity=super.getUniformLocation("reflectivity");
        location_useFakeLighing=super.getUniformLocation("useFakeLighting");
        location_skyColour=super.getUniformLocation("skyColour");
        location_numberOfRows=super.getUniformLocation("numberOfRows");
        location_offset=super.getUniformLocation("offset");
        location_plane=super.getUniformLocation("plane");

        location_lightPosition =new int[MAX_LIGHTS];
        location_lightColor =new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for (int i=0;i<MAX_LIGHTS;i++){
            location_lightPosition[i]=super.getUniformLocation("lightPosition["+i+"]");
            location_lightColor[i]=super.getUniformLocation("lightColor["+i+"]");
            location_attenuation[i]=super.getUniformLocation("attenuation["+i+"]");
        }
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(locationTransformationMatrix,matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(locationProjectionMatrix,projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix= Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix,viewMatrix);
    }

    public void loadLights(List<Light> lights){
        for (int i=0;i<MAX_LIGHTS;i++){
            if (i<lights.size()){
                super.loadVector(location_lightPosition[i], lights.get(i).getPozition());
                super.loadVector(location_lightColor[i],lights.get(i).getColor());
                super.loadVector(location_attenuation[i],lights.get(i).getAttenuation());
            }else {
                super.loadVector(location_lightPosition[i],new Vector3f(0,0,0));
                super.loadVector(location_lightColor[i],new Vector3f(0,0,0));
                super.loadVector(location_attenuation[i],new Vector3f(1,0,0));
            }
        }
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadFakeLightngVariable(boolean useFakeLighting){
        super.loadBoolean(location_useFakeLighing,useFakeLighting);
    }

    public void loadSkyColour(float r, float g, float b){
        super.loadVector(location_skyColour, new Vector3f(r, g, b));
    }

    public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(location_numberOfRows,numberOfRows);
    }

    public void loadOffset(float x, float y){
        super.loadVector(location_offset, new Vector2f(x, y));
    }

    public void loadClipPlane(Vector4f plane){
        super.loadVector(location_plane,plane);
    }
}
