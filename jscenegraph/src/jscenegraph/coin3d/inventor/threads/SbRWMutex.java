/**
 * 
 */
package jscenegraph.coin3d.inventor.threads;

import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbRWMutex implements Destroyable {

	public
		  enum Precedence {
		    READ_PRECEDENCE,
		    WRITE_PRECEDENCE
		  };

		  public SbRWMutex() {
			  this(Precedence.READ_PRECEDENCE);
		  }
	/**
	 * 
	 */
	public SbRWMutex(Precedence policy) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destructor() {
		// TODO Auto-generated method stub
		
	}

	public void readLock() {
		// TODO Auto-generated method stub
		
	}

	public void readUnlock() {
		// TODO Auto-generated method stub
		
	}

	public void writeLock() {
		// TODO Auto-generated method stub
		
	}

	public void writeUnlock() {
		// TODO Auto-generated method stub
		
	}

}
