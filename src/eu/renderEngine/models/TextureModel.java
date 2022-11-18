package eu.renderEngine.models;

import eu.renderEngine.textures.ModelTexture;

/**
 * Class used for storing model whit textures
 */
public class TextureModel {

    private RawModel rawModel;
    private ModelTexture texture;

    /**
     * Constructor of textured model class
     *
     * @param rawModel , object model without texture applied to it
     * @param texture, texture for the model
     */
    public TextureModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    /**
     * Method return raw model used for applying texture on it
     *
     * @return raw model
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * Method used to get texture which is applied on the model
     *
     * @return texture applied on the model
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
