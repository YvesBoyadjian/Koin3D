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

	public IdentityOffset(String fieldName, SoField field) {
		super(fieldName);
		this.field = field;
	}

	public Object plus(SoFieldContainer container) {
		return field;
	}
	
}
