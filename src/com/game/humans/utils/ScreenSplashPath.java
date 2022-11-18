package com.game.humans.utils;

/**
 * Created by adrian on 3/13/15.
 */
public class ScreenSplashPath {

    private static final String screenSplash="res/game/splashscreen/";

    public enum ScreenSplash{
        CAMP(screenSplash + "camp.png"),
        TIGER(screenSplash + "tiger.png");

        private String screenSplashPath;
        ScreenSplash (String screenSplashPath){
            this.screenSplashPath=screenSplashPath;
        }

        public String getScreenSplashPath() {
            return screenSplashPath;
        }
    }
}
