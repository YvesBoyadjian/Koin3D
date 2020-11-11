package application.viewer.glfw;

import jscenegraph.database.inventor.SbVec3f;

public interface ForceProvider {

    void apply(SbVec3f force);
}
