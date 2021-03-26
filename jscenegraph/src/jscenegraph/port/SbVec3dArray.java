package jscenegraph.port;

import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3dSingle;
import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class SbVec3dArray extends Indexable<SbVec3d> implements ByteBufferAble, DoubleBufferAble, Destroyable {

    DoubleMemoryBuffer valuesArray;

    int delta;

    SbVec3d dummy;

    //private DoubleBuffer[] doubleBuffer = new DoubleBuffer[1];

    public SbVec3dArray(SbVec3dArray other) {
        this(other,0);
    }

    public SbVec3dArray(SbVec3dArray other, int delta) {
        valuesArray = other.valuesArray;
        this.delta = other.delta + delta;
        //this.doubleBuffer = other.doubleBuffer;
    }

    public SbVec3dArray(DoubleMemoryBuffer valuesArray) {
        this.valuesArray = valuesArray;
    }

    public SbVec3dArray(SbVec3dSingle singleSbVec3d) {
        valuesArray = singleSbVec3d.getValueBuffer();
    }
/* TODO
    public SbVec3dArray(MutableSbVec3dArray other) {
        valuesArray = other.getValuesArray();
        this.delta = other.getDelta();
    }

    public SbVec3dArray(MutableSbVec3dArray other, int delta) {
        valuesArray = other.getValuesArray();
        this.delta = other.getDelta()+ delta;
    }
*/
    public static SbVec3dArray copyOf(SbVec3dArray other) {
        if(other == null) {
            return null;
        }
        SbVec3dArray copy = new SbVec3dArray(other,0);
        return copy;
    }

    public int getSizeDouble() {
        return valuesArray.numDoubles() - delta*3;
    }

    public SbVec3d get(int index) {
        return new SbVec3d(valuesArray, (index+delta)*3);
    }

    public SbVec3d getFast(int index) {
        if( null == dummy ) {
            dummy = new SbVec3d(valuesArray, (index+delta)*3);
        }
        else {
            dummy.setIndice((index+delta)*3);
        }
        return dummy;
    }

    public double[] get3Doubles(int index, double[] values) {
        values[0] = valuesArray.getDouble((index+delta)*3);
        values[1] = valuesArray.getDouble((index+delta)*3+1);
        values[2] = valuesArray.getDouble((index+delta)*3+2);
        return values;
    }

    public void setValueXYZ(int index, double x, double y, double z) {
        valuesArray.setDouble((index+delta)*3, x);
        valuesArray.setDouble((index+delta)*3+1, y);
        valuesArray.setDouble((index+delta)*3+2, z);
    }

    public SbVec3dArray plus(int delta) {
        return new SbVec3dArray(this,delta);
    }

    public static SbVec3dArray allocate(int maxPoints) {
        return new SbVec3dArray(DoubleMemoryBuffer.allocateDoubles(maxPoints*3));
    }

    public DoubleArray toDoubleArray() {
        return new DoubleArray(delta*3,valuesArray);
    }

    @Override
    public DoubleBuffer toDoubleBuffer() {

        DoubleBuffer fb = valuesArray.toByteBuffer().asDoubleBuffer();

        int offset = delta*3;

        fb.position(offset);

        return fb;

//		int length = valuesArray.length - offset;
//		if(doubleBuffer[0] == null || doubleBuffer[0].capacity() != length) {
//			doubleBuffer[0] = BufferUtils.createDoubleBuffer(length);
//		//}
//		doubleBuffer[0].clear();
//		doubleBuffer[0].put(valuesArray, offset, length);
//		doubleBuffer[0].flip();
//		}
//		return doubleBuffer[0];//DoubleBuffer.wrap(valuesArray,offset, length);
    }

    public DoubleMemoryBuffer getValuesArray() {
        return valuesArray;
    }

//	public DoubleBuffer[] getValuesBuffer() {
//		return doubleBuffer;
//	}

    int getDelta() {
        return delta;
    }

    public static SbVec3dArray fromArray(SbVec3d[] arrayPtr) {
        int length = arrayPtr.length;
        DoubleMemoryBuffer valuesArray = DoubleMemoryBuffer.allocateDoubles(length*3);
        int indice=0;
        for(int i=0; i< length; i++) {
            valuesArray.setDouble(indice++, arrayPtr[i].getX());
            valuesArray.setDouble(indice++, arrayPtr[i].getY());
            valuesArray.setDouble(indice++, arrayPtr[i].getZ());
        }
        SbVec3dArray retVal = new SbVec3dArray(valuesArray);
        return retVal;
    }

    public void copyIn(DoubleBuffer doubleBuffer) {
        int offset = delta*3;
        int length = valuesArray.numDoubles() - offset;
        doubleBuffer.put(valuesArray.toDoubleArray(), offset, length);
        doubleBuffer.flip();
    }

    public DoubleBuffer toDoubleBuffer(int index) {
        DoubleBuffer fb = valuesArray.toDoubleBuffer();
        fb.position((delta+index)*3);
        return fb;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        ByteBuffer bb = valuesArray.toByteBuffer();
        bb.position(delta*3*Double.BYTES);
        return bb.slice();
    }

    @Override
    public SbVec3d getO(int index) {
        return get(index);
    }

    @Override
    public int length() {
        return valuesArray.numDoubles()/3 - delta;
    }

    @Override
    public void setO(int index, SbVec3d object) {
        getO(index).copyFrom(object);

    }

    @Override
    public int delta() {
        return delta;
    }

    @Override
    public Object values() {
        return valuesArray;
    }

    @Override
    public void destructor() {
        if (delta != 0) {
            throw new IllegalArgumentException();
        }
        Destroyable.delete(valuesArray);
        valuesArray = null;
        dummy = null;
    }

    /**
     * @param num
     * @param source
     */
    public void copy(int num, Indexable<SbVec3d> source) {
        if( source instanceof SbVec3dArray) {
            SbVec3dArray source_ = (SbVec3dArray)source;
            DoubleMemoryBuffer.arraycopy(source_.valuesArray, source_.delta * 3, valuesArray, delta * 3, num * 3);
        }
        else {
            super.copy(num,source);
        }
    }
}
