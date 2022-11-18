package com.game.humans.terrain;

import eu.enties.Entity;
import eu.renderEngine.terrains.Terrain;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Class used to render object  around a player based by a specific radius.
 */
public class SmallGird {

    /**
     * Constant used to get radius of object renderer around player.
     */
    private final static int SMALL_GRID = 8;

    /**
     * Constant used to set grid size.
     */
    private final static int SMALL_GRID_SIZE = 100;

    /**
     * Constant used to store number of small grids after sectioning full map
     */
    private final static int pozDivider = 100;

    /**
     * Variable used to remeber current grid position on X.
     */
    private float xCurrentGrid;

    /**
     * Variable used to remember current grid position on Z.
     */
    private float zCurrentGrid;

    /**
     * Small grid instance;
     */
    private static SmallGird instance;

    /**
     * List contains all object on a small grid section.
     */
    private Hashtable<String,List<Entity>> smallGridLists = new Hashtable<String, List<Entity>>();

    private Hashtable<String, List<Vector3f>> collision = new Hashtable<>();

    private List<Vector3f> collisionList = new ArrayList<>();

    /**
     * Method used to get instance of small grid.
     * @return instance of SmallGrid class
     */
    public static synchronized SmallGird getInstance(){
        if (instance == null){
            instance = new SmallGird();
        }
        return instance;
    }

    /**
     * Method used to section terrain in smaller grid
     *
     * @param terrain terrain to be section
     */
    public void addTerrainToSmallGrid(Terrain terrain){
        xCurrentGrid = terrain.getX()/ SMALL_GRID;
        zCurrentGrid = terrain.getZ()/ SMALL_GRID;

        if (terrain.getX() >=0 ){
            xCurrentGrid = SMALL_GRID_SIZE;
        }else {
            xCurrentGrid = -SMALL_GRID_SIZE;
        }

        if (terrain.getZ() >=0 ){
            zCurrentGrid = SMALL_GRID_SIZE;
        }else {
            zCurrentGrid = -SMALL_GRID_SIZE;
        }

        for (int z=0 ; z< SMALL_GRID; z++){
            for(int x=0; x< SMALL_GRID; x++){
                Vector2f vector2f = new Vector2f(xCurrentGrid, zCurrentGrid);
                loadVectorToHashMap(vector2f);
                xCurrentGrid =getXCurrentDirection(xCurrentGrid);
            }
            xCurrentGrid = getXCurrentDirection(0);
            zCurrentGrid = getZCurrentDirection(zCurrentGrid);
        }
    }

    /**
     * Method used to get current gird on X axis
     *
     * @param xCurrentGrid current x coordinate of current grid
     * @return direction of movement on X coordinate
     */
    private float getXCurrentDirection(float xCurrentGrid){
        if (xCurrentGrid >=0 ){
            return (xCurrentGrid + SMALL_GRID_SIZE);
        }else {
            return (xCurrentGrid - SMALL_GRID_SIZE);
        }
    }

    /**
     * Method used to get current gird on Z axis
     *
     * @param zCurrentGrid current z coordinate of current grid
     * @return direction of movement on z coordinate
     */
    private float getZCurrentDirection(float zCurrentGrid){
        if (zCurrentGrid >=0 ){
            return (zCurrentGrid + SMALL_GRID_SIZE);
        }else {
            return (zCurrentGrid - SMALL_GRID_SIZE);
        }
    }

    /**
     * Method to load small vector gird into hashmap
     *
     * @param coordonateVector coordinate of small gird
     */
    private void loadVectorToHashMap(Vector2f coordonateVector){
        float x = coordonateVector.getX();
        float y = coordonateVector.getY();

        int key1 = (int) (x / pozDivider);
        int key2 = (int) (y / pozDivider);

        smallGridLists.put(String.valueOf(key1)+String.valueOf(key2),new ArrayList<Entity>());
    }

    /**
     * Method used to add entity and collision parameters on small grid.
     *
     * @param x x coordinate in the world
     * @param z z coordinate in the world
     * @param entity object to add in the world
     */
    public void addElements (float x, float z, Entity entity){
        String key = getKey(x, z);
        List<Entity> entities = smallGridLists.get(key);

        if (entities != null) {
            entities.add(entity);
            addCollisionPoint(key,entity);
        }else {
            entities = new ArrayList<>();
            entities.add(entity);
            addCollisionPoint(key,entity);
            smallGridLists.put(key, entities);
        }
    }

    /**
     * Method uset to get all object to render from a certain
     *
     * @param xPoz position on x coordinate of player in the world
     * @param zPoz position on y coordinate of player in the world
     * @return list of entity ready to be render
     */
    public List<Entity> getEntitesToRender (float xPoz, float zPoz){
        List<Entity> entities = new ArrayList<Entity>();

        String keyCenterCenter = getKey(xPoz,zPoz);
        String keyCenterLeft = getKey((xPoz-pozDivider),zPoz);
        String keyCenterRight = getKey((xPoz+pozDivider),zPoz);
        String keyTopCenter = getKey(xPoz,(zPoz+pozDivider));
        String keyTopLeft = getKey((xPoz-pozDivider),(zPoz+pozDivider));
        String keyTopRight = getKey((xPoz+pozDivider),(zPoz+pozDivider));
        String keyDownCenter = getKey(xPoz,(zPoz-pozDivider));
        String keyDownLeft = getKey((xPoz-pozDivider),(zPoz-pozDivider));
        String keyDownRight = getKey((xPoz + pozDivider), (zPoz - pozDivider));

        List<Entity> tempListRezult = smallGridLists.get(keyCenterCenter);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyCenterLeft);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyCenterRight);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyTopCenter);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyTopLeft);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyTopRight);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyDownCenter);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyDownLeft);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        tempListRezult = smallGridLists.get(keyDownRight);
        if (tempListRezult!=null){
            entities.addAll(tempListRezult);
        }

        collisionList = collision.get(keyCenterCenter);
        return entities;
    }

    /**
     * Method used to get a specific entity by name from a small gird
     *
     * @param name name of the entity
     * @return list of entity whit the name given as parameter
     */
    public List<Entity> getAllEntitiesByName(String name){
        List<Entity> entityList = new ArrayList<Entity>();
        for (List<Entity> entities : smallGridLists.values()) {
            for (Entity entity : entities) {
                if (entity.getName().equals(name)){
                    entityList.add(entity);
                }
            }
        }
        return entityList;
    }

    /**
     * Method used to cet a list of collision from the fragment map the player is in
     * @return a list of Vector3f whit collision coordinates
     */
    public List<Vector3f> getCollisionPoints(){
        return collisionList;
    }

    private String getKey(float xPoz, float zPoz){
        int key1 = (int) (xPoz / pozDivider);
        if (key1 >=0 ){
            key1 +=1;
        }else {
            key1 -=1;
        }

        int key2 = (int) (zPoz / pozDivider);
        if (key2 >=0 ){
            key2 += 1;
        }else {
            key2 -= 1;
        }

        return String.valueOf(key1)+String.valueOf(key2);
    }

    private void addCollisionPoint(String key, Entity entity){
        List<Vector3f> collisionPoints = collision.get(key);
        if (collisionPoints == null){
            collisionPoints = new ArrayList<>();
            collisionPoints.add(entity.getCollision());
            collision.put(key, collisionPoints);
        }else {
            collisionPoints.add(entity.getCollision());
        }
    }

}