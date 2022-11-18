package com.game.humans.world;

/**
 * Created by adrian on 4/1/15.
 */
public class AnimationFilePaths {
    private static final String objModelsAnimation="game/animation/";
    private static final String objTextures="game/objects/textures/";

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
        HUMAN(objModelsAnimation+"human/human_",9);

        private String modelPath;
        private int nrOfFrames;
        ModelPath(String modelPath, int nrOfFrames){
            this.modelPath = modelPath;
            this.nrOfFrames = nrOfFrames;
        }

        public String getModelPath(){
            return modelPath;
        }

        public int getNrOfFrames(){
            return nrOfFrames;
        }
    }
}
