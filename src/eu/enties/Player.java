package eu.enties;

import eu.renderEngine.DisplayManager;
import eu.renderEngine.models.TextureModel;
import eu.renderEngine.terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 09.11.2014.
 */
public class Player extends Entity{

    private static final float RUN_SPEED=1;
    private static final float TURN_SPEED=30;
    private static final float GRAVITY=-10;
    private static final float JUMP_POWER=1;

    private float currentSpeed=0;
    private float currentTurnSpeed=0;
    private float upwardsSpeed=0;

    private boolean isInAir=false;

    private List<Vector3f> collisionPoints = new ArrayList<>();

    public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float collisionRadius) {
        super(model, position, rotX, rotY, rotZ, scale, collisionRadius);
    }

    public Player(TextureModel model, Vector3f postion, float rotX, float rotY, float rotZ, float scale, int textureIndex, float collisionRadius){
        super(model, postion, rotX, rotY, rotZ, scale, textureIndex, collisionRadius);
    }

    public void move(Terrain terrain, List<Vector3f> collisionPoints){
        this.collisionPoints = collisionPoints;
        chekInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance=currentSpeed*DisplayManager.getFrameTimeSeconds();

        float dx= (float) (distance*Math.sin(Math.toRadians(super.getRotY())));
        float dz= (float) (distance*Math.cos(Math.toRadians(super.getRotY())));

            super.increasePosition(dx, 0, dz, collisionPoints);
            upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
            super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0,collisionPoints);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight){
            upwardsSpeed=0;
            isInAir=false;
            super.getPosition().y= terrainHeight;
        }
    }

    private void jump(){
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir=true;
        }
    }

    private void chekInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = -RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){

            this.currentSpeed=RUN_SPEED;
        }else {
            currentSpeed=0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed=-TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed=TURN_SPEED;
        }else {
            currentTurnSpeed=0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            currentSpeed*=2;
        }else {
            currentSpeed*=1;
        }
    }


}
