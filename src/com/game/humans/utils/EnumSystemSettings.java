package com.game.humans.utils;

/**
 * Class used to set system after starting game
 */
public class EnumSystemSettings {

    /** OpenGl and GLSL version associated */
    public enum OpenGlVersion{
        Gl20("2.0","glsl110"),
        Gl21("2.1","glsl120"),
        Gl30("3.0","glsl130"),
        Gl31("3.1","glsl140"),
        Gl32("3.2","glsl150"),
        Gl33("3.3","glsl330"),
        Gl40("4.0","glsl400"),
        Gl41("4.1","glsl410"),
        Gl42("4.2","glsl420"),
        Gl43("4.3","glsl430"),
        Gl44("4.4","glsl440"),
        Gl45("4.5","glsl450"),
        Gl46("4.6","glsl460");

        private String version;
        private String glslVersion;

        OpenGlVersion(String version, String glslVersion) {
            this.version = version;
            this.glslVersion = glslVersion;
        }

        /**
         * Method used to get OpenGl version used
         * @return openGl version as string
         */
        public String getVersion() {
            return version;
        }

        /**
         * Method used to get GLSL associated version whit installed OpenGL
         * @return GLSL vesrion as string
         */
        public String getGlslVersion() {
            return glslVersion;
        }
    }

}
