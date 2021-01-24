package application.viewer.glfw;

import jscenegraph.database.inventor.SbVec3f;

public interface ForceProvider {

    enum Direction {
        STILL,
        FRONT,
        BACK,
        LEFT,
        RIGHT
    }

    void apply(SbVec3f force, Direction direction);
}
