package eu.enties;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by adrian on 07.09.2014.
 */
public class Camera {

    private float distanceFromPlayer=3;
    private float angleAroundPlayer=-180;

    private Vector3f position=new Vector3f(0,5,5);
    private float pitch =20;
    private float yaw;
    private float rol;
    private float run=1f;
    private float offsetY=0.5f;

    private Player player;

    public Camera(Player player) {
        this.player=player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        caluculateAngleAroundPlayer();
        float horizantalDistance= caluculateHorizantalDistance();
        float verticalDistance =caluculateVeritcalDistance();
        calculateCameraPosition(horizantalDistance,verticalDistance);
        this.yaw=180-(player.getRotY() +angleAroundPlayer);
    }

    public void  increaseSpeed(){
        run+=0.1f;
    }

    public void decreaseSpeed(){
        run-=0.1f;
    }

    public void invertPitch(){
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRol() {
        return rol;
    }

    private void calculateCameraPosition(float horizDistance, float verticalDistance){
        float theta=player.getRotY()+angleAroundPlayer;
        float offsetX=(float)(horizDistance*Math.sin(Math.toRadians(theta)));
        float offsetZ=(float)(horizDistance*Math.cos(Math.toRadians(theta)));

        position.x=player.getPosition().x -offsetX;
        position.z=player.getPosition().z -offsetZ;
        position.y=player.getPosition().y + verticalDistance+offsetY;
    }

    private float caluculateHorizantalDistance(){
        return  (float)(distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
    }

    private float caluculateVeritcalDistance(){
        return  (float)(distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel()*0.001f;
        if (distanceFromPlayer-zoomLevel>2){
            distanceFromPlayer -=zoomLevel;
        }
    }

    private void  calculatePitch(){
        if (Mouse.isButtonDown(1)){
            float pitchChange =Mouse.getDY() *0.1f;
            pitch -=pitchChange;
        }
    }

    private void caluculateAngleAroundPlayer(){
        if (Mouse.isButtonDown(0)){
            float angleChange= Mouse.getDX() *0.3f;
            angleAroundPlayer -=angleChange;
        }
    }
}
