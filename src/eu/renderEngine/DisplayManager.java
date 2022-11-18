package eu.renderEngine;

import eu.toolBox.EngineConstants;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Class used to create a manager for window
 */
public class DisplayManager {

    private static long lastFrameTime;
    private static float delta;

    /**
     * Method used to create a custom sized window
     *
     * @param width , width of the window
     * @param height , height of the window
     * @param windowTitle , title of the window
     */
    public static void createDisplay(int width, int height, String windowTitle){
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            Display.setTitle(windowTitle);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, width, height);
        lastFrameTime=getCurrentTime();

    }

    /**
     * Method used to create a full screen window
     *
     * @param windowTitle , title of the window
     */
    public static void createFullScreenDisplay(String windowTitle){
        try {
            Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());

            Display.create();
            Display.setTitle(windowTitle);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0,Display.getWidth(),Display.getHeight());
        lastFrameTime = getCurrentTime();
    }

    /**
     * Method used to update content of a window
     */
    public static void updateDisplay(){
        Display.sync(EngineConstants.FRAMES_PER_SECONDS);
        Display.update();
        long currentFrameTime=getCurrentTime();
        delta=(currentFrameTime-lastFrameTime)/1000f;
        lastFrameTime=currentFrameTime;
    }

    /**
     * Method used to get frames per second
     * @return float value whit frames per second
     */
    public static float getFrameTimeSeconds() {
        return delta;
    }

    /**
     * Method used to close a window
     */
    public static void closeDisplay(){
        Display.destroy();
    }

    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

}
