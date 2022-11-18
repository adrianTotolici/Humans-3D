/**
 * When I wrote this, only God and I understood what I was doing
 * Now only God knows.
 */
package com.game.humans;

import com.game.humans.utils.Utils;
import com.game.humans.world.World;

/**
 * Main game class.
 */
public class Main {

    /**
     * Main method of the game, in witch the world is generated.
     * @param arg default arg (are not in used for now)
     */
    public static void main(String[] arg){
        try {
            World.getWorldInstance().generateTheWorld();
        }catch (Exception exception){
            Utils.sendMail(exception);
        }
    }
}