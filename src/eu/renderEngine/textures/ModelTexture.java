package eu.renderEngine.textures;

/**
 * Created by adrian on 07.09.2014.
 */
public class ModelTexture {

    private int textureID;
    private float shineDamper=1;
    private float reflectivity=0;

    private  boolean hasTransaparency=false;
    private  boolean useFakeLightung=false;

    private int numberOfRows = 1;

    public ModelTexture(int id){
        this.textureID=id;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransaparency() {
        return hasTransaparency;
    }

    public void setHasTransaparency(boolean hasTransaparency) {
        this.hasTransaparency = hasTransaparency;
    }

    public boolean isUseFakeLightung() {
        return useFakeLightung;
    }

    public void setUseFakeLightung(boolean useFakeLightung) {
        this.useFakeLightung = useFakeLightung;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
