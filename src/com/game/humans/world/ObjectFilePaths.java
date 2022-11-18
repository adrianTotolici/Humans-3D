package com.game.humans.world;

/**
 * Created by adrian on 2/25/15.
 */
public class ObjectFilePaths {

    private static final String objTextures="game/objects/textures/";
    private static final String objModels="game/objects/models/";

    public enum TexturePath{
        HUMAN_SKIN(objTextures+"humans/human_skin2"),
        TRUNK(objTextures+"trees/trunk"),
        FERN(objTextures+"grass/fern"),
        FLOWER(objTextures+"grass/flower"),
        GRASS(objTextures+"grass/grass"),
        CAT_SKIN(objTextures+"animals/cat"),
        WOLF_SKIN(objTextures+"animals/wolf"),
        TRUNK_DARK(objTextures+"trees/darkBark"),
        TREE_FOLIAGE(objTextures+"leefs/leafs"),
        RED_APPLE(objTextures+"food/appleSkin"),
        STONE(objTextures+"stone/stone");

        private String texturePath;
        TexturePath(String texturePath){
            this.texturePath=texturePath;
        }

        public String getTexturePath(){
            return texturePath;
        }
    }

    public enum ModelPath{
        HUMAN(objModels+"humans/human"),
        TREE(objModels+"trees/tree"),
        FERN(objModels+"grass/fern"),
        GRASS_PATCH_SMALL(objModels+"grass/grass_patch_small"),
        GRASS_PATCH_LARGE(objModels+"grass/grass_patch_large"),
        GRASS(objModels+"grass/grass"),
        CAT(objModels+"animals/cat"),
        WOLF(objModels+"animals/wolft"),
        STONE(objModels+"stones/stone"),
        TREE_FOLIAGE(objModels+"grass/treeFoliage"),
        WOOD(objModels+"wood/woodTrunk"),
        APPLE(objModels+"food/apple"),
        CAMPFIRE(objModels+"campfires/campfire");


        private String modelPath;
        ModelPath(String modelPath){
            this.modelPath=modelPath;
        }

        public String getModelPath(){
            return modelPath;
        }
    }

}
