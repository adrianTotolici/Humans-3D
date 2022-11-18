/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */
package com.game.humans.menu.build;

public class EnumMenuItems {

    public enum ItemsBuildMenu{
        ONE(0.45f,0.7f),
        TWO(0.45f,0.55f),
        THREE(0.45f,0.45f);

        float pozXimageItemBuild;
        float pozYimageItemBuild;

        ItemsBuildMenu(float xPoz, float yPoz){
            this.pozXimageItemBuild = xPoz;
            this.pozYimageItemBuild = yPoz;
        }

        public float getPozXimageItemBuild() {
            return pozXimageItemBuild;
        }

        public float getPozYimageItemBuild() {
            return pozYimageItemBuild;
        }
    }
}
