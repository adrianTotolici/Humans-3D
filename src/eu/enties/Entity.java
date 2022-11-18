package eu.enties;

import eu.gui.GuiTexture;
import eu.renderEngine.models.TextureModel;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Class representing objects in the world
 */
public class Entity {

    /**
     *  Textured model of entity
     */
    public TextureModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private boolean show;
    private String name;
    private GuiTexture guiTexture;

    private Vector3f collision;
    private float collisionRadius;


    private int textureIndex = 0;

    /**
     * Constructor for simple entity in the world
     *
     * @param model ,object of an entity to be initialize for the world render
     * @param position ,the position of entity in the world
     * @param rotX ,rotation on x axis of the entity
     * @param rotY ,rotation on y axis of the entity
     * @param rotZ ,rotation on z axis of the entity
     * @param scale ,scale of the entity of the object
     */
    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                  float collisionRadius) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.collisionRadius = collisionRadius;
    }

    /**
     * Constructor for entity whit texture applied to it
     *
     * @param model ,object of an entity to be initialize for the world render
     * @param position ,the position of entity in the world
     * @param rotX ,rotation on x axis of the entity
     * @param rotY ,rotation on y axis of the entity
     * @param rotZ ,rotation on z axis of the entity
     * @param scale ,scale of the entity of the object
     * @param textureIndex ,texture ID to be applied to object
     */
    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                  int textureIndex, float collisionRadius) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.textureIndex = textureIndex;
        this.collisionRadius = collisionRadius;

    }

    /**
     * Constructor for entity whit texture applied to it
     *
     * @param model ,object of an entity to be initialize for the world render
     * @param position ,the position of entity in the world
     * @param rotX ,rotation on x axis of the entity
     * @param rotY ,rotation on y axis of the entity
     * @param rotZ ,rotation on z axis of the entity
     * @param scale ,scale of the entity of the object
     * @param textureIndex ,texture ID to be applied to object
     * @param guiTexture , gui texture that coresponds to this enmity.
     */
    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                  int textureIndex, GuiTexture guiTexture, float collisionRadius) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.textureIndex = textureIndex;
        this.guiTexture = guiTexture;
        this.collisionRadius = collisionRadius;

    }

    /**
     * Constructor for entity whit texture applied to it
     *
     * @param model ,object of an entity to be initialize for the world render
     * @param position ,the position of entity in the world
     * @param rotX ,rotation on x axis of the entity
     * @param rotY ,rotation on y axis of the entity
     * @param rotZ ,rotation on z axis of the entity
     * @param scale ,scale of the entity of the object
     * @param textureIndex ,texture ID to be applied to object
     * @param guiTexture , gui texture that coresponds to this enmity.
     * @param show , flag to mark if entity is shown on map or not.
     * @param name ,  name of entity.
     */
    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean show,
                  String name, GuiTexture guiTexture, int textureIndex, float collisionRadius) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.show = show;
        this.name = name;
        this.guiTexture = guiTexture;
        this.textureIndex = textureIndex;
        this.collisionRadius = collisionRadius;

    }


    /**
     * Method used to increase position on x, y and z axis.
     *
     * @param dx , position of entity on x axis
     * @param dy , position of entity on y axis
     * @param dz , position of entity on z axis
     */
    public void increasePosition(float dx, float dy, float dz, List<Vector3f> collisionPoints){

        if (dx!=0 || dz!=0){
//            boolean dontCollide = verifyIfWillCollide(new Vector3f(this.position.x + dx, this.position.y + dy,
//                    this.position.z + dz), collisionPoints);
//            if (dontCollide){
                this.position.x += dx;
                this.position.y += dy;
                this.position.z += dz;
//            }
        }

    }

    /**
     * Method used to increase rotation on x,y and z axis.
     *
     * @param dx , rotation of entity on x axis
     * @param dy , rotation of entity on y axis
     * @param dz , rotation of entity on z axis
     */
    public void increaseRotation(float dx, float dy, float dz){
        this.rotX+=dx;
        this.rotY+=dy;
        this.rotZ+=dz;
    }

    /**
     * Method used to set position of an entity on the map
     *
     * @param position , axis coordinates of entity
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Method used to set rotation on x axis
     *
     * @param rotX rotation value
     */
    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    /**
     * Method used to set rotation on y axis
     *
     * @param rotY rotation value
     */
    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    /**
     * Method used to set roation on z axis
     *
     * @param rotZ rotation value
     */
    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    /**
     * Method used to set scale of entity
     *
     * @param scale value
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Method used to get model used for entity
     *
     * @return textured model
     */
    public TextureModel getModel() {
        return model;
    }

    /**
     * Method used to get coordinates on all axis for an entity
     *
     * @return vector containing all coordinates
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Method used to get rotation on x axis of entity
     *
     * @return value of rotation on x axis
     */
    public float getRotX() {
        return rotX;
    }

    /**
     * Method used to get rotation on y axis of entity
     *
     * @return value of rotation on y axis
     */
    public float getRotY() {
        return rotY;
    }

    /**
     * Method used to get rotation on z axis of entity
     *
     * @return value of rotation on z axis
     */
    public float getRotZ() {
        return rotZ;
    }

    /**
     * Method used to get scale of entity
     *
     * @return value of scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Method used to get offset of texture clamped on model for x axis
     *
     * @return offset value on x for texture
     */
    public float getTextureXOffset(){
        int column =textureIndex%model.getTexture().getNumberOfRows();
        return (float)column/(float)model.getTexture().getNumberOfRows();
    }

    /**
     * Method used to get offset of texture clamped on model for y axis
     *
     * @return offset value on y for texture
     */
    public float getTextureYOffset(){
        int row = textureIndex/model.getTexture().getNumberOfRows();
        return row/model.getTexture().getNumberOfRows();
    }

    /**
     * Method used to set flag for entity visibility
     *
     * @param show flag for visibility
     */
    public void setShow(boolean show) {
        this.show = show;
    }

    /**
     * Method used to get flag for entity visibility
     *
     * @return value of flag for visibility
     */
    public boolean isShow() {
        return show;
    }

    /**
     * Method used to get name of entity
     *
     * @return name of entity as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Method used to set a name to an entity
     *
     * @param name , name of entity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Methodu used to get texture index of a entity.
     * @return integer representing the index of texture on entity
     */
    public int getTextureIndex() {
        return textureIndex;
    }

    /**
     * Method to return Gui texture of an entity
     * @return gui Texture object for a certain entity.
     */
    public GuiTexture getGuiTexture() {
        return guiTexture;
    }

    /**
     * Method to return starting point of model.
     * @return vector whit starting point of collision
     */
    public synchronized Vector3f getCollision() {
        this.collision= new Vector3f(position.getX() - collisionRadius, position.getY(), position.getZ() - collisionRadius);
        return collision;
    }

    /**
     * Method to return radius of object
     * @return a float representig radius of collision point.
     */
    public float getCollisionRadius() {
        return collisionRadius;
    }

    public void setCollisionRadius(float collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public void setCollision(Vector3f collision) {
        this.collision = collision;
    }

    private boolean verifyIfWillCollide(Vector3f futurePosition, List<Vector3f> collisionPoints){
        for (Vector3f collisionPoint : collisionPoints) {
            //todo add dinamic position to encounter entity
            float xPlayer = futurePosition.x;
            float xCollision = collisionPoint.x;

            float zPlayer = futurePosition.z;
            float zCollision = collisionPoint.z;

            float xMaxCollision = xCollision +(xPlayer - xCollision)+0.1f;
            float zMaxCollision = zCollision + (zPlayer - zCollision)+0.1f;

            if ((xPlayer > xCollision)&&(xPlayer <xMaxCollision)){
                if ((zPlayer> zCollision)&&(zPlayer<zMaxCollision)){
                    return false;
                }
            }
        }
        return true;
    }
}
