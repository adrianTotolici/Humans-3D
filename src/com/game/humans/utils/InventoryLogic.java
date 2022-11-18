package com.game.humans.utils;

import com.game.humans.menu.build.BuildTechnology;
import eu.enties.Entity;
import eu.gui.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.UnicodeFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Class used to make logic for inventory
 */
public class InventoryLogic {

    private static InventoryLogic inventoryLogic;
    private static int fontSizeItems = 32;

    private List<Entity> tokableItems;
    private List<Entity> itemsTook = new ArrayList<Entity>();
    private List<GuiTexture> guiItemList = new ArrayList<GuiTexture>();
    private List<GuiTexture> allGuiItemList = new ArrayList<GuiTexture>();
    private List<FontGui> fonts = new ArrayList<FontGui>();
    private GuiTexture selectionGui;
    private boolean selectionGuiAdded = false;
    private HashMap<String, GuiTexture> guiHashMap = new HashMap<String, GuiTexture>();

    private InventoryLogic(List<Entity> tokableItems, HashMap<String, GuiTexture> guiHashMap, List<FontGui> fonts,
                           GuiTexture selectionGui) {
        this.tokableItems = tokableItems;
        this.guiHashMap = guiHashMap;
        this.fonts = fonts;
        this.selectionGui = selectionGui;
    }

    /**
     * Method used to initialize the logic of an inventory
     *
     * @param tokableItems object which can pe took from map
     * @param guiHashMap hash map whit texture of items
     * @param fonts text of inventory items
     * @param selectionGui texture used for selection
     * @return instance of InventroyLogic class
     */
    public static synchronized InventoryLogic getInventoryLogic(List<Entity> tokableItems,
                                                                HashMap<String,GuiTexture> guiHashMap,
                                                                List<FontGui> fonts, GuiTexture selectionGui){
        if (inventoryLogic == null){
            inventoryLogic = new InventoryLogic(tokableItems, guiHashMap, fonts, selectionGui);
        }
        return inventoryLogic;
    }

    /**
     * Method used to take an item from the map and adding it to inventory
     *
     * @param item element token from map
     */
    public void takeItem(Entity item){
        for (Entity tokableItem : tokableItems) {
            if (tokableItem.equals(item) && item.isShow()){
                item.setShow(false);
                itemsTook.add(item);
                addGuiTexture(item.getName());
                break;
            }
        }
    }

    /**
     * Method used to get an entity from inventory
     *
     * @return entity selected from inventory
     */
    public Entity getItemFromInventory(){
        Entity entity = searchSelected();
        if (entity != null) {
            return new Entity(entity.getModel(), entity.getPosition(), entity.getRotX(), entity.getRotY(),
                    entity.getRotZ(), entity.getScale(), entity.getCollisionRadius());
        }
        return null;
    }

    /**
     * Methodd used to place an item on the world map
     *
     * @param shadowItem item to be placed on the map
     */
    public void placeItemOnMap(Entity shadowItem){

        Entity entity = searchSelected();
        for (Entity tokableItem : tokableItems) {
            if (tokableItem.equals(entity)){
                entity.setPosition(shadowItem.getPosition());
                entity.setRotY(shadowItem.getRotY());
                entity.setShow(true);

                itemsTook.remove(entity);
                removeGuiTexture();
                break;
            }
        }
    }

    /**
     * Method used to move selection tab to left
     */
    public void moveSelectionToLeft(){
        for (int i = 0; i < fonts.size(); i++) {
            FontGui font = fonts.get(i);
            if (font.isSelected()) {
                if (i == 0){
                    break;
                }else {
                    font.setSelected(false);
                    fonts.get(i-1).setSelected(true);
                }
                break;
            }
        }
    }

    /**
     * Method used to move selection tab to right
     */
    public void moveSelectionToRight(){
        for (int i = 0; i < fonts.size(); i++) {
            FontGui font = fonts.get(i);
            if (font.isSelected()) {
                if (i == fonts.size()-1){
                    break;
                }else {
                    font.setSelected(false);
                    fonts.get(i+1).setSelected(true);
                }
                break;
            }
        }
    }

    /**
     * Method to get item size
     *
     * @return the size of an item
     */
    public int getItemTookSize(){
        return itemsTook.size();
    }

    /**
     * Method uset to get all item gui textures from UI
     *
     * @return list of all gui items
     */
    public List<GuiTexture> getAllGuiItemList() {
        allGuiItemList.clear();
        allGuiItemList.addAll(guiItemList);
        if (!guiItemList.isEmpty()) {
            allGuiItemList.add(selectionGui);
        }
        return allGuiItemList;
    }

    /**
     * Method used to get all fonts of GUI
     *
     * @return list of all fonts on UI
     */
    public List<FontGui> getFonts() {
        setSelectionGui();
        return fonts;
    }

    private void addGuiTexture(String itemName){
        GuiTexture guiTexture = guiHashMap.get(itemName);
        guiTexture.setName(itemName);

        float startPosition = -0.3f;
        float step=-0.08f;

        Vector2f position = new Vector2f();
        if (!guiItemList.contains(guiTexture)){
            switch (guiItemList.size()){
                case 0:
                    guiTexture.setPosition(new Vector2f(EnumsEntityGui.Position.ONE.getxPoz(), EnumsEntityGui.Position.ONE.getyPoz()));
                    position.set(startPosition,guiTexture.getPosition().getY());
                    break;
                case 1:
                    guiTexture.setPosition(new Vector2f(EnumsEntityGui.Position.SECOND.getxPoz(), EnumsEntityGui.Position.SECOND.getyPoz()));
                    position.set(startPosition+step,guiTexture.getPosition().getY());
                    break;
                case 2:
                    guiTexture.setPosition(new Vector2f(EnumsEntityGui.Position.THIRD.getxPoz(), EnumsEntityGui.Position.THIRD.getyPoz()));
                    position.set(startPosition+(step*2), guiTexture.getPosition().getY());
                    break;
                case 3:
                    guiTexture.setPosition(new Vector2f(EnumsEntityGui.Position.FORTH.getxPoz(), EnumsEntityGui.Position.FORTH.getyPoz()));
                    position.set(startPosition+(step*3), guiTexture.getPosition().getY());
                    break;
                default:
                    break;
            }

            guiItemList.add(guiTexture);

            UnicodeFont trueTypeFont = Utils.loadFont(FontsFilePath.FontPath.HEAVY_HEAP.getFontPath(), fontSizeItems, false, false);
            FontGui fontGui = new FontGui(position,trueTypeFont,1);
            if (fonts.isEmpty()){
                fontGui.setSelected(true);
                selectionGui.setPosition(guiTexture.getPosition());
            }
            fonts.add(fontGui);

        }else {
            for (int i = 0; i < guiItemList.size(); i++) {
                GuiTexture texture = guiItemList.get(i);
                if (texture.equals(guiTexture)) {
                    FontGui fontGui = fonts.get(i);
                    fontGui.setNumber(fontGui.getNumber()+1);
                    break;
                }
            }
        }
    }

    private void removeGuiTexture(){
        for (int i = 0; i < fonts.size(); i++) {
            FontGui font = fonts.get(i);
            if (font.isSelected()) {
                int number = font.getNumber();
                if (number == 1){
                    guiItemList.remove(i);
                    fonts.remove(font);

                    if (!fonts.isEmpty()){
                        FontGui fontGui = fonts.get(0);
                        fontGui.setSelected(true);

                    }
                }else {
                    number = number-1;
                    font.setNumber(number);
                    allGuiItemList.remove(selectionGui);
                    selectionGuiAdded = false;
                }
                break;
            }
        }
    }

    private void setSelectionGui(){
        if (!guiItemList.isEmpty()) {
            for (int i = 0; i < guiItemList.size(); i++) {
                if (fonts.get(i).isSelected()) {
                    selectionGui.setPosition(guiItemList.get(i).getPosition());
                    if (!selectionGuiAdded) {
                        allGuiItemList.add(selectionGui);
                        selectionGuiAdded = true;
                    }
                    break;
                }
            }
        }
    }

    private Entity searchSelected(){
        if (selectionGuiAdded){
            for (GuiTexture guiTexture : guiItemList) {
                if (guiTexture.getPosition() == selectionGui.getPosition()){
                    String name = guiTexture.getName();
                    for (Entity entity : itemsTook) {
                        if (entity.getName().equals(name)){
                            return entity;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void addItemInPouch(BuildTechnology buildTechnology){
        List<GuiTexture> guiTextureList = buildTechnology.getGuiTextureList();
        List<HashMap<String, Integer>> materialNecessary = buildTechnology.getMaterialNecessary();
        String name = buildTechnology.getName();

        addGuiTexture(name);

    }
}
