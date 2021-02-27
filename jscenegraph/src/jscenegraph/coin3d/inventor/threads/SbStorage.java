
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.inventor.threads;

import java.util.function.Supplier;

import jscenegraph.coin3d.inventor.base.Dict;

/**
 * @author Yves Boyadjian
 *
 */

/*
  This ADT manages thread-local memory.  When different threads access
  the memory an cc_storage object manages, they will receive different
  memory blocks back.

  For additional API documentation, see doc of the SbStorage C++
  wrapper around the cc_storage_*() functions at the bottom of this
  file.
*/

public class SbStorage<T extends Object> {
	
	private Supplier<T> p;
	private cc_storage storage;
	
	public static interface SbStorageApplyFunc{
		void invoke(Object tls, Object closure);
	}

	public static interface cc_storage_apply_func {
		void invoke(Object dataptr, Object closure);
	}
	
	
	public SbStorage(Supplier<T> p) {
		this.p = p;
	}
	
	public SbStorage(Class size, cc_storage_f constr, cc_storage_f destr) {
		 this.storage = cc_storage.cc_storage_construct_etc(size, constr, destr); 
	}

	public T get() {
		if(storage != null) {			
			return (T)cc_storage.cc_storage_get(storage);
		}
		return p.get();
	}
	
	/* struct needed for cc_dict wrapper callback */
public static class cc_storage_hash_apply_data {
  cc_storage_apply_func func;
  Object closure;
} ; 

	
/* callback from cc_dict_apply. will simply call the function specified
   in cc_storage_apply_to_appl */
public static void 
storage_hash_apply(int key, Object val, Object closure)
{
  cc_storage_hash_apply_data data = 
    (cc_storage_hash_apply_data) closure;
  data.func.invoke(val, data.closure);
}

	

	public static void 
cc_storage_apply_to_all(cc_storage storage, 
                        cc_storage_apply_func func, 
                        Object closure)
{
  /* need to set up a struct to use cc_dict_apply */
  cc_storage_hash_apply_data mydata = new cc_storage_hash_apply_data();
  
  /* store func and closure in struct */
  mydata.func = func;
  mydata.closure = closure;

//#ifdef HAVE_THREADS
//  cc_mutex_lock(storage->mutex);
//  cc_dict_apply(storage->dict, storage_hash_apply, &mydata);
//  cc_mutex_unlock(storage->mutex);
//#else /* ! HAVE_THREADS */
  Dict.cc_dict_apply(
		  storage.dict,
		  new Dict.cc_dict_apply_func() {
			
			@Override
			public void invoke(Object key, Object val, Object closure) {
				SbStorage.storage_hash_apply(((Number)key).intValue(),val, closure);				
			}
		}
		  ,
		  mydata);
//#endif /* ! HAVE_THREADS */

}

	
  public void applyToAll(SbStorageApplyFunc func, Object closure) {
    SbStorage.cc_storage_apply_to_all(this.storage, 
    		new SbStorage.cc_storage_apply_func() {
		public void invoke(Object dataptr, Object closure2) {
			func.invoke(dataptr, closure2);
		}		
    }
                            /*(cc_storage_apply_func)(func)*/, closure);
  }

}
