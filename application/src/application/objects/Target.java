package application.objects;

public interface Target {

    String getTexturePath();

    int getNbTargets();

    float[] getTarget(int sealIndex, float[] vector);

    float getSize();

    float getRatio();

    float getViewDistance();
}
