/**
 * 
 */
package jscenegraph.port;

import java.lang.reflect.Field;

import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class Offset {
	
	private Class<? extends SoFieldContainer> containerClass;
	private String fieldName;

	public Offset(Class<? extends SoFieldContainer> containerClass, String fieldName) {
		this.containerClass = containerClass;
		this.fieldName = fieldName;
	}

	public void copyFrom(Offset offset) {
		fieldName = offset.getFieldName();
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public static boolean containerContains(Class<? extends SoFieldContainer> class1, SoFieldContainer container, String fieldName ) {
		try {
			return null != plus(class1, container,fieldName);
		}
		catch(IllegalStateException e) {
			return false;
		}
	}

	public Object plus(SoFieldContainer container) {
		return plus( containerClass, container, fieldName);
	}
	
	public static Object plus(Class<? extends SoFieldContainer> class1, SoFieldContainer container, String fieldName) {
		try {
			Field field = getField(/*container.getClass()*/class1,fieldName);
			field.setAccessible(true);
			Object fieldObject = field.get(container);
			return fieldObject;
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static Field getField(Class clazz, String fieldName)
	        throws NoSuchFieldException {
	      return clazz.getDeclaredField(fieldName);
	  }
}
