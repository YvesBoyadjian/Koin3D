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
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This is the SoLineHighlightRenderAction class, used to highlight
 |   the selection by drawing the selected shapes in wireframe.
 |
 |   Classes    : SoLineHighlightRenderAction
 |
 |   Author(s)  : David Mott
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoLightModel;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.interaction.inventor.nodes.SoSelection;
import jscenegraph.nodekits.inventor.SoNodeKitPath;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
/// Line highlight - a subclass of SoGLRenderAction which renders the
/// scene graph, then renders wireframe boxes surrounding each selected object.
///
//////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
//! Selection highlight style.
/*!
\class SoLineHighlightRenderAction
\ingroup General
SoLineHighlightRenderAction is a render action which
renders the specified scene graph, then renders each selected object
again in wireframe.
Selected objects are specified by the first SoSelection
node in the scene to which this action is applied.
If there is no renderable geometry in a selected object,
no highlight is rendered for that object.
A highlight render action can be passed to
the setGLRenderAction() method of 
SoQtRenderArea
to have an affect on scene graphs.

\par See Also
\par
SoBoxHighlightRenderAction, SoGLRenderAction, SoSelection, SoXtRenderArea, SoDrawStyle, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

public class SoLineHighlightRenderAction extends SoGLRenderAction {

	public SoType getTypeId() {
		return new SoType(classTypeId);
	}
    public static SoType getClassTypeId()                              
                                    { return classTypeId; }                   
    public static void addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
    // java port
    public  static void                 enableElement(Class<?> klass)         
    { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
    
    public static void enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
    protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
    private static SoType               classTypeId	;

  protected
    //! Nodes which comprise the local highlight graph
    SoSeparator     localRoot;
  protected SoLightModel    lightModel;
  protected SoBaseColor     baseColor;
  protected SoDrawStyle     drawStyle;
  protected SoTexture2      texture;
    
  protected boolean            hlVisible;
    


//////////////////////////////////////////////////////////////////////////////
//
//  Constructor which takes no params
//
public SoLineHighlightRenderAction() {
        super(new SbViewportRegion(new SbVec2s((short)1, (short)1))); // pass a dummy viewport region
//
//////////////////////////////////////////////////////////////////////////////

    constructorCommon();
}

//////////////////////////////////////////////////////////////////////////////
//
//  Constructor which takes SoGLRenderAction params
//
public SoLineHighlightRenderAction(
    final SbViewportRegion viewportRegion) {    
        super(viewportRegion);
//
//////////////////////////////////////////////////////////////////////////////

    constructorCommon();
}

//////////////////////////////////////////////////////////////////////////////
//
//  Constructor common
//
private void constructorCommon()
//
//////////////////////////////////////////////////////////////////////////////
{
    //SO_ACTION_CONSTRUCTOR(SoLineHighlightRenderAction.class);
    traversalMethods = methods;


    // Set up our highlight graph
    localRoot   = new SoSeparator();
    lightModel  = new SoLightModel();
    baseColor   = new SoBaseColor();
    drawStyle   = new SoDrawStyle();
    texture     = new SoTexture2();
    SoMaterialBinding mb = new SoMaterialBinding();

    localRoot.ref();
    
    lightModel.model.setValue(SoLightModel.Model.BASE_COLOR);
    lightModel.setOverride(true);

    baseColor.rgb.setValue(1, 0, 0); // default color
    baseColor.setOverride(true);

    drawStyle.style.setValue(SoDrawStyle.Style.LINES);
    drawStyle.lineWidth.setValue(3);
    drawStyle.linePattern.setValue((short)0xffff);
    drawStyle.setOverride(true);
    
    // turn off texturing
    texture.setOverride(true);
    
    //set material binding to OVERALL
    mb.setOverride(true);
    
    // now set up the highlight graph
    localRoot.addChild(lightModel);
    localRoot.addChild(baseColor);
    localRoot.addChild(drawStyle);
    localRoot.addChild(texture);
    localRoot.addChild(mb);
    
    hlVisible = true;
}    

//////////////////////////////////////////////////////////////////////////////
//
//  Destructor
//
public void destructor()
//
//////////////////////////////////////////////////////////////////////////////
{
    localRoot.unref();
    super.destructor();
}    

static SoSearchAction sa = null;
//////////////////////////////////////////////////////////////////////////////
//
//  beginTraversal - have the base class render the passed scene graph,
//  then render highlights for our selection node.
//
public void apply(SoNode node)
//
//////////////////////////////////////////////////////////////////////////////
{
    // Render the scene
    super.apply(node);

    // Render the highlight?
    if (! hlVisible) return;

    // Add the rendering localRoot beneath our local scene graph localRoot
    // so that we can find a path from localRoot to the selection node 
    // which is under the render root.
    localRoot.addChild(node);
    
    // Find the selection node under the local root
    if (sa == null)
        sa = new SoSearchAction();
    else
        sa.reset();
    sa.setFind(SoSearchAction.LookFor.TYPE);
    sa.setInterest(SoSearchAction.Interest.FIRST);
    sa.setType(SoSelection.getClassTypeId());
    sa.apply(localRoot);
    
    SoPath hlPath = sa.getPath();
    if (hlPath != null) {
        hlPath = hlPath.copy();
        hlPath.ref();
        
        // Make sure something is selected
        SoSelection sel = (SoSelection ) hlPath.getTail();
        if (sel.getNumSelected() > 0) {
            // Keep the length from the root to the selection
            // as an optimization so we can reuse this data
            int reusablePathLength = hlPath.getLength();
    
            // For each selection path, create a new path rooted under our localRoot
            for (int j = 0; j < sel.getNumSelected(); j++) {
                // Continue the path down to the selected object.
                // No need to deal with p[0] since that is the sel node.
                SoFullPath p = SoFullPath.cast(sel.getPath(j));
                SoNode pathTail = p.getTail();

                if ( pathTail.isOfType(SoBaseKit.getClassTypeId())) {
                    // Find the last nodekit on the path.
                    SoNode kitTail = (SoNodeKitPath.cast(p)).getTail();

                    // Extend the selectionPath until it reaches this last kit.
                    SoFullPath fp = SoFullPath.cast(p);
                    int k = 0;
                    do {
                        hlPath.append(fp.getIndex(++k));
                    } 
                    while ( fp.getNode(k) != kitTail );
                }
                else {
                    for (int k = 1; k < p.getLength(); k++)
                        hlPath.append(p.getIndex(k));
                }
        
                // Render the shape with the local draw style to make the highlight
                super.apply(hlPath);
                        
                // Restore hlPath for reuse
                hlPath.truncate(reusablePathLength);
            }
        }
        
        hlPath.unref();
    }
    
    // Remove the rendering localRoot from our local scene graph
    localRoot.removeChild(node);
}    


// Methods which affect highlight appearance
public void setColor( final SbColor c )
{ baseColor.rgb.setValue(c); }

public SbColor getColor()
{ return baseColor.rgb.operator_square_bracket(0); }

public void setLinePattern( short pattern )
{ drawStyle.linePattern.setValue(pattern); }

public short getLinePattern()
{ return drawStyle.linePattern.getValue(); }

public void setLineWidth( float width )
{ drawStyle.lineWidth.setValue(width); }

public float getLineWidth()
{ return drawStyle.lineWidth.getValue(); }


//
// These are here to quiet the compiler.
//
public void apply(SoPath path)
{ super.apply(path); }

public void apply(final SoPathList pathList, boolean obeysRules)
{ super.apply(pathList, obeysRules); }
  
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes the SoGLRenderAction class.
//
public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    //SO_ACTION_INIT_CLASS(SoLineHighlightRenderAction.class, SoGLRenderAction.class);
    enabledElements = new SoEnabledElementsList(SoGLRenderAction.enabledElements);
    methods = new SoActionMethodList(SoGLRenderAction.methods);                   
    classTypeId    = SoType.createType(SoGLRenderAction.getClassTypeId(),        
                                        new SbName("SoLineHighlightRenderAction"), null);
}

}
