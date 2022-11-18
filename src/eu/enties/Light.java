package eu.enties;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by adrian on 26.10.2014.
 */
public class Light {

    private Vector3f pozition;
    private Vector3f color;
    private Vector3f attenuation= new Vector3f(1,0,0);

    public Light(Vector3f pozition, Vector3f color) {
        this.pozition = pozition;
        this.color = color;
    }

    public Light(Vector3f pozition, Vector3f color, Vector3f attenuation) {
        this.pozition = pozition;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getPozition() {
        return pozition;
    }

    public void setPozition(Vector3f pozition) {
        this.pozition = pozition;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }
}
