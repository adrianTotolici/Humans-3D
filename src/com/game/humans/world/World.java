/**
 * When I wrote this, only God and I understood what I was doing
 * Now only God knows.
 */
package com.game.humans.world;

import com.game.humans.menu.build.BuildTechnology;
import com.game.humans.technology.EnumTechnology;
import com.game.humans.test.Inventory;
import com.game.humans.utils.*;
import com.game.humans.menu.build.FontBuildGui;
import com.game.humans.menu.build.MenuLogic;
import com.game.humans.terrain.SmallGird;
import eu.enties.*;
import eu.gui.GuiRenderer;
import eu.gui.GuiTexture;
import eu.renderEngine.Loader;
import eu.renderEngine.MasterRenderer;
import eu.renderEngine.models.TextureModel;
import eu.renderEngine.terrains.Terrain;
import eu.toolBox.EngineConstants;
import eu.toolBox.MousePicker;
import eu.water.WaterFrameBuffers;
import eu.water.WaterRenderer;
import eu.water.WaterShader;
import eu.water.WaterTile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.game.humans.utils.EnumSystemSettings.OpenGlVersion.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Class used to generate the game world and applying the logic to it.
 */
public class World {

    /**
     *  World instance
     */
    private static World worldInstance;
    /** Instance of small grid used in sectioning map into smaller maps, for high performance */
    private static SmallGird smallGirdInstance;
    /** Loader variable used in loading all graphic elements into OpenGL. */
    private Loader loader;
    /** Instance of Inventory class */
    //##########
//    private static InventoryLogic inventoryLogic;
    private static Inventory inventory;
    /** Object representing logic for mouse piker int the world. */
    private MousePicker mousePicker;


    /** Master rendere used for rending whole scene,*/
    private MasterRenderer masterRenderer;
    /** Rendere used to render gui elements */
    private GuiRenderer guiRenderer;
    /** Texure used for starting screen */
    private static Texture splashScreen;

    /** Object contains all the world dynamic elements */
    private WorldElements worldElements;
    /** Object representing player in the world */
    private Player player;
    /** List of all AI players in the world */
    private List<AiPlayer> aiPlayers;
    /** Object depicting camera around player in the world. */
    private Camera camera;


    /** Variable used as flag for signaling when all the objects for game are loaded. */
    private boolean loading =true;
    /** Variable used as flag if building menu is on or not. */
    private boolean buildMenuOn = false;
    /** Variable used as flag if mouse is clicked. */
    private boolean mouseClicked = false;
    /** Variable used as flag if a key is pressed. */
    private boolean keyPressed = false;

    /** Light list in the world */
    private List<Light> lights;
    /** All Gui textures in the game */
    private List<GuiTexture> guiTextureList;
    /** List whit all gui items in the game */
    private HashMap<String, GuiTexture> allGuiItems;

    /** List whit all font writing in game */
    private List<FontGui> fontList;
    /** Gui texture for item selector */
    private GuiTexture itemSelection;
    /** Gui texture for building menu */
    private GuiTexture menuTexture;

    /** List whit gui texture for receipts to build */
    private List<GuiTexture> buildMenu = new ArrayList<>();
    /** List whit writing fonts for receipts to build */
    private List<FontBuildGui> buildFonts;
    /** List whit all items that can be taken in the world */
    private List<Entity> tookableItems;
    /** List whit all gui of items to be rendered*/
    private List<GuiTexture> guiItemsList;
    /** A shadow item to be shown on map only when something is seleted to be put down */
    private Entity shadowItems;

    /** Water renderer engine */
    private WaterRenderer waterRenderer;
    /** Whater shaderer */
    private WaterShader waterShader;
    /** List whit water tiles */
    private List<WaterTile> waterTileList;
    /** Water frame buffers for water effect */
    private WaterFrameBuffers waterFrameBuffers;

    /** Terrain grid to be render to left of player */
    public Terrain terrainToLeft;
    /** Terrain grid to right of player */
    public Terrain terrainToRight;
    /** Terrain grid to be render to right of player */
    public Terrain terrainBackLeft;
    /** Terrain grid to be render to back of player */
    public Terrain terrainBackRight;

    //todo remove this entity and add as item in future
    /** Camp fire entity */
    public Entity campFire;

    /** Constructor of World class. Initialize window, loader, renderers (water, gui, master), small grids. */
    private World() {
        Window.getWindowInstance().createWindow();
        setOpenGlVersion();
        loader = new Loader();
        if (EngineConstants.ACTIVE_WATER) {
            waterShader = new WaterShader();
        }

        masterRenderer = new MasterRenderer(loader);
        guiRenderer = new GuiRenderer(loader);

        if (EngineConstants.ACTIVE_WATER) {
            waterFrameBuffers = new WaterFrameBuffers();
        }

        if (EngineConstants.ACTIVE_WATER) {
            waterRenderer = new WaterRenderer(loader, waterShader, masterRenderer.getProjectionMatrix(), waterFrameBuffers);
        }

        smallGirdInstance = SmallGird.getInstance();
        worldElements = new WorldElements();

        lights = new ArrayList<>();
        guiTextureList = new ArrayList<>();
        aiPlayers = new ArrayList<>();
        tookableItems = new ArrayList<>();
        guiItemsList = new ArrayList<>();
        allGuiItems = new HashMap<>();
        fontList = new ArrayList<>();
        buildFonts = new ArrayList<>();
        if (EngineConstants.ACTIVE_WATER) {
            waterTileList = new ArrayList<>();
        }
    }

    /**
     * Method creates a single ton for class World. Since the world generate once in the game.
     * @return World instance
     */
    public static synchronized World getWorldInstance(){
        if (worldInstance == null){
            worldInstance = new World();
        }
        return worldInstance;
    }

    private void setOpenGlVersion() {
        ContextAttribs contextAttributes = new ContextAttribs(3, 2);
        contextAttributes.withForwardCompatible(true);
        contextAttributes.withProfileCore(true);

        String glVersionInfo = glGetString(GL_VERSION);
        String glVersion= glVersionInfo.split(" ")[0];
        glVersion = Utils.parseOpenGLVersion(glVersion);

        String gslsVer;

        if (glVersion.equals(Gl20.getVersion())) {
            gslsVer = Gl20.getGlslVersion();
        } else if (glVersion.equals(Gl21.getVersion())) {
            gslsVer = Gl21.getGlslVersion();

        } else if (glVersion.equals(Gl30.getVersion())) {
            gslsVer = Gl30.getGlslVersion();

        } else if (glVersion.equals(Gl31.getVersion())) {
            gslsVer = Gl31.getGlslVersion();

        } else if (glVersion.equals(Gl32.getVersion())) {
            gslsVer = Gl32.getGlslVersion();

        } else if (glVersion.equals(Gl33.getVersion())) {
            gslsVer = Gl33.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl40.getVersion())) {
            gslsVer = Gl40.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl41.getVersion())) {
            gslsVer = Gl41.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl42.getVersion())) {
            gslsVer = Gl42.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl43.getVersion())) {
            gslsVer = Gl43.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl44.getVersion())) {
            gslsVer = Gl44.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl45.getVersion())) {
            gslsVer = Gl45.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else if (glVersion.equals(Gl46.getVersion())) {
            gslsVer = Gl46.getGlslVersion();
            EngineConstants.ACTIVE_WATER = true;

        } else {
            gslsVer = glVersion;
        }
        Utils.logWhitTime("GLSL version selected is : " + gslsVer, World.class.getName());
        EngineConstants.OPEN_GL_MODE_FILE_PATH = EngineConstants.OPEN_GL_MODE_FILE_PATH+gslsVer+"/";
    }

    /**
     * Method used to initialize world for game.
     */
    public void generateTheWorld(){
        initSplashScreen();
        loadTerrain();
        loadAllGui();
        loadAllObjects();
        if (EngineConstants.ACTIVE_WATER) {
            loadWaterTiles();
        }

        mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(),terrainToRight);

        MenuLogic.getInstance().setBuildMenuPosition(menuTexture.getPosition());
//        loadBuildMenu();

        List<Entity> listToRender = new ArrayList<>();

        renderWorld(listToRender);

        if (EngineConstants.ACTIVE_WATER) {
            waterFrameBuffers.cleanUp();
        }
        masterRenderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        Window.getWindowInstance().closeWindow();
    }

    /**
     * Method used to render terrain and all objects in the world.
     *
     * @param listToRender a list whit all entity which can be render
     * @param clipPlane plane in which the the camera can see
     * @return a list of entity that can be render in the game
     */
    public List<Entity> renederObject(List<Entity> listToRender, Vector4f clipPlane){
        if (shadowItems!=null){
            Vector3f currentTerrainPoint = mousePicker.getCurrentTerrainPoint();
            if (currentTerrainPoint!=null) {
                shadowItems.setPosition(currentTerrainPoint);
                masterRenderer.processEntity(shadowItems);
            }
        }

        // rendere items in world
        for (Entity tookableItem : tookableItems) {
            if (tookableItem.isShow()){
                masterRenderer.processEntity(tookableItem);
            }
        }

        //todo make a new method for movement on terrain for player;
        // initialize  player movement
        switch (initTerrainMovement(player.getPosition().x,player.getPosition().z)) {
            case 0:
                listToRender = smallGirdInstance.getEntitesToRender(player.getPosition().getX(), player.getPosition().getZ());
                player.move(terrainToRight,smallGirdInstance.getCollisionPoints());
                mousePicker.setTerrain(terrainToRight);
                break;
            case 1:
                listToRender = smallGirdInstance.getEntitesToRender(player.getPosition().getX(), player.getPosition().getZ());
                player.move(terrainToLeft,smallGirdInstance.getCollisionPoints());
                mousePicker.setTerrain(terrainToLeft);
                break;
            case 2:
                listToRender = smallGirdInstance.getEntitesToRender(player.getPosition().getX(), player.getPosition().getZ());
                player.move(terrainBackRight,smallGirdInstance.getCollisionPoints());
                mousePicker.setTerrain(terrainBackRight);
                break;
            case 3:
                listToRender = smallGirdInstance.getEntitesToRender(player.getPosition().getX(), player.getPosition().getZ());
                player.move(terrainBackLeft,smallGirdInstance.getCollisionPoints());
                mousePicker.setTerrain(terrainBackLeft);
                break;
            default:
                Utils.logWhitTime("Terrain movement wrong !!!! x = " + player.getPosition().getX() +
                        "  z=" + player.getPosition().getZ(),World.class.getName());
                break;
        }

        // render fire camp
        masterRenderer.processEntity(campFire);
        // render player
        masterRenderer.processEntity(player);

        // render terrain
        masterRenderer.processTerrain(terrainToRight);
        masterRenderer.processTerrain(terrainToLeft);
        masterRenderer.processTerrain(terrainBackRight);
        masterRenderer.processTerrain(terrainBackLeft);

        // render close entity
        for (Entity entity : listToRender) {
            if (entity.isShow()) {
                masterRenderer.processEntity(entity);
            }
        }

        // todo create a new method for movement
        // render AI movement
        for (AiPlayer aiPlayer : aiPlayers) {
            switch (initTerrainMovement(aiPlayer.getPosition().x,aiPlayer.getPosition().z)) {
                case 0:
                    aiPlayer.setTerrain(terrainToRight, smallGirdInstance.getCollisionPoints());
                    break;
                case 1:
                    aiPlayer.setTerrain(terrainToLeft,smallGirdInstance.getCollisionPoints());
                    break;
                case 2:
                    aiPlayer.setTerrain(terrainBackRight, smallGirdInstance.getCollisionPoints());
                    break;
                case 3:
                    aiPlayer.setTerrain(terrainBackLeft, smallGirdInstance.getCollisionPoints());
                    break;
                default:
                    Utils.logWhitTime("Terrain movement wrong !!!! x = " + aiPlayer.getPosition().getX() +
                            "  z=" + aiPlayer.getPosition().getZ(), World.class.getName());
                    break;
            }
            masterRenderer.processEntity(aiPlayer);
        }

        // render lights
        masterRenderer.render(lights, camera, clipPlane);

        return listToRender;
    }

    /**
     * Method which update the screen by rendering all object
     * @param listToRender entity that will be render at this moment
     */
    private void renderWorld(List<Entity> listToRender){
        while (!Window.getWindowInstance().isWindowCloseRequired()){

            if (loading) {
                loadSplashScreen();
            }else {
                // initialize envorment
                camera.move();
                mousePicker.update();
                keyBoardListener();
                mouseListener();

                GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

                // render water
                if (EngineConstants.ACTIVE_WATER) {
                    waterFrameBuffers.bindReflectionFrameBuffer();
                    float distance = 2 * (camera.getPosition().y - HumansConstants.SEA_LEVEL);
                    camera.getPosition().y -= distance;
                    camera.invertPitch();
                    renederObject(listToRender, new Vector4f(0, 1, 0, -HumansConstants.SEA_LEVEL));
                    camera.getPosition().y += distance;
                    camera.invertPitch();


                    waterFrameBuffers.bindRefractionFrameBuffer();
                    renederObject(listToRender, new Vector4f(0, -1, 0, HumansConstants.SEA_LEVEL));

                }

                // render item selected in world
                if (EngineConstants.ACTIVE_WATER) {
                    waterFrameBuffers.unbindCurrentFrameBuffer();
                }
                GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
                listToRender = renederObject(listToRender, new Vector4f(0,1,0,-HumansConstants.SEA_LEVEL));

                if (EngineConstants.ACTIVE_WATER) {
                    waterRenderer.render(waterTileList, camera);
                }

//                 render gui elements (inventorii)
                guiRenderer.render(guiTextureList);
                guiRenderer.render(guiItemsList);

                // add inventory text
                //##############
//                fontList = inventoryLogic.getFonts();

                // add build menu text
                if (buildMenuOn){
                    guiRenderer.render(buildMenu);
                    buildFonts = MenuLogic.getInstance().getFontBuildGuiList();
                    for (FontBuildGui buildFont : buildFonts) {
                        UnicodeFont font = buildFont.getFont();
                        Vector2f position = buildFont.getPosition();
                        String text = buildFont.getText();

                        renderFont(font, text,position);
                    }
                }

                for (FontGui fontGui : fontList) {
                    UnicodeFont font = fontGui.getFont();
                    Vector2f position = fontGui.getPosition();
                    int number = fontGui.getNumber();

                    renderFont(font, String.valueOf(number),position);
                }
            }
            Window.getWindowInstance().updateWindow();
        }
    }


    /**
     * Method used to generate terrain and items on different girds of the world;
     *
     * @param textureModels list of model generated whit texture applied to it
     * @param terrains list of terrains generated in the world
     * @param aiPlayersModels list of AI players generated in the world
     * @param items list of items generated in the world
     */
    private void loadWorld(List<TextureModel> textureModels, List<Terrain> terrains, List<TextureModel> aiPlayersModels,
                           List<TextureModel> items){
        synchronized (this) {

            Utils.logWhitTime("Start thread 2.", World.class.getName());

            Utils.logWhitTime("Start loading small map grid.", World.class.getName());
            smallGirdInstance.addTerrainToSmallGrid(terrainToLeft);
            smallGirdInstance.addTerrainToSmallGrid(terrainToRight);
            smallGirdInstance.addTerrainToSmallGrid(terrainBackRight);
            smallGirdInstance.addTerrainToSmallGrid(terrainBackLeft);
            Utils.logWhitTime("Finish loading small map grid.", World.class.getName());

            addEntityInWorld(textureModels, terrains);
            addItemsInWorld(items,terrains);

            for (TextureModel aiPlayersModel : aiPlayersModels) {
                aiPlayers.addAll(worldElements.generateRandomAiPLayers(aiPlayersModel,
                        HumansConstants.MAX_NR_OF_AI_IN_WORLD, terrainToRight));
            }
            loading = false;
            Utils.logWhitTime("Finish thread 2.", World.class.getName());
        }
    }

    /**
     * Method used to identify on which terrain player is on.
     *
     * @return an integer representing the terrain number
     */
    private int initTerrainMovement(float x, float z){
        if (z<=0) {
            if (x > 0) {
                return 0;
            } else {
                return 1;
            }
        }else {
            if (x > 0) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    /**
     * Method used to render text over GUI.
     *
     * @param font font type
     * @param text text to be render
     * @param position position of text in 2D coordonates
     */
    private void renderFont(UnicodeFont font, String text, Vector2f position){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        font.drawString(position.getX(), position.getY(), text, Color.yellow);
        glDisable(GL_BLEND);
    }

    /**
     * Method used to initialize splash screen
     */
    private void initSplashScreen(){
        try {
            splashScreen = TextureLoader.getTexture("PNG", new FileInputStream(new File(ScreenSplashPath.ScreenSplash.CAMP.getScreenSplashPath())));
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
    }

    /**
     * Method used to load splash screen in to OpenGL
     */
    private void loadSplashScreen(){
        glClear(GL_COLOR_BUFFER_BIT);
        splashScreen.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex2i(Display.getWidth(), 0); // Upper-left
        glTexCoord2f(0, 0);
        glVertex2i(0, 0); // Upper-right
        glTexCoord2f(0, 1);
        glVertex2i(0, Display.getHeight()); // Bottom-right
        glTexCoord2f(1, 1);
        glVertex2i(Display.getWidth(), Display.getHeight()); // Bottom-left
        glEnd();
    }

    /**
     * Method used to load terrains into loader class.
     */
    private void loadTerrain(){
        Utils.logWhitTime("Start loading terrain.", World.class.getName());
        terrainToLeft = worldElements.loadTerrain(loader, -1, -1);
        terrainToRight = worldElements.loadTerrain(loader, 0, -1);
        terrainBackLeft = worldElements.loadTerrain(loader, -1, 0);
        terrainBackRight = worldElements.loadTerrain(loader, 0, 0);
        Utils.logWhitTime("Finish loading terrain.", World.class.getName());
    }

    /**
     * Method used to load all objects in to OpenGl.
     */
    private void loadAllObjects(){
        Utils.logWhitTime("Start loading objects.", World.class.getName());
        Light sun = new Light(new Vector3f(3000, 6000, 3000), new Vector3f(0.85f, 0.85f, 0.85f));

        TextureModel humanPlayer = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.HUMAN.getModelPath(),
                ObjectFilePaths.TexturePath.HUMAN_SKIN.getTexturePath(), 1, false, false);
        TextureModel tree = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.TREE.getModelPath(),
                ObjectFilePaths.TexturePath.TRUNK_DARK.getTexturePath(), 1, false, false);
        TextureModel treeFoliage = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.TREE_FOLIAGE.getModelPath(),
                ObjectFilePaths.TexturePath.TREE_FOLIAGE.getTexturePath(), 1,false,false);
        TextureModel fern = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.FERN.getModelPath(),
                ObjectFilePaths.TexturePath.FERN.getTexturePath(), 2, true, true);
        TextureModel flower = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.GRASS.getModelPath(),
                ObjectFilePaths.TexturePath.FLOWER.getTexturePath(), 1, true, true);
        TextureModel grass = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.GRASS.getModelPath(),
                ObjectFilePaths.TexturePath.GRASS.getTexturePath(), 1, true, true);

        TextureModel cat = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.CAT.getModelPath(),
                ObjectFilePaths.TexturePath.CAT_SKIN.getTexturePath(), 1, false, false);
        TextureModel wolf = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.WOLF.getModelPath(),
                ObjectFilePaths.TexturePath.WOLF_SKIN.getTexturePath(), 1, false, false);

        TextureModel stone = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.STONE.getModelPath(),
                ObjectFilePaths.TexturePath.STONE.getTexturePath(), 1, false, false);
        TextureModel wood = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.WOOD.getModelPath(),
                ObjectFilePaths.TexturePath.TRUNK.getTexturePath(), 1, false, false);
        TextureModel apple = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.APPLE.getModelPath(),
                ObjectFilePaths.TexturePath.RED_APPLE.getTexturePath(), 1, false, false);

        List<TextureModel> humanAnimation  =  new ArrayList<>();
        for (int i = 1; i<AnimationFilePaths.ModelPath.HUMAN.getNrOfFrames();i++){
            TextureModel human = worldElements.loadModels(loader, AnimationFilePaths.ModelPath.HUMAN.getModelPath()+i,
                    AnimationFilePaths.TexturePath.HUMAN_SKIN.getTexturePath(),1,false,false);
            humanAnimation.add(human);
        }

        final List<TextureModel> textureModels = new ArrayList<>();
        textureModels.add(tree);
        textureModels.add(treeFoliage);
        textureModels.add(fern);
        textureModels.add(flower);
        textureModels.add(grass);

        final List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrainToRight);
        terrains.add(terrainToLeft);
        terrains.add(terrainBackRight);
        terrains.add(terrainBackLeft);

        final List<TextureModel> items = new ArrayList<>();
        items.add(stone);
        items.add(wood);
        items.add(apple);

        final List<TextureModel> aiPlayersModel = new ArrayList<>();
        aiPlayersModel.add(cat);
        aiPlayersModel.add(wolf);

        Utils.logWhitTime("Finish loading objects.",World.class.getName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadWorld(textureModels, terrains, aiPlayersModel, items);
            }
        }).start();

        Utils.logWhitTime("Continue thread 1.",World.class.getName());

        campFire = generateCampFire(terrainToRight.getHeightOfTerrain(10, -30));
        player = new Player(humanPlayer, new Vector3f(1, terrainToRight.getHeightOfTerrain(1,-1), -1), 0, 0, 0, 0.5f, 0.06f);
        camera = new Camera(player);
        lights = worldElements.loadLights(lights, sun);
    }

    private void loadAllGui(){
        menuTexture=new GuiTexture(loader.loadTexture(GuiTextureFilePath.GuiTexturePath.MENU_FRAME.getGuiTexturePath()),
                new Vector2f(0.8f,-0.2f),new Vector2f(0.47f,1f));
        itemSelection = new GuiTexture(loader.loadTexture(GuiTextureFilePath.GuiTexturePath.INVENTORY_SELECTION.getGuiTexturePath()),
                new Vector2f(0.07f, 0.1f));
        GuiTexture healthBar = new GuiTexture(loader.loadTexture(GuiTextureFilePath.GuiTexturePath.HEALTH_BAR.getGuiTexturePath())
                , new Vector2f(-0.65f, -0.8f), new Vector2f(0.30f, 0.20f));

        guiTextureList.add(healthBar);
    }

    /**
     * Method used to load all water on terrains.
     */
    private void loadWaterTiles(){
        searchForHolesOnMap(terrainToRight.getSIZE());
        searchForHolesOnMap(terrainToLeft.getSIZE());
        searchForHolesOnMap(terrainBackRight.getSIZE());
        searchForHolesOnMap(terrainBackLeft.getSIZE());
    }

    /**
     * Method used to load and setup build menu.
     */
    private void loadBuildMenu(){
        List<GuiTexture> handAxe = EnumTechnology.BuildItems.HAND_AXE.getGuiTextureList(EnumTechnology.BuildItems.HAND_AXE.getName());
        List<GuiTexture> campFire = EnumTechnology.BuildItems.CAMP_FIRE.getGuiTextureList(EnumTechnology.BuildItems.CAMP_FIRE.getName());

        buildMenu = MenuLogic.getInstance().addTechnology(EnumTechnology.BuildItems.HAND_AXE.getName(),handAxe);
        buildMenu = MenuLogic.getInstance().addTechnology(EnumTechnology.BuildItems.CAMP_FIRE.getName(),campFire);
    }

    /**
     * Method used to detect all gourd level less than SEA_LEVEL constant and cover it whit water.
     *
     * @param size size of water patch , which is covering the hole in ground
     */
    private void searchForHolesOnMap(float size){
        waterTileList.add(new WaterTile(size / 2, size / 2, HumansConstants.SEA_LEVEL));
    }

    /**
     * Method used to implement key logic.
     */
    private synchronized void keyBoardListener(){
        boolean itemTook = false;
        if (Keyboard.isKeyDown(Keyboard.KEY_E)){
            for (Entity tookableItem : tookableItems) {
                if (Utils.entityWhitenParams(player, tookableItem)) {
                    if (tookableItem.isShow()){
                        itemTook = true;
                    }
                    inventory.addToInventory(tookableItem);
                    tookableItem.setShow(false);

                    guiItemsList = inventory.getAllGuiItems();
                    if (itemTook){
                        break;
                    }
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B) && !Keyboard.isRepeatEvent() && !keyPressed){
                    keyPressed = true;
                    if (buildMenuOn){
                        guiTextureList.remove(menuTexture);
                        buildMenuOn = false;
                        Utils.logWhitTime("Remove Menu", World.class.getName());
                    }else {
                        guiTextureList.add(menuTexture);
                        buildMenuOn = true;
                        Utils.logWhitTime("Add Menu", World.class.getName());
                    }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    keyPressed = false;
                }
            }).start();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
            if (inventory.isContainingItems()) {
                shadowItems = inventory.getFromInventory();
            }
        }

        if (Mouse.isButtonDown(0)){
            if (shadowItems != null) {
//                inventory.getFromInventoryByName(shadowItems.getName());
                guiItemsList = inventory.getAllGuiItems();

                shadowItems = null;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_R)){
            if (shadowItems !=null) {
                shadowItems.setRotY(shadowItems.getRotY()+2f);
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            inventory.moveItemSelectorToLeft();
            guiItemsList = inventory.getAllGuiItems();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            inventory.moveItemSelectorToRight();
            guiItemsList = inventory.getAllGuiItems();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            System.exit(-1);
        }
    }

    /**
     * Method which implements a mouse listener.
     */
    private synchronized void mouseListener(){

            if (Mouse.isButtonDown(Mouse.getDX()) && buildMenuOn && !mouseClicked) {
                BuildTechnology buildTechnology = MenuLogic.getInstance().checkMenuItemSelected(Mouse.getX(), Mouse.getY());

//                inventoryLogic.addItemInPouch(buildTechnology);

                mouseClicked = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mouseClicked = false;
                    }
                }).start();
            }
    }

    /**
     * Method which add entity in world.
     *
     * @param modelsList textured models list ready to render in world
     * @param terrainList terrain list on which models will be added
     */
    private void addEntityInWorld(List<TextureModel> modelsList, List<Terrain> terrainList){
        Utils.logWhitTime("Start generate multiple models.", World.class.getName());

        List<TextureModel> trees = new ArrayList<>();
        trees.add(modelsList.get(0));
        trees.add(modelsList.get(1));

        List<TextureModel> frens = new ArrayList<>();
        frens.add(modelsList.get(2));

        List<TextureModel> flowers = new ArrayList<>();
        flowers.add(modelsList.get(3));

        List<TextureModel> grasses = new ArrayList<>();
        grasses.add(modelsList.get(4));

        for (Terrain terrain : terrainList) {
            worldElements.generateGroupOfEntities(HumansConstants.MAX_NR_OF_TREES_IN_FOREST, trees, terrain, 1,
                    HumansConstants.MAX_NR_OF_FOREST_ON_SECTION_WORLD, smallGirdInstance,"forest",0.2f);
            worldElements.generateRandomEntities(trees, HumansConstants.MAX_NR_OF_ROUGE_TREES_PER_SECTION,
                    terrain, 1, smallGirdInstance,"tree",0.2f);
            worldElements.generateGroupOfEntities(HumansConstants.MAX_NR_OF_FERNS_IN_GROUP, frens, terrain, 4,
                    HumansConstants.MAX_NR_OF_GROUP_FERNS_ON_SECTION, smallGirdInstance,"frens",0f);
            worldElements.generateGroupOfEntities(HumansConstants.MAX_NR_OF_FLOWERS_IN_GROUP, flowers, terrain, 1,
                    HumansConstants.MAX_NR_OF_GROUPS_FLOWERS_ON_SECTION, smallGirdInstance,"flowers",0f);
            worldElements.generateGroupOfEntities(HumansConstants.MAX_NR_OF_GRASS_IN_GROUP, grasses, terrain, 1,
                    HumansConstants.MAX_NR_OF_GRASS_GROUPS_ON_SECTION, smallGirdInstance,"grasses",0f);
        }

        Utils.logWhitTime("Finish generating multiple models.", World.class.getName());
    }

    /**
     * Method used to add items in world
     *
     * @param modelList list of item models ready to added in world
     * @param terrainList terrain list on which items will be added
     */
    private synchronized void addItemsInWorld(List<TextureModel> modelList, List<Terrain> terrainList){
        Utils.logWhitTime("Start generate items in world.", World.class.getName());
        TextureModel stone = modelList.get(0);
        TextureModel wood = modelList.get(1);
        TextureModel apple = modelList.get(2);

        List<Entity> trees = smallGirdInstance.getAllEntitiesByName("tree");

        for (Terrain terrain : terrainList) {
            tookableItems.addAll(worldElements.generateRandomItems(stone, HumansConstants.MAX_STONE_ITEM_IN_WORLD,
                    terrain, 1, smallGirdInstance, EnumsEntityGui.Items.STONE.getItemName(),0));
            tookableItems.addAll(worldElements.generateRandomItems(wood, HumansConstants.MAX_WOOD_ITEM_IN_WORLD,
                    terrain, 1, smallGirdInstance, EnumsEntityGui.Items.WOOD.getItemName(),0));
            tookableItems.addAll(worldElements.generateRandomItemsAroundObjects(apple,
                    HumansConstants.MAX_APPLE_ITEM_AROUND_TREES, trees, terrain, 1, smallGirdInstance,
                    EnumsEntityGui.Items.APPLE.getItemName(),0));
        }

//        inventoryLogic = InventoryLogic.getInventoryLogic(tookableItems,allGuiItems,fontList,itemSelection);
        inventory = Inventory.getInstance(loader);
        Utils.logWhitTime("Finish generating items. Number of items: "+tookableItems.size(), World.class.getName());
    }

    /**
     * Method used to generate a campfie in world
     *
     * @param pozitionY height of campfire in world
     * @return campfire entity after adding light to campfire and position
     */
    private Entity generateCampFire(float pozitionY){
        TextureModel campfire = worldElements.loadModels(loader, ObjectFilePaths.ModelPath.CAMPFIRE.getModelPath(),
                ObjectFilePaths.TexturePath.TRUNK.getTexturePath(), 1, true, true);
        Entity campfireEntity = new Entity(campfire, new Vector3f(10, pozitionY+0.1f, -30), 0f, 0f, 0f,0.35f,0.1f);
        Light light =new Light(new Vector3f(10,1f,-30),new Vector3f(2.55f,1.53f,0.51f),new Vector3f(2, 0.5f, 0.002f));
        lights.add(light);

        return campfireEntity;
    }
}
