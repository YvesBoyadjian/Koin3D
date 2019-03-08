/**
 * 
 */
package jscenegraph.coin3d.inventor.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Yves Boyadjian
 *
 */
public class cc_storage {
	Class size;
	cc_storage_f constructor;
	cc_storage_f destructor;
	Map dict;
	
	static cc_storage cc_storage_construct_etc(Class size, cc_storage_f constructor, cc_storage_f destructor) {
		  return cc_storage_init(size, constructor, destructor);		
	}
	
	static cc_storage cc_storage_init(Class size, cc_storage_f constructor, cc_storage_f destructor) {
		cc_storage storage = new cc_storage();
		storage.size = size;
		storage.constructor = constructor;
		storage.destructor = destructor;
		storage.dict = new HashMap();
		
		return storage;
	}


public static Object
cc_storage_get(cc_storage storage)
{
  Object val;
  long threadid = 0;

//#ifdef HAVE_THREADS
//  threadid = cc_thread_id();
//
//  cc_mutex_lock(storage->mutex);
//#endif /* HAVE_THREADS */

  if ((val = storage.dict.get(threadid))== null) {
    val = malloc(storage.size);
    if (storage.constructor != null) {
      storage.constructor.invoke(val);
    }
    storage.dict.put(threadid, val);
  }

//#ifdef HAVE_THREADS
//  cc_mutex_unlock(storage->mutex);
//#endif /* HAVE_THREADS */

  return val;
}

static Object malloc(Class klass) {
	try {
		return klass.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}

}
