package eu.renderEngine.models;

/**
 * Class used to store raw models without texture clamped to it.
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;

    /**
     * Constructor of raw model.
     *
     * @param vaoID vector array ordinate ID
     * @param vertexCount vertex count
     */
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Method used to get ID
     *
     * @return the ID of raw model ID
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     * Method used to get the vertex count
     *
     * @return the vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
