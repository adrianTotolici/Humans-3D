package com.game.humans.technology;

import com.game.humans.utils.EnumsEntityGui;
import eu.gui.GuiTexture;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adrian on 5/9/15.
 */
public class EnumTechnology {

    public enum BuildItems{
        HAND_AXE("Hand axe",2,1, EnumsEntityGui.Items.STONE.getItemName(),EnumsEntityGui.Items.APPLE.getItemName()),
        CAMP_FIRE("Camp fire",3,1, EnumsEntityGui.Items.WOOD.getItemName(),EnumsEntityGui.Items.APPLE.getItemName());

        String name;
        int item1;
        int item2;
        String item1Name;
        String item2Name;
        HashMap<String,List<GuiTexture>> guiTextureListHashmap = new HashMap<>();

        BuildItems(String name, int item1, int item2, String item1Name, String item2Name){
            this.name = name;
            this.item1 = item1;
            this.item2 = item2;
            this.item1Name = item1Name;
            this.item2Name = item2Name;
        }

        BuildItems(String name, int item1, int item2, String item1Name, String item2Name,List<GuiTexture> guiTextureList){
            this.name = name;
            this.item1 = item1;
            this.item2 = item2;
            this.item1Name = item1Name;
            this.item2Name = item2Name;
        }

        public String getName() {
            return name;
        }

        public int getItem1() {
            return item1;
        }

        public int getItem2() {
            return item2;
        }

        public String getItem1Name() {
            return item1Name;
        }

        public String getItem2Name() {
            return item2Name;
        }

        public List<GuiTexture> getGuiTextureList(String key) {
            return guiTextureListHashmap.get(key);
        }

        public void setGuiTextureList(List<GuiTexture> guiTextureList, String key) {
            guiTextureListHashmap.put(key,guiTextureList);
        }
    }

}
