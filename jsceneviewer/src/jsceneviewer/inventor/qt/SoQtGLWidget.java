/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 *
 *  http://www.sgi.com
 *
 *  For further information regarding this notice, see:
 *
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */

/*
 * Copyright (C) 1990-93   Silicon Graphics, Inc.
 * Author(s): Alain Dumesny, David Mott
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */

package jsceneviewer.inventor.qt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.opengl.GLCanvas;
//import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.swt.GLCanvas;
import org.lwjgl.opengl.swt.GLData;
import org.lwjgl.system.Callback;
import org.lwjgl.glfw.GLFW;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jsceneviewer.BorderLayout;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQtGLWidget extends Composite implements Destroyable {
	
	private final int style;
	
    //! the real GL widget
	private GLCanvas mainWidget;
    //! the GL format to use
	private GLData glFormat;


    //! currently set cursor for the GL widget
    private Cursor     _glCursor;
    private boolean    _glCursorIsSet;

    //! stores setting of autoBufferSwap
    private boolean _autoBufferSwapOn = true;
    
    //@{ data for external event handling
    private Object      eventCBData;
    private eventCBType eventCB;
    //@}    
    
    protected boolean        autoFocus;        //! widget gets focus on mouse enter

	private int shareGroup;
	protected int shareID;
	    
	private static final SoContextShareManager contextShareManager = new SoContextShareManager();

	private boolean firstVisibility; // java port
	
	private boolean initialized; // java port
	
    private interface eventCBType {
    	boolean run(Object userData, TypedEvent anyevent);
    }
    
	public SoQtGLWidget(Composite parent, int style) {
		super(parent, style);
		this.style = style;
	}
	

	public void makeCurrent()
	{
	    getGLWidget().setCurrent();//makeCurrent(); //TODO verify this
	}
	
	
    public boolean doubleBuffer() {
    	return format().doubleBuffer;
    }
	

public GLData format()
{
    if (mainWidget != null && mainWidget.isVisible()/*isRealized()*/) {
        return (GLData)mainWidget.getGLData();
    } else {
        // return default format
        return glFormat;
    }
}

    
	
    //! Call this to create the real QGLWidget (and all surrounding widgets) after you
    //! have set the GL format/context. This also calls createBorderWidgets().
    public void buildWidget(int style) {
        if (mainWidget == null) {
        	
        	setLayout(new BorderLayout());
        	
            Composite borderWidget;
            borderWidget = createLowerBorder (this);
            if (borderWidget != null) {
            	borderWidget.setLayoutData(new BorderLayout.BorderData(BorderLayout.SOUTH));                	  
                //vertLayout.addWidget (borderWidget);
            }
            borderWidget = createUpperBorder (this);
            if (borderWidget != null) {
            	borderWidget.setLayoutData(new BorderLayout.BorderData(BorderLayout.NORTH));                	  
                //vertLayout.insertWidget (0, borderWidget);
            }
            borderWidget = createRightBorder (this);
            if (borderWidget != null) {
            	borderWidget.setLayoutData(new BorderLayout.BorderData(BorderLayout.EAST));                	  
                //horiLayout.addWidget (borderWidget);
            }
            borderWidget = createLeftBorder (this);
            if (borderWidget != null) {
            	borderWidget.setLayoutData(new BorderLayout.BorderData(BorderLayout.WEST));                	  
                //horiLayout.insertWidget (0, borderWidget);
            }
            
    		GLData capsReqUser = null;
    		GLData capsChooser = null;
    		
            replaceWidget (/*new GLCanvas(this,style,capsReqUser,capsChooser)*/
                contextShareManager.createWidget (glFormat, style, this, shareGroup));
        	mainWidget.setLayoutData(new BorderLayout.BorderData(BorderLayout.CENTER));                	  
        }    	
    }
    
    //! Get pointer to the real GL widget, automatically calls buildWidget
    public GLCanvas  getGLWidget() {
        if (mainWidget == null) {
            buildWidget(style);
        }
        return mainWidget;    	
    }
    
    //@{ methods for cursor management on the real GL widget
    public void setGLCursor (final Cursor cursor)
    {
        _glCursorIsSet = true;
        _glCursor = cursor;
        if (mainWidget != null) {
            mainWidget.setCursor (cursor);
        }
    }    
	
    public void unsetGLCursor()
    {
        _glCursorIsSet = false;
        if (mainWidget != null) {
            //mainWidget.setCursor(new Cursor(getDisplay(),0));
        }
    }

//    public GLContext context() {
//    	return mainWidget.getContext();
//    }
	
    public void swapBuffers() {
    	mainWidget.swapBuffers();
    }
    
    protected boolean autoBufferSwap() {
        return _autoBufferSwapOn;    	
    }
    
    protected void setAutoBufferSwap (boolean on) {
        _autoBufferSwapOn = on;
        //mainWidget.setAutoSwapBufferMode(on);
    }

    //! viewers with same group id try to share display lists
    public int getShareGroup() { return shareGroup; }

    //! return sharing id of used context - same id that isn't -1 means
    //! contexts share display lists
    public GL2 getShareID() {
    	return Ctx.get(shareID);
//        if (mainWidget != null) {
//            return ((RealQGLWidget)mainWidget).getShareID();
//        } else {
//            return -1;
//        }    	
    }

    public void updateGL() {
    	mainWidget.redraw();//display();
    }
    
    //@{ A set of virtual functions with the same meaning as in QGLWidget, calls are
    //! redirected from the real QGLWidget
    protected void initializeGL(GL2 gl2) {
        gl2.glEnable(GL2.GL_DEPTH_TEST);  
    }    
    
    protected void paintGL(GL2 gl2) {}
    protected void resizeGL (GL2 gl2, int width, int height) {}
    
    // to be implemented by subclasses
    protected void visibilityChanged (boolean flag) {};
    
    //! These methods are called to create border widgets around the GL area.
    //! Override this to create decorations in your derived class.
    protected Composite createUpperBorder (Composite parent) { return null; }
    protected Composite createLowerBorder (Composite parent) { return null; }
    protected Composite createLeftBorder  (Composite parent) { return null; }
    protected Composite createRightBorder (Composite parent) { return null; }    
    
    //! Added for compatibility with soXtLib.
    protected boolean       isDoubleBuffer()           { return doubleBuffer(); }

    //! return actual size of the GL widget drawing area
    public SbVec2s getGlxSize() {
        if (mainWidget != null) {
            // QGLWidget::size calls QWidget::size which returns the size in window coordinates
            // we need to scale this size by the device pixel ratio.
            Point size = mainWidget.getSize()/*size() * mainWidget.devicePixelRatio()*/;
            return new SbVec2s ((short)size.x, (short)size.y);
        } else {
            return new SbVec2s (/*minGLWidth*/(short)1, /*minGLHeight*/(short)1);
        }    	
    }
  

  //! return ratio between physical pixels and device-independent pixels of GL widget drawing area
    protected double getGlxDevicePixelRatio()
  {
      return (mainWidget != null) ? /*mainWidget.devicePixelRatio()*/1.0 : 1.0;
  }

    
    protected
        //! handles events from the real QGLWidget, mostly mouse events
    void        processEvent (TypedEvent anyEvent, EventType type, boolean[] isAccepted) {
        // default does nothing    	
    }
    
    private
        //! replace main GL widget with a new one, the previous one is destroyed
        void replaceWidget (GLCanvas newWidget) {
        mainWidget = newWidget;    	
        
        firstVisibility = true;
        
        mainWidget.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseEnter(MouseEvent e) {
		        if (autoFocus) {
		        	mainWidget.setFocus();
		        }
				event(e, EventType.MOUSE_EVENT_MOUSE_ENTER);
			}

			@Override
			public void mouseExit(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_MOUSE_EXIT);
			}

			@Override
			public void mouseHover(MouseEvent e) {
				// Mouse hover is not handled by viewer
				//event(e, EventType.MOUSE_EVENT_MOUSE_HOVER);
			}
        	
        });
        
        //mainWidget.setAutoSwapBufferMode(_autoBufferSwapOn);
        
        if(_glCursorIsSet) {
        	mainWidget.setCursor(_glCursor);
        }
        mainWidget.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				mainWidget.setCurrent();
				if(!initialized) {
					initialized = true;
					initializeGL(new GL2() {});					
				}
				
				if(firstVisibility) {
					firstVisibility = false;
					visibilityChanged(true);
				}
			      GL2 gl2 = new GL2() {};  // get the OpenGL 2 graphics context
			      paintGL(gl2);
			      if(_autoBufferSwapOn) {
			    	  swapBuffers();
			      }
			}
        	
        });
        
        mainWidget.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle bounds = mainWidget.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				mainWidget.setCurrent();
				int width = bounds.width;
				int height = bounds.height;
			    resizeGL(new GL2() {}, width, height);

				mainWidget.setCurrent();
				if(!initialized) {
					initialized = true;
					initializeGL(new GL2() {});					
				}
				
				if(firstVisibility) {
					firstVisibility = false;
					visibilityChanged(true);
				}
			      GL2 gl2 = new GL2() {};  // get the OpenGL 2 graphics context
			      paintGL(gl2);
			      if(_autoBufferSwapOn) {
			    	  swapBuffers();
			      }
			}
		});
        
        addListener (SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(Event event) {
				SoQtGLWidget.this.destructor();
			}
        	
        });

		mainWidget.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_DOUBLE_CLICK);
				//mainWidget.redraw();//display();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_MOUSE_DOWN);
				//mainWidget.redraw();//display();
			}

			@Override
			public void mouseUp(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_MOUSE_UP);
				//mainWidget.redraw();//display();
			}
			
		});
		
		mainWidget.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_MOUSE_MOVE);				
				//mainWidget.display();
			}
			
		});
		
		mainWidget.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent e) {
				event(e, EventType.MOUSE_EVENT_MOUSE_SCROLLED);				
				//mainWidget.redraw();//display();
			}
			
		});
		
		mainWidget.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				event(e, EventType.KEY_EVENT_KEY_PRESSED);				
				//mainWidget.redraw();//display();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				event(e, EventType.KEY_EVENT_KEY_RELEASED);				
				//mainWidget.redraw();//display();
			}
			
		});	
		
		mainWidget.addListener(SWT.Hide, new Listener() {
			public void handleEvent(Event e) {
				visibilityChanged(false);
			}
			});
		
		mainWidget.addListener(SWT.Show, new Listener() {
			public void handleEvent(Event e) {
				visibilityChanged(true);
			}
			});
    }
    
    public void destructor() {
        contextShareManager.removeWidget (mainWidget);
	}

	public static enum EventType {
    	MOUSE_EVENT_DOUBLE_CLICK,
    	MOUSE_EVENT_MOUSE_DOWN,
    	MOUSE_EVENT_MOUSE_UP,
    	MOUSE_EVENT_MOUSE_ENTER,
    	MOUSE_EVENT_MOUSE_EXIT,
    	MOUSE_EVENT_MOUSE_HOVER,
    	MOUSE_EVENT_MOUSE_MOVE,
    	MOUSE_EVENT_MOUSE_SCROLLED,
    	LOCATION_REFRESH_MOUSE_EVENT,
    	KEY_EVENT_KEY_PRESSED,
    	KEY_EVENT_KEY_RELEASED,
    }
    
    private void event ( TypedEvent  e, EventType type )
    {
    	final boolean[] isAccepted = new boolean[1];
//        if (e.type() == QEvent::Resize) {
//            // Don't interfere with resize event,
//            // we have a separate resizeGL callback for this!
//            return QGLWidget::event(e);
//        }
//        if (e.type() == QEvent::Show) {
//            _w.visibilityChanged(true);
//        } else if (e.type() == QEvent::Hide) {
//            _w.visibilityChanged(false);
//        }

        // ignore mouse events when disabled
//        if (!isEnabled()) {
//          switch(e.type()) {
//            case QEvent::TabletPress:
//            case QEvent::TabletRelease:
//            case QEvent::TabletMove:
//            case QEvent::MouseButtonPress:
//            case QEvent::MouseButtonRelease:
//            case QEvent::MouseButtonDblClick:
//            case QEvent::MouseMove:
//            case QEvent::TouchBegin:
//            case QEvent::TouchUpdate:
//            case QEvent::TouchEnd:
//            case QEvent::ContextMenu:
//            case QEvent::Wheel:
//              return false;
//            default:
//              break;
//          }
//        }

        // first give event to external callback:
        if (eventCB != null && eventCB.run(eventCBData, e)) {
            // consume event if it was handled
            //e.setAccepted (true);
        } else {
            //e.ignore();
            processEvent(e, type, isAccepted);

            // Added focus setting because of bug 2833.
            if (e instanceof MouseEvent) {
            	MouseEvent me = (MouseEvent)e;
            	if(me.button == 1 && me.stateMask == 0) {            
            		setFocus();
            	}
            }
        }
        //return e.isAccepted() || QGLWidget::event(e);
    }
    
  //! Set the GL format. Same restrictions apply as above.
  public void setFormat ( GLData format, int style)
  {
      glFormat = format;
      if (mainWidget != null) {
          replaceWidget (
              contextShareManager.createWidget (format, style, this, shareGroup));
      }
  }


//! The SoContextShareManager does automatic GL context sharing between
//! "share groups" with the same id. Since sharing isn't guaranteed to
//! succeed, the effective share id (which isn't the same as the group
//! id) - obtained by getShareID - must be compared.
private static class SoContextShareManager
{
public
    //! default constructor
    SoContextShareManager() { nextShareID = 1; }

    //! Create new GL widget with given format and parent and try automatic
    //! context sharing with other widgets (created with this method)
    //! with the same shareGroup id. [Attention: Traverses linear list!]
    //! A shareGroup of -1 means no sharing.
	GLCanvas createWidget ( GLData theFormat, int style, SoQtGLWidget parent,
                                 int shareGroup) {
		GLData format = theFormat;
	//  Use these to enable core profile without deprecated functions (Experimental!)
//	    format.setProfile(QGLFormat::CoreProfile);
//	    format.setOption(QGL::NoDeprecatedFunctions);

	    int shareID = -1;
	    GLCanvas widget = null;
	    int count = entries.size();
	    int i=0;
//	    while (i<count && shareGroup != -1) {
//	    	GLCanvas entry = entries.get(i);
//	        if (entry.shareGroup == shareGroup) {
//	            if (widget != null) {
//	                // we already tried to create this widget, but obviously failed
//	                // so we must delete the old widget first:
//	                widget.destroy();
//	                widget = null;
//	                // This obviously could be slow, but hope is that the first
//	                // try will succeed in common cases.
//	            }
//	            widget = new GLCanvas (format, parent, entry);
//	            if (widget.isSharing()) {
//	                // sharing succeded
//	                shareID = entry.shareID;
//	                // leave loop
//	                break;
//	            }
//	        }
//	        i++;
//	    }
	    if (widget == null) {
	    	
	    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
	    	
	    	// we didn't find any widget for this share group, so lets
	        // just create a new one without sharing
	        widget = new GLCanvas (parent,style | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE,format);	        
	    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
	        widget.setCurrent();
	    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
	        GLCapabilities swtCapabilities = GL.createCapabilities();	        
	        parent.initialized = false; // have to reinitialize
	    }
	    if (shareID == -1) {
	        // sharing failed, give it a new ID:
	        shareID = nextShareID;
	        nextShareID++;
	    }
	    //widget.shareGroup = shareGroup;
	    //widget.shareID = shareID;
	    entries.add (widget);
	    return widget;
		
	}

    //! remove GL widget from the internal sharing list.
    public void removeWidget (GLCanvas widget) {
        while(entries.remove (widget));
    }

private
    //! internal list of widgets
    final List<GLCanvas> entries = new ArrayList<>();

    //! next share id to give to un-shared context
    private int nextShareID;
};


// set/get stereo buffering visual. This routine (like setDoubleBuffer)
// can be called interactively, althought slower since a new window
// with the needed visual is always created on the fly.
protected void setStereoBuffer (boolean flag)
{
    if (flag != isStereoBuffer()) {
    	GLData newFormat = format();
        newFormat.stereo = flag;
        setFormat (newFormat, getStyle());
    }
}


public void setColorBitDepth (int depth)
{
    if (depth != getColorBitDepth()) {
    	GLData newFormat = format();
        newFormat.redSize = depth;
        newFormat.greenSize = depth;
        newFormat.blueSize = depth;
        newFormat.alphaSize = depth;
        newFormat.depthSize = depth;
        setFormat (newFormat, getStyle());
    }
}


public int getColorBitDepth()
{
	GLData currFormat = format();
    int depth = currFormat.redSize;
    if (depth > currFormat.greenSize) { depth = currFormat.greenSize; }
    if (depth > currFormat.blueSize)  { depth = currFormat.blueSize;  }
    return depth;
}


	protected boolean      isStereoBuffer()  { 
		return /*context().format().stereo()*/false; //TODO 
	}
	
	/**
	 * Java port
	 * @return
	 */
	public Composite getParentWidget() {
		return getParent();
	}
	
	/**
	 * Java port
	 */
	  public boolean isFullScreen() {
		  return false; //TODO
	  }

	  /**
	   * Java port
	   * @param onoff
	   * @return
	   */
	  public boolean setFullScreen(boolean onoff) {
		  return false; // TODO
	  }

	  /**
	   * Java port
	   * @param title
	   */
	  public void setTitle(String title) {
		  // TODO
	  }
	  
	  /**
	   * Java port
	   * @param size
	   */
	  public void setSize( SbVec2s size) {
		  // TODO
	  }
	  
	  /**
	   * Java port
	   */
	  public void show() {
		  // TODO
	  }
}
