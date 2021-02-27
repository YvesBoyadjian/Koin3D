package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.inventor.threads.SbMutex;
import jscenegraph.port.Destroyable;

public class SoVRMLPixelTextureP implements Destroyable {
    public
    SoGLImage glimage; //ptr
    public boolean glimagevalid;
    int readstatus;

//#ifdef COIN_THREADSAFE
    final SbMutex glimagemutex = new SbMutex();
    void lock_glimage() { this.glimagemutex.lock(); }
    void unlock_glimage() { this.glimagemutex.unlock(); }

    @Override
    public void destructor() {
        glimagemutex.destructor();
    }
//#else // !COIN_THREADSAFE
//    void lock_glimage(void) { }
//    void unlock_glimage(void) { }
//#endif // !COIN_THREADSAFE
}
