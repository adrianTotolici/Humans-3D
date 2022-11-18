package com.game.humans.world;

import com.game.humans.utils.EnumsEntityGui;

import java.util.Random;

/**
 * Created by adrian on 2/27/15.
 */
public class GuiTextureFilePath {

    private static final String guiTexture="game/guitexture/";
    private static final String guiTools = "game/guitexture/tools/";
    private static final String guiItem = "game/guitexture/item/";

    public enum GuiTexturePath{
        HEALTH_BAR(guiTexture+"healthBar"),
        INVENTORY_BAR(guiTexture+"inventory_bar"),
        INVENTORY_SELECTION(guiTexture+"selection"),
        MENU_FRAME(guiTexture+"menuSkin");

        private String guiTexturePath;
        GuiTexturePath(String guiTexturePath){
            this.guiTexturePath = guiTexturePath;
        }

        public String getGuiTexturePath(){
            return guiTexturePath;
        }
    }

    public enum GuiItemPath{
        APPLE_ITEM(guiItem+"appleGui", EnumsEntityGui.Items.APPLE.getItemName()),
        ROCK_ITEM(guiItem+"rock_item", EnumsEntityGui.Items.STONE.getItemName()),
        WOOD_ITEM(guiItem+"wood_item", EnumsEntityGui.Items.WOOD.getItemName()),
        HAND_AXE_ITEM(guiTools+"handAxe"+new Random().nextInt(16), EnumsEntityGui.Items.HAND_AXE.getItemName()),
        CAMPFIRE_ITEM(guiTools+"campFire", EnumsEntityGui.Items.FIRE_CAMP.getItemName());

        private String guiItemPath;
        private String guiItemName;

        GuiItemPath (String guiItemPath, String guiItemName){
            this.guiItemPath = guiItemPath;
            this.guiItemName = guiItemName;
        }

        public String getGuiItemPath(){
            return guiItemPath;
        }

        public String getGuiItemName() {
            return guiItemName;
        }
    }
}
