package com.game.humans.menu.build;

import eu.gui.GuiTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adrian on 5/9/15.
 */
public class BuildTechnology {

    private String name;
    private List<HashMap<String,Integer>> materialNecessary = new ArrayList<HashMap<String, Integer>>();
    private List<GuiTexture> guiTextureList = new ArrayList<>();

    public BuildTechnology(String name, List<HashMap<String, Integer>> materialNecessary, List<GuiTexture> guiTextureList) {
        this.name = name;
        this.materialNecessary = materialNecessary;
        this.guiTextureList = guiTextureList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HashMap<String, Integer>> getMaterialNecessary() {
        return materialNecessary;
    }

    public void setMaterialNecessary(List<HashMap<String, Integer>> materialNecessary) {
        this.materialNecessary = materialNecessary;
    }

    public List<GuiTexture> getGuiTextureList() {
        return guiTextureList;
    }

    public void setGuiTextureList(List<GuiTexture> guiTextureList) {
        this.guiTextureList = guiTextureList;
    }
}
