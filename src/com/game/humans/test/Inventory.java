package com.game.humans.test;

import com.game.humans.utils.EnumsEntityGui.Position;
import com.game.humans.utils.Utils;
import eu.enties.Entity;
import eu.gui.GuiTexture;
import eu.renderEngine.Loader;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.game.humans.utils.EnumsEntityGui.Position.*;
import static com.game.humans.world.GuiTextureFilePath.*;
import static com.game.humans.world.GuiTextureFilePath.GuiItemPath.*;

/**
 * Class that holds the inventory of a player
 */
public class Inventory {

    private static Inventory instance;
    private InventoryDataBase inventoryDataBase;
    private Loader loader;
    private HashMap<String, GuiTexture> itemGui = new HashMap<>();
    private GuiTexture selectionGui;

    private boolean containingItems = false;

    private Inventory(Loader loader) {
        inventoryDataBase = new InventoryDataBase();
        this.loader = loader;
    }

    /**
     * Method crates a single instance of inventory thrum all the game.
     * @return instance of inventroy class
     */
    public static synchronized Inventory getInstance(Loader loader){
        if (instance == null){
            instance = new Inventory(loader);
        }
        return instance;
    }

    /**
     * Method add a new item in to inventory.
     * @param item item ready to bee added in inventory.
     */
    public void addToInventory(Entity item) {
        if (itemGui.isEmpty()){
            selectionGui = new GuiTexture(loader.loadTexture(GuiTexturePath.INVENTORY_SELECTION.getGuiTexturePath()),
                    getLastPosition(),new Vector2f(0.07f, 0.1f));
            containingItems = true;
        }
        inventoryDataBase.addNewEntity(item, 1);
        String name = item.getName();
        if (!itemGui.containsKey(name)) {
            GuiTexture guiTexture = new GuiTexture(loader.loadTexture(getGuiTextureFilePath(name)), getLastPosition(), new Vector2f(0.1f, 0.1f), name);
            itemGui.put(name, guiTexture);
        }
    }

    /**
     * Method used to get selected item from inventory.
     * @return selected entity
     */
    public Entity getFromInventory() {
        String selectedItem = getSelectedItem();
        Entity entity = inventoryDataBase.getEntity(selectedItem, 1);
        if (inventoryDataBase.getNumberOfEntity(selectedItem) == 0) {
            itemGui.remove(selectedItem);
            if (itemGui.size()<1){
                containingItems = false;
            }
        }

        return new Entity(entity.getModel(),entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),
                entity.getScale(),true,entity.getName(),entity.getGuiTexture(), entity.getTextureIndex(),entity.getCollisionRadius());
    }

    /**
     * Methodd used to place an item on the world map
     *
     * @param shadowItem item to be placed on the map
     */
//    public void placeItemOnMap(Entity shadowItem){
//
//        Entity entity = searchSelected();
//        for (Entity tokableItem : tokableItems) {
//            if (tokableItem.equals(entity)){
//                entity.setPosition(shadowItem.getPosition());
//                entity.setRotY(shadowItem.getRotY());
//                entity.setShow(true);
//
//                itemsTook.remove(entity);
//                removeGuiTexture();
//                break;
//            }
//        }
//    }

    /**
     * Method to get an item form inventory, form a certain position.
     * @param name item name in inventory
     * @return item to be placed in world
     */
    public Entity getFromInventoryByName(String name){
        Entity entity = inventoryDataBase.getEntity(name, 1);
        if (inventoryDataBase.getNumberOfEntity(name) == 0) {
            itemGui.remove(name);
            if (itemGui.size()<1){
                containingItems = false;
            }
        }

        return new Entity(entity.getModel(),entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),
                entity.getScale(),true,entity.getName(),entity.getGuiTexture(), entity.getTextureIndex(), entity.getCollisionRadius());
    }

    /**
     * Method used to add an item in inventory from building whit a certain technology.
     * @param technology , technology used to develop the new item.
     */
    public void makeNewItem(Technology technology){

    }

    /**
     * Method that return all gui item from inventory.
     * @return return a list of gui texture.
     */
    public List<GuiTexture> getAllGuiItems(){
        List<GuiTexture> allGuisItems = new ArrayList<>();
        if (itemGui.size()>0) {
            for (String key : itemGui.keySet()) {
                allGuisItems.add(itemGui.get(key));
            }
            allGuisItems.add(selectionGui);
        }
        return allGuisItems;
    }

    /**
     * Method used to move item selector to item on left.
     */
    public void moveItemSelectorToLeft(){
        if (selectionGui != null) {
            int guiSelectorPosition = getGuiSelectorPosition();
            switch (guiSelectorPosition) {
                case 1:
                    selectionGui.setPosition(getLastPosition());
                    break;
                case 2:
                    selectionGui.setPosition(new Vector2f(ONE.getxPoz(), ONE.getyPoz()));
                    break;
                case 3:
                    selectionGui.setPosition(new Vector2f(SECOND.getxPoz(), SECOND.getyPoz()));
                    break;
                case 4:
                    selectionGui.setPosition(new Vector2f(THIRD.getxPoz(), THIRD.getyPoz()));
                    break;
                default:
                    selectionGui.setPosition(getLastPosition());
            }
        }
    }

    /**
     * Method used to move item selector to item on right.
     */
    public void moveItemSelectorToRight(){
        if (selectionGui != null) {
            int guiSelectorPosition = getGuiSelectorPosition();
            switch (guiSelectorPosition) {
                case 1:
                    if (itemGui.size() > 1) {
                        selectionGui.setPosition(new Vector2f(SECOND.getxPoz(), SECOND.getyPoz()));
                    }
                    break;
                case 2:
                    if (itemGui.size() > 2) {
                        selectionGui.setPosition(new Vector2f(THIRD.getxPoz(), THIRD.getyPoz()));
                    } else {
                        selectionGui.setPosition(new Vector2f(ONE.getxPoz(), ONE.getyPoz()));
                    }
                    break;
                case 3:
                    if (itemGui.size() > 3) {
                        selectionGui.setPosition(new Vector2f(FORTH.getxPoz(), FORTH.getyPoz()));
                    } else {
                        selectionGui.setPosition(new Vector2f(ONE.getxPoz(), ONE.getyPoz()));
                    }
                    break;
                default:
                    selectionGui.setPosition(new Vector2f(ONE.getxPoz(), ONE.getyPoz()));
            }
        }
    }

    /**
     * Method used to get flag for signal if inventroy is empty or not.
     * @return boolean value indicating state of inventory
     */
    public boolean isContainingItems() {
        return containingItems;
    }

    private String getGuiTextureFilePath(String itemName){
        if (itemName.equals(WOOD_ITEM.getGuiItemName())) {
            return WOOD_ITEM.getGuiItemPath();
        } else if (itemName.equals(GuiItemPath.ROCK_ITEM.getGuiItemName())) {
            return ROCK_ITEM.getGuiItemPath();
        } else if (itemName.equals(APPLE_ITEM.getGuiItemName())) {
            return APPLE_ITEM.getGuiItemPath();
        } else if (itemName.equals(HAND_AXE_ITEM.getGuiItemName())) {
            return HAND_AXE_ITEM.getGuiItemPath();
        } else if (itemName.equals(CAMPFIRE_ITEM.getGuiItemName())) {
            return CAMPFIRE_ITEM.getGuiItemPath();
        } else {
            Utils.logWhitTime("Item Gui not found.", Inventory.class.getName());
            return null;
        }
    }

    private Vector2f getLastPosition(){
        int size = itemGui.size();
        switch (size){
            case 0:
                return new Vector2f(ONE.getxPoz(), ONE.getyPoz());
            case 1:
                return new Vector2f(Position.SECOND.getxPoz(), Position.SECOND.getyPoz());
            case 2:
                return new Vector2f(Position.THIRD.getxPoz(), Position.THIRD.getyPoz());
            case 3:
                return new Vector2f(Position.FORTH.getxPoz(), Position.FORTH.getyPoz());
            default:
                Utils.logWhitTime("Gui item list is biger than 8 positions.", Inventory.class.getName());
                return new Vector2f(0,0);
        }
    }

    private int getGuiSelectorPosition(){
        int currentPosition = 0;
        float x = selectionGui.getPosition().getX();

        if (x == Position.ONE.getxPoz()){
            currentPosition = 1;
        }else if (x == Position.SECOND.getxPoz()){
            currentPosition = 2;
        }else if (x == Position.THIRD.getxPoz()){
            currentPosition = 3;
        }else if (x == Position.FORTH.getxPoz()){
            currentPosition = 4;
        }

        if (currentPosition == 0){
            Utils.logWhitTime("Position of item selection is not corectly.",Inventory.class.getName());
        }

        return currentPosition;
    }

    private String getSelectedItem(){
        float currentPosition = 0;
        float x = selectionGui.getPosition().getX();

        if (x == Position.ONE.getxPoz()){
            currentPosition = ONE.getxPoz();
        }else if (x == Position.SECOND.getxPoz()){
            currentPosition = SECOND.getxPoz();
        }else if (x == Position.THIRD.getxPoz()){
            currentPosition = THIRD.getxPoz();
        }else if (x == Position.FORTH.getxPoz()){
            currentPosition = FORTH.getxPoz();
        }
        if (currentPosition == 0){
            Utils.logWhitTime("Position of item selection is not corectly.",Inventory.class.getName());
        }

        for (String key : itemGui.keySet()) {
            GuiTexture guiTexture = itemGui.get(key);
            if (guiTexture.getPosition().getX() == currentPosition){
                return guiTexture.getName();
            }
        }
        return null;
    }

//    private Entity searchSelected(){
//        if (selectionGuiAdded){
//            for (GuiTexture guiTexture : guiItemList) {
//                if (guiTexture.getPosition() == selectionGui.getPosition()){
//                    String name = guiTexture.getName();
//                    for (Entity entity : itemsTook) {
//                        if (entity.getName().equals(name)){
//                            return entity;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
}
