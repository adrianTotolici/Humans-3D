package com.game.humans.world;

import com.game.humans.utils.HumansConstants;
import com.game.humans.terrain.SmallGird;
import com.game.humans.utils.Utils;
import eu.enties.AiPlayer;
import eu.enties.Entity;
import eu.enties.Light;
import eu.renderEngine.Loader;
import eu.renderEngine.ModelData;
import eu.renderEngine.OBJLoader;
import eu.renderEngine.models.RawModel;
import eu.renderEngine.models.TextureModel;
import eu.renderEngine.terrains.Terrain;
import eu.renderEngine.textures.ModelTexture;
import eu.renderEngine.textures.TerrainTexture;
import eu.renderEngine.textures.TerrainTexturePack;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldElements {

    private Random random =new Random();

    public WorldElements() {}

    public Terrain loadTerrain(Loader loader, int gridX, int gridZ){
        TerrainTexture backgroundTexture= new TerrainTexture(loader.loadTexture(TerrainFilePaths.TexturePath.GRASS.getTexturePath()));
        TerrainTexture rTexture=new TerrainTexture(loader.loadTexture(TerrainFilePaths.TexturePath.DIRT.getTexturePath()));
        TerrainTexture gTexture=new TerrainTexture(loader.loadTexture(TerrainFilePaths.TexturePath.CLUBER_STONE.getTexturePath()));
        TerrainTexture bTexture=new TerrainTexture(loader.loadTexture(TerrainFilePaths.TexturePath.PAVEMENT.getTexturePath()));

        TerrainTexturePack terrainTexturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap=new TerrainTexture(loader.loadTexture(TerrainFilePaths.BlendmapsPath.BLENDMAP.getBlendmapPath()));

        return new Terrain(gridX,gridZ,loader,terrainTexturePack,blendMap,TerrainFilePaths.HeightmapsPath.HEIGHTMAP.getHeightmapPath());
    }

    public TextureModel loadModels(Loader loader, String modelPath, String texturePath, int numberofRows,
                                   boolean transparency, boolean useFakeLithing){
        ModelTexture modelTexture=new ModelTexture(loader.loadTexture(texturePath));
        modelTexture.setNumberOfRows(numberofRows);
        modelTexture.setHasTransaparency(transparency);
        modelTexture.setUseFakeLightung(useFakeLithing);
        ModelData modelData= OBJLoader.loadOBJ(modelPath);

        RawModel rawModel= loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(),
                modelData.getNormals(), modelData.getIndices());
        return new TextureModel(rawModel,modelTexture);
    }

    public List<Light> loadLights(List<Light> lights, Light light){
        if (lights == null) {
            lights = new ArrayList<Light>();
        }
        lights.add(light);
        return lights;
    }

    public SmallGird generateGroupOfEntities(int numberOfEntityInGroup, List<TextureModel> models, Terrain terrain,
                                             int textureIndex, int numberOfGroups, SmallGird smallGird, String name,
                                             float collisionRadius){
        for (int j=0; j<numberOfGroups; j++) {

            float x = getRandomFloatBetweenNumbers(terrain.getX(), terrain.getX() + terrain.getSIZE());
            float z = getRandomFloatBetweenNumbers(terrain.getZ(), terrain.getZ() + terrain.getSIZE());

            for (int i = 0; i < numberOfEntityInGroup; i++) {
                float minX = x - numberOfEntityInGroup / 8;
                float maxX = x + numberOfEntityInGroup / 8;

                float minZ = z - numberOfEntityInGroup / 8;
                float maxZ = z + numberOfEntityInGroup / 8;

                float xRand = getRandomFloatBetweenNumbers(minX, maxX);
                float zRand = getRandomFloatBetweenNumbers(minZ, maxZ);

                float y = terrain.getHeightOfTerrain(xRand, zRand);

                if (y > HumansConstants.SEA_LEVEL) {
                    float yRot = random.nextFloat();
                    float scale = getRandomFloatBetweenNumbers(0.6f, 1.5f);

                    for (TextureModel model : models) {
                        Entity entity = new Entity(model, new Vector3f(xRand, y, zRand), 0f, yRot, 0f, scale, textureIndex,collisionRadius);
                        entity.setShow(true);
                        entity.setName(name);
                        entity.setCollision(entity.getCollision());
                        smallGird.addElements(xRand, zRand, entity);
                    }
                }
            }
        }
        return smallGird;
    }

    private float getRandomFloatBetweenNumbers(float min, float max){
        Random r = new Random();
        return (float) (min + (max - min) * r.nextDouble());

    }

    public  SmallGird generateRandomEntities(List<TextureModel> model, int number, Terrain terrain, int textureIndex,
                                             SmallGird smallGird, String name, float collisionRadius){
        Random random=new Random();

        for (int i=0; i<number;i++){
            float xRand = getRandomFloatBetweenNumbers(terrain.getX(), terrain.getX() + terrain.getSIZE());
            float zRand = getRandomFloatBetweenNumbers(terrain.getZ(), terrain.getZ() + terrain.getSIZE());

            float rotY=random.nextFloat()*100;

            float y= terrain.getHeightOfTerrain(xRand,zRand);

            if (y> HumansConstants.SEA_LEVEL) {
                for (TextureModel textureModel : model) {
                    Entity entity = new Entity(textureModel, new Vector3f(xRand, y, zRand), 0f, rotY, 0f, 1f, random.nextInt(textureIndex),collisionRadius);
                    entity.setShow(true);
                    entity.setName(name);
                    entity.setCollision(entity.getCollision());
                    smallGird.addElements(xRand, zRand, entity);
                }
            }
        }

        return smallGird;
    }

    public List<Entity> generateRandomItems(TextureModel model, int number, Terrain terrain, int
                                            textureIndex, SmallGird smallGird, String itemName, float collisionRadius){
        Random random=new Random();
        List<Entity> entities = new ArrayList<Entity>();

        for (int i=0; i<number;i++){
            float xRand = getRandomFloatBetweenNumbers(terrain.getX(), terrain.getX() + terrain.getSIZE());
            float zRand = getRandomFloatBetweenNumbers(terrain.getZ(), terrain.getZ() + terrain.getSIZE());

            float rotY=random.nextFloat()*100;

            float y= terrain.getHeightOfTerrain(xRand,zRand);

            if (y> HumansConstants.SEA_LEVEL) {

                float scale = getRandomFloatBetweenNumbers(HumansConstants.MIN_SCALE_WOOD, HumansConstants.MAX_SCALE_WOOD);

                Entity entity = new Entity(model, new Vector3f(xRand, y, zRand), 0f, rotY, 0f, scale, random.nextInt(textureIndex),collisionRadius);
                entity.setShow(true);
                entity.setName(itemName);
                entity.setCollision(entity.getCollision());
                smallGird.addElements(xRand, zRand, entity);
                entities.add(entity);
            }
        }

        return entities;
    }

    public List<Entity> generateRandomItemsAroundObjects(TextureModel model, int number, List<Entity> objectList, Terrain terrain,
                                                        int textureIndex, SmallGird smallGird, String itemName, float collisionRadius){

        Random random = new Random();
        List<Entity> entities = new ArrayList<Entity>();

        for (Entity object : objectList) {

            Vector3f position = object.getPosition();
            float x = position.getX();
            float z = position.getZ();

            float minX = x - number;
            float maxX = x + number;

            float minZ = z - number;
            float maxZ = z + number;


            for (int i = 0; i < number; i++) {

                float xRand = getRandomFloatBetweenNumbers(minX, maxX);
                float zRand = getRandomFloatBetweenNumbers(minZ, maxZ);

                float y = terrain.getHeightOfTerrain(xRand, zRand);

                if (y> HumansConstants.SEA_LEVEL) {
                    float yRot = random.nextFloat();
                    float zRot = random.nextFloat();
                    float xRot = random.nextFloat();

                    float scale = getRandomFloatBetweenNumbers(HumansConstants.MIN_SCALE_APPLE, HumansConstants.MAX_SCALE_APPLE);

                    Entity entity = new Entity(model, new Vector3f(xRand, y, zRand), xRot, yRot, zRot, scale, random.nextInt(textureIndex),collisionRadius);
                    entity.setShow(true);
                    entity.setName(itemName);
                    entity.setCollision(entity.getCollision());
                    smallGird.addElements(xRand, zRand, entity);
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public List<AiPlayer> generateRandomAiPLayers(TextureModel model, int number, Terrain terrain){
        List<AiPlayer> aiPlayersList = new ArrayList<AiPlayer>();

        Random random=new Random();


        for (int i=0; i<number;i++){
            float xRand = getRandomFloatBetweenNumbers(terrain.getX(), terrain.getZ());
            float zRand = getRandomFloatBetweenNumbers(terrain.getX(), terrain.getZ());

            if (terrain.getX()<0){
                xRand=xRand*(-1);
            }
            if (terrain.getZ()<0){
                zRand=zRand*(-1);
            }

            float rotY=random.nextFloat()*100;

            float y= terrain.getHeightOfTerrain(xRand,zRand);

            if (y> HumansConstants.SEA_LEVEL) {
                aiPlayersList.add(new AiPlayer(model, new Vector3f(xRand, y, zRand), 0f, rotY, 0f, 0.6f, 0.05f));
            }
        }
        return aiPlayersList;
    }

}
