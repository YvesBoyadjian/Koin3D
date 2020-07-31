/**
 * 
 */
package jscenegraph.coin3d.inventor.base;

import java.util.Map;

/**
 * @author BOYADJIAN
 *
 */
public class Dict {

	public static class cc_dict_entry {
    Object key;
    Object val;
     cc_dict_entry next; // ptr
  };
  
  public interface cc_dict_hash_func {
	  Object invoke(Object key);
  }
  
  public interface cc_dict_apply_func {
	  void invoke(Object key, Object val, Object closure);
  }
  
  
  /* allocator struct */
//public interface cc_memalloc {
//
//  cc_memalloc_free free; //ptr
//  cc_memalloc_memnode memnode; //ptr
//
//  int chunksize;
//
//  int num_allocated_units;
//  cc_memalloc_strategy_cb strategy; //ptr
//};

  
	
//  public class cc_dict {
//    int size;
//    int elements;
//    float loadfactor;
//    int threshold;
//    cc_dict_entry[] buckets;
//    cc_dict_hash_func hashfunc; // ptr
//    cc_memalloc memalloc; // ptr
//  };

	/*!
  Call \a func for for each element in the hash table.
*/
public static void
cc_dict_apply(/*cc_dict*/Map ht, cc_dict_apply_func func, Object closure)
{
  int i;
  Dict.cc_dict_entry elem;
  for (Object obj : ht.entrySet()) {
    elem = (Dict.cc_dict_entry)obj;//ht.buckets[i];
    while (elem != null) {
      func.invoke(elem.key, elem.val, closure);
      elem = elem.next;
    }
  }
}

}
