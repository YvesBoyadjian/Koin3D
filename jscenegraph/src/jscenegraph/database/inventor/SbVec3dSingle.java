package jscenegraph.database.inventor;

import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

public class SbVec3dSingle extends SbVec3d {

    public SbVec3dSingle(double f, double g, double h) {
        super(f,g,h);
    }

    public SbVec3dSingle() {
        super();
    }

    public SbVec3dSingle(SbVec3d other) {
        super(other);
    }

    public final double[] getValue() {
        return getValueRef();
    }

    public final DoubleMemoryBuffer getValueBuffer() {
        return vec;
    }
}
