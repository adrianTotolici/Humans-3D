package com.game.humans.technology;

import eu.gui.GuiTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.game.humans.technology.EnumTechnology.BuildItems.CAMP_FIRE;
import static com.game.humans.technology.EnumTechnology.BuildItems.HAND_AXE;

public class TechnologyLogic {

    private static TechnologyLogic instance;

    public synchronized static TechnologyLogic getInstance(){
        if (instance==null){
            instance=new TechnologyLogic();
        }
        return instance;
    }

    public List<HashMap<String,Integer>> getTechnologyRecipe(String technologyName){
        List<HashMap<String,Integer>> mapList = new ArrayList<>();
        HashMap<String,Integer> item = new HashMap<>();

        if (technologyName.equals(HAND_AXE.getName())){
            item.put(HAND_AXE.getItem1Name(),HAND_AXE.getItem1());
            item.put(HAND_AXE.getItem2Name(), HAND_AXE.getItem2());

            mapList.add(item);
            return mapList;
        }

        if (technologyName.equals(CAMP_FIRE.getName())){
            item.put(CAMP_FIRE.getItem1Name(),CAMP_FIRE.getItem1());
            item.put(CAMP_FIRE.getItem2Name(),CAMP_FIRE.getItem2());

            mapList.add(item);
            return mapList;
        }

            return mapList;
    }

    public List<String> getTechnologyName(String itemName){
        List<String> itemLists = new ArrayList<>();
        if (itemName.equals(HAND_AXE.getName())){
            String item1="x"+HAND_AXE.getItem1();
            String item2 ="x"+HAND_AXE.getItem2();

            itemLists.add(item1);
            itemLists.add(item2);

            return itemLists;
        }

        if (itemName.equals(CAMP_FIRE.getName())){
            String item1="x"+CAMP_FIRE.getItem1();
            String item2 ="x"+CAMP_FIRE.getItem2();

            itemLists.add(item1);
            itemLists.add(item2);

            return itemLists;
        }

        return itemLists;
    }

    public void addStoneAxeTechnologyGui(GuiTexture stoneAxe, GuiTexture stone, GuiTexture apple){
        List<GuiTexture> guiTextures = new ArrayList<>();
        guiTextures.add(stoneAxe);
        guiTextures.add(stone);
        guiTextures.add(apple);

        EnumTechnology.BuildItems.HAND_AXE.setGuiTextureList(guiTextures,EnumTechnology.BuildItems.HAND_AXE.getName());
    }

    public void addCampFireTechnologyGui(GuiTexture campFire, GuiTexture wood, GuiTexture apple){
        List<GuiTexture> guiTextures = new ArrayList<>();
        guiTextures.add(campFire);
        guiTextures.add(wood);
        guiTextures.add(apple);

        EnumTechnology.BuildItems.CAMP_FIRE.setGuiTextureList(guiTextures,EnumTechnology.BuildItems.CAMP_FIRE.getName());
    }

}
