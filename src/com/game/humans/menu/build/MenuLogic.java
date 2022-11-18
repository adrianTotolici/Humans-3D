package com.game.humans.menu.build;

import com.game.humans.technology.TechnologyLogic;
import com.game.humans.utils.FontsFilePath;
import com.game.humans.utils.InventoryLogic;
import com.game.humans.utils.Utils;
import eu.gui.GuiTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.UnicodeFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class used to implement build menu logic
 */
public class MenuLogic {

    private static MenuLogic instance;
    private static int fontSize = 16;
    private static int fontSize2 = 14;

    private int page = 0;

    private int nrOfBuildOfMenu = 8;

    private List<FontBuildGui> fontBuildGuiList = new ArrayList<>();
    private List<BuildTechnology> buildTechnologyList = new ArrayList<>();

    private Vector2f buildMenuPosition;

    /**
     * Method used to get instance of MenuLogic class
     * @return instante of class MenuLogic
     */
    public static synchronized MenuLogic getInstance(){
        if (instance == null ){
            instance = new MenuLogic();
        }
        return instance;
    }

    /**
     * Method used to add items in build menu
     */
    public synchronized void addGuiTexture(){

        for (int i = 0; i<buildTechnologyList.size(); i++) {

            String itemName= buildTechnologyList.get(i).getName();

            Vector2f vector2f = new Vector2f(buildMenuPosition.getX()-0.05f,buildMenuPosition.getY()+0.07f+(i*(-0.07f)));

            UnicodeFont trueTypeFont = Utils.loadFont(FontsFilePath.FontPath.AMAZON.getFontPath(), fontSize, false, false);
            UnicodeFont trueTypeFont2 = Utils.loadFont(FontsFilePath.FontPath.AMAZON.getFontPath(), fontSize2, false, false);
            FontBuildGui fontGui = new FontBuildGui(vector2f,trueTypeFont,itemName);
            fontBuildGuiList.add(fontGui);

            //add resources needed

            checkForMaterialsNeded(itemName,vector2f,trueTypeFont2);
        }


    }

    /**
     * Method used to set position of build menu on the screen
     *
     * @param buildMenuPosition vector containing x and y coordinate
     */
    public void setBuildMenuPosition(Vector2f buildMenuPosition) {
        this.buildMenuPosition = buildMenuPosition;
    }

    /**
     * Method used to get all fonts text in build menu
     *
     * @return a list of text from build menu
     */
    public List<FontBuildGui> getFontBuildGuiList() {
        return fontBuildGuiList;
    }

    /**
     * Method used to check the item clicked in build menu
     *
     * @param xPoz position x of clicked mouse
     * @param yPoz position y of clicked mouse
     * @return a recipe for building that certain item
     */
    public BuildTechnology checkMenuItemSelected(int xPoz, int yPoz){
        if (checkIfFirstRowIsBuild(xPoz, yPoz)){
            return buildTechnologyList.get(page+0);
        }

        if (checkIfSecondRowIsBuild(xPoz, yPoz)){
            return buildTechnologyList.get(page+0);
        }

        return null;
    }

    /**
     * Method used to add new items in build menu
     *
     * @param technologyName name of new item in build menu
     * @param guiList list of necessarily materials and resulted item
     * @return a list whit all items in build menu
     */
    public List<GuiTexture> addTechnology(String technologyName, List<GuiTexture> guiList){
        List<HashMap<String,Integer>> listMaterials = TechnologyLogic.getInstance().getTechnologyRecipe(technologyName);
        BuildTechnology buildTechnology = new BuildTechnology(technologyName,listMaterials, guiList);
        buildTechnologyList.add(buildTechnology);

        addGuiTexture();

        return getGuiBuildList();
    }

    private List<GuiTexture> getGuiBuildList(){
        List<GuiTexture> buildList = new ArrayList<>();
        int pageStep = buildTechnologyList.size()/nrOfBuildOfMenu;
        for (int i=0; i<pageStep+1; i++){
            int truncSizeOfTechnology = getTruncSizeOfTechnology(i);
            int x = 0;
            while (x<truncSizeOfTechnology) {
                List<GuiTexture> guiTextureList = buildTechnologyList.get(x).getGuiTextureList();
                for (int j = 0; j < guiTextureList.size(); j++) {
                    GuiTexture gui = guiTextureList.get(j);
                    GuiTexture newGui = new GuiTexture(gui.getTexture(), gui.getScale());
                    newGui.setName(gui.getName());
                    Vector2f position = new Vector2f();

                    float v = 0f;
                    if (j > 0) {
                        v = 0.3f + ((j - 1) * 0.1f);
                    }

                    switch (x) {
                        case (0):
                            position.setX(EnumMenuItems.ItemsBuildMenu.ONE.getPozXimageItemBuild() + v);
                            position.setY(EnumMenuItems.ItemsBuildMenu.ONE.getPozYimageItemBuild());
                            break;
                        case (1):
                            position.setX(EnumMenuItems.ItemsBuildMenu.TWO.getPozXimageItemBuild() + v);
                            position.setY(EnumMenuItems.ItemsBuildMenu.TWO.getPozYimageItemBuild());
                            break;
                        case (2):
                            position.setX(EnumMenuItems.ItemsBuildMenu.THREE.getPozXimageItemBuild() + v);
                            position.setY(EnumMenuItems.ItemsBuildMenu.THREE.getPozYimageItemBuild());
                            break;
                        default:
                            Utils.logWhitTime("Build menu wrong postion !!!", MenuLogic.class.getPackage()+MenuLogic.class.getName());
                            break;
                    }

                    newGui.setPosition(position);
                    newGui.setScale(new Vector2f(0.07f, 0.07f));
                    buildList.add(newGui);
                }
                x++;
            }
        }
        return buildList;
    }

    private void checkForMaterialsNeded(String itemName, Vector2f vector2f, UnicodeFont unicodeFont){

        List<String> technologyName = TechnologyLogic.getInstance().getTechnologyName(itemName);
        float step = 0.05f;
        for (int i=0;i<technologyName.size();i++) {
            FontBuildGui fontGui = new FontBuildGui(new Vector2f(vector2f.getX()+0.13f+(step*i), vector2f.getY()-0.01f),
                    unicodeFont,technologyName.get(i));
            fontBuildGuiList.add(fontGui);
        }
    }

    private boolean checkIfFirstRowIsBuild(int xPoz, int yPoz){
        int lowerWidth = (int) ((69.33*Display.getWidth())/100);
        int higherWidth = (int) ((94.62*Display.getWidth())/100);

        int lowerHeight = (int) ((82.03*Display.getHeight())/100);
        int higherHeight = (int) ((88.43*Display.getHeight())/100);

        return ((xPoz < higherWidth) && (xPoz > lowerWidth)) && ((yPoz<higherHeight) && (yPoz>lowerHeight));
    }

    private boolean checkIfSecondRowIsBuild(int xPoz, int yPoz){
        int lowerWidth = (int) ((69.33*Display.getWidth())/100);
        int higherWidth = (int) ((94.62*Display.getWidth())/100);

        int lowerHeight = (int) ((75.93*Display.getHeight())/100);
        int higherHeight = (int) ((80.31*Display.getHeight())/100);
        return ((xPoz < higherWidth) && (xPoz > lowerWidth)) && ((yPoz<higherHeight) && (yPoz>lowerHeight));
    }

    private int getTruncSizeOfTechnology(int page){
        int indexMare = (nrOfBuildOfMenu*page)+nrOfBuildOfMenu;
        int sizeOfTrunk = nrOfBuildOfMenu;
        if (buildTechnologyList.size()<indexMare){
            sizeOfTrunk = buildTechnologyList.size();
        }
        return sizeOfTrunk;
    }


}

