package com.game.humans.test;

import com.game.humans.utils.Utils;
import eu.enties.Entity;

import java.util.HashMap;

/**
 * Class used to hold all item in players inventory.
 */
public class InventoryDataBase {

    private HashMap<String, Integer> inventory = new HashMap<>();
    private HashMap<String, Entity> entityModels = new HashMap<>();

    /**
     * Method used to add new entity in inventory database.
     * @param entity entity to be added into database.
     * @param numberOfEntity number of entity to be added to database,
     */
    public void addNewEntity(Entity entity, int numberOfEntity){
        String name = entity.getName();
        boolean entityFound = false;

        for (String key : inventory.keySet()) {
            if (key.equals(name)) {
                inventory.put(key,(inventory.get(key) + numberOfEntity));
                entityFound=true;
                break;
            }
        }

        if (!entityFound) {
            inventory.put(name,numberOfEntity);
            entityModels.put(name,entity);
        }
    }

    /**
     * Method used to get a certain amount of entity form inventory data base.
     * @param name name of entity to be taken.
     * @param number number of entity taken form inventory.
     * @return an entity if it is available in inventory or null in case there is not sufficient items in inventory.
     */
    public Entity getEntity(String name, int number) {
        for (String key : inventory.keySet()) {
            if (key.equals(name)) {
                Integer numberOfEntitys = inventory.get(key);
                Entity entityModel = entityModels.get(key);

                Entity entity = new Entity(entityModel.getModel(), entityModel.getPosition(), entityModel.getRotX(),
                        entityModel.getRotY(), entityModel.getRotZ(), entityModel.getScale(), entityModel.isShow(),
                        entityModel.getName(), entityModel.getGuiTexture(),entityModel.getTextureIndex(), entityModel.getCollisionRadius());

                if ((numberOfEntitys - number) >= 0) {
                    if ((numberOfEntitys - number) == 0) {
                        entityModels.remove(key);
                        inventory.remove(key);
                    } else {
                        inventory.put(key, numberOfEntitys - number);
                    }
                    return entity;
                } else {
                    break;
                }
            }
        }
        Utils.logWhitTime("Requested too many items form inventory.",InventoryDataBase.class.getName());
        return null;
    }

    /**
     * Method used to get the number of entity in inventory.
     * @param name name of entity from inventory
     * @return the number of entity store in inventory
     */
    public int getNumberOfEntity(String name){
        for (String key : inventory.keySet()) {
            if (key.equals(name)){
               return inventory.get(key);
            }
        }
        return 0;
    }
}
