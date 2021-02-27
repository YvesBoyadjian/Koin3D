/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;

/**
 * @author Yves Boyadjian
 *
 */
public class IdentityOffset extends Offset {
	
	private SoField field;

	public IdentityOffset(Class<? extends SoFieldContainer> containerClass, String fieldName, SoField field) {
		super(containerClass, fieldName);
		this.field = field;
	}

	public Object plus(SoFieldContainer container) {
		return field;
	}
	
}
