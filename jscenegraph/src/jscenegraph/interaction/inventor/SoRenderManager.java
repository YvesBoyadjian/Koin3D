package jscenegraph.interaction.inventor;

import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.port.Destroyable;

public class SoRenderManager implements Destroyable {

    SoGLRenderAction glaction;

    public SoRenderManager() {
        glaction = new SoGLRenderAction(new SbViewportRegion((short)400, (short)400));
    }

/*!
  Update our SoGLRenderAction's viewport settings.

  This will change \e both the information about window dimensions and
  the actual viewport size and origin.

  \sa setWindowSize()
*/
    public void setViewportRegion(SbViewportRegion newregion)
    {
        glaction.setViewportRegion(newregion);
    }
/*!
  Returns current viewport region used by the render action and the
  event handling.

  \sa setViewportRegion()
*/
public SbViewportRegion getViewportRegion()
    {
        return glaction.getViewportRegion();
    }

    @Override
    public void destructor() {

    }
}
