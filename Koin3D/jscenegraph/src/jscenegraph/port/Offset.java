/**
 * 
 */
package jscenegraph.port;

import java.lang.reflect.Field;

import jscenegraph.database.inventor.fields.SoFieldContainer;

/**
 * @author Yves Boyadjian
 *
 */
public class Offset {
	
	private String fieldName;

	public Offset(String fieldName) {
		this.fieldName = fieldName;
	}

	public void copyFrom(Offset offset) {
		fieldName = offset.getFieldName();
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public static boolean containerContains(SoFieldContainer container, String fieldName ) {
		try {
			return null != plus(container,fieldName);
		}
		catch(IllegalStateException e) {
			return false;
		}
	}

	public Object plus(SoFieldContainer container) {
		return plus( container, fieldName);
	}
	
	public static Object plus(SoFieldContainer container, String fieldName) {
		try {
			Field field = getField(container.getClass(),fieldName);
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
	    try {
	      return clazz.getDeclaredField(fieldName);
	    } catch (NoSuchFieldException e) {
	      Class superClass = clazz.getSuperclass();
	      if (superClass == null) {
	        throw e;
	      } else {
	        return getField(superClass, fieldName);
	      }
	    }
	  }
}
