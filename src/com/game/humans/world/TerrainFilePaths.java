package com.game.humans.world;

public class TerrainFilePaths {

    private static final String TERRAIN_PATH = "game/terrain";

    public enum TexturePath{
        CLUBER_STONE(TERRAIN_PATH+"/texture/cluber_stone"),
        DIRT(TERRAIN_PATH+"/texture/dirt"),
        GRASS(TERRAIN_PATH+"/texture/grass"),
        PAVEMENT(TERRAIN_PATH+"/texture/pavement");

        private String texturePath;
        TexturePath(String texturePath){
            this.texturePath=texturePath;
        }

        public String getTexturePath(){
            return texturePath;
        }
    }

    public enum BlendmapsPath {
        BLENDMAP(TERRAIN_PATH+"/blendmaps/blendmap");

        private String blendmapPath;
        BlendmapsPath(String blendmapPath){
            this.blendmapPath=blendmapPath;
        }

        public String getBlendmapPath(){
            return blendmapPath;
        }
    }

    public enum HeightmapsPath {
        HEIGHTMAP(TERRAIN_PATH+"/heightmaps/heightmap");

        private String heightmapPath;
        HeightmapsPath(String heightmapPath){
            this.heightmapPath=heightmapPath;
        }

        public String getHeightmapPath(){
            return heightmapPath;
        }
    }

    public enum DuDvMaps{
        WATER_DUDV(TERRAIN_PATH+"/dudvmap/waterDUDV");

        private String waterDUDVPath;

        DuDvMaps(String waterDUDVPath) {
            this.waterDUDVPath = waterDUDVPath;
        }

        public String getWaterDUDVPath() {
            return waterDUDVPath;
        }
    }
}
