/**
 * 
 */
package jscenegraph.database.inventor.details;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.nodes.SoText3;

/**
 * @author Yves
 *
 */
public class SoTextDetail extends SoDetail {
	
  private
    int             stringIndex, charIndex;
    SoText3.Part       part;
	

	  private
	    static final SoType       classTypeId = new SoType();            //!< Type identifier
	  
	    //! Returns type identifier for this class.
	   public static SoType       getClassTypeId() { return new SoType(classTypeId); }

	    //! Returns the type identifier for a specific instance.
	   public SoType      getTypeId() {
		   return classTypeId;
	   }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns an instance that is a copy of this instance. The caller
//    is responsible for deleting the copy when done.
//
// Use: public, virtual
//

public SoDetail 
copy() 
//
////////////////////////////////////////////////////////////////////////
{
    SoTextDetail newDetail = new SoTextDetail();

    newDetail.stringIndex = stringIndex;
    newDetail.charIndex   = charIndex;
    newDetail.part        = part;

    return newDetail;
}

    //! For Text3, this sets which part is picked:
    public void                setPart(SoText3.Part p)        { part = p; }


    //! These set the string and character indices:
    public void                setStringIndex(int i)               { stringIndex = i; }
    public void                setCharacterIndex(int i)    { charIndex = i; }


}
