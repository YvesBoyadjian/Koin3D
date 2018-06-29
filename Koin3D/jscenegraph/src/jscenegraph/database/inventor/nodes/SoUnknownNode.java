/**
 * 
 */
package jscenegraph.database.inventor.nodes;

/**
 * @author Yves Boyadjian
 *
 */
public class SoUnknownNode extends SoGroup {

    //! Real class name
    private String className;


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the class name of the unknown node
//
// Use: public

public void setClassName( String name )

//
////////////////////////////////////////////////////////////////////////
{
    className = name;
}

}
