/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbName;

/**
 * @author Yves Boyadjian
 *
 */
public class SbNameArray extends Indexable<SbName> {
	
	SbName[] values;
	int start;

	public SbNameArray(int length) {
		values = new SbName[length];
		for( int i=0;i<length;i++) {
			values[i] = new SbName();
		}
	}

	public SbNameArray(int start2, SbNameArray sbNameArray) {
		this.start = start2 + sbNameArray.start;
		this.values = sbNameArray.values;
	}

	@Override
	public SbName getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, SbName object) {
		values[i+start].copyFrom(object);
	}

	public SbNameArray plus(int start2) {
		return new SbNameArray(start2,this);
	}

	@Override
	public int delta() {
		return start;
	}

	@Override
	public Object values() {
		return values;
	}

}
