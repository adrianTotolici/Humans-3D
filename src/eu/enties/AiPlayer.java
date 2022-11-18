package eu.enties;

import eu.renderEngine.DisplayManager;
import eu.renderEngine.models.TextureModel;
import eu.renderEngine.terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Random;

/**
 * Created by adrian on 2/26/15.
 */
public class AiPlayer extends Entity{

    private static final float RUN_SPEED=3;
    private static final float TURN_SPEED=70;
    private static final float GRAVITY=-10;
    private static final float JUMP_POWER=7;

    private float currentSpeed=0;
    private float currentTurnSpeed=0;
    private float upwardsSpeed=0;

    private boolean isInAir=false;

    private Terrain terrain;


    public AiPlayer(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float collisionRadius) {
        super(model, position, rotX, rotY, rotZ, scale, collisionRadius);
    }

    public AiPlayer(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int textureIndex, float collisionRadius) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex,collisionRadius);
    }

    public void move(Terrain terrain, List<Vector3f> collisionPoints){
        this.terrain=terrain;
        randomMoveInputs();

        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance=currentSpeed*DisplayManager.getFrameTimeSeconds();

        float dx= (float) (distance*Math.sin(Math.toRadians(super.getRotY())));
        float dz= (float) (distance*Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx,0,dz,collisionPoints);

        upwardsSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0,upwardsSpeed*DisplayManager.getFrameTimeSeconds(),0,collisionPoints);
        float terrainHeight= this.terrain.getHeightOfTerrain(super.getPosition().x,super.getPosition().z);

        if (super.getPosition().y < terrainHeight){
            upwardsSpeed=0;
            isInAir=false;
            super.getPosition().y= terrainHeight;
        }


    }

    public synchronized void setTerrain(Terrain terrain, List<Vector3f> collisionPoints) {
        move(terrain,collisionPoints);
    }

    private void jump(){
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir=true;
        }
    }

    private void randomMoveInputs(){
        int randomMovement = randInt(0,4);
        switch (randomMovement){
            case 0:
                this.currentSpeed=-RUN_SPEED;
                break;
            case 1:
                this.currentSpeed = 0;
                break;
            case 2:
                this.currentTurnSpeed = TURN_SPEED;
                break;
            case 3:
                this.currentTurnSpeed = -TURN_SPEED;
                break;
            case 4:
                this.currentTurnSpeed = 0;
                break;
            default:
                break;
        }
    }

    private int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
