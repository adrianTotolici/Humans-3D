package com.game.humans.utils;

/**
 * Created by adrian on 3/18/15.
 */
public class FontsFilePath {

    private static final String fontsPath = "res/game/fonts/";

    public enum FontPath{
        ALA_CARTE(fontsPath+"alaCarte.ttf"),
        BUDMO_JIGGLER(fontsPath+"budmoJiggler.ttf"),
        CHLORINAR(fontsPath+"chlorinar.ttf"),
        DRIFT_TYPE(fontsPath+"driftType.ttf"),
        AMAZON(fontsPath+"amazon.ttf"),
        HEAVY_HEAP(fontsPath+"heavyHeap.ttf");

        private String fontPath;
        FontPath(String fontPath){
            this.fontPath=fontPath;
        }

        public String getFontPath(){
            return fontPath;
        }
    }
}
