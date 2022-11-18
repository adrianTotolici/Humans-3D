package com.game.humans.utils;

public class EnumsEntityGui {

    public enum Items{
        STONE("stone"),
        WOOD("wood"),
        APPLE("apple"),
        HAND_AXE("hand_axe"),
        FIRE_CAMP("fire_camp");

        private String itemName;

        Items(String itemName){
            this.itemName = itemName;
        }

        public String getItemName(){
            return itemName;
        }
    }

    public enum Position {
        ONE(-0.30f,-0.9f),
        SECOND(-0.15f,-0.9f),
        THIRD(0,-0.9f),
        FORTH(0.15f,-0.9f);

        private float xPoz;
        private float yPoz;

        Position(float xPoz, float yPoz){
            this.xPoz = xPoz;
            this.yPoz = yPoz;
        }

        public float getxPoz() {
            return xPoz;
        }

        public float getyPoz() {
            return yPoz;
        }
    }
}
