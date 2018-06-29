/**
 * 
 */
package jscenegraph.database.inventor.misc;

import jscenegraph.database.inventor.SbPList;


/**
 * @author Yves Boyadjian
 *
 */
public class SoCallbackList {
	
	private class SoCallbackStruct {
		SoCallbackListCB func;
		Object userData;
	}
	
	private final SbPList list = new SbPList(); 

	/**
	 * Managing callback functions At callback time, 
	 * f will be called with userData as the first parameter, 
	 * and callback specific data as the second parameter. 
	 * e.g. (*f)(userData, callbackData); Adds a function to or 
	 * removes a function from the list of callback functions. 
	 * 
	 * @param f
	 * @param userData
	 */
	 //
	 // addCallback - adds the function f to the callback list, along with
	 // userData. At invocation, f will be passed userData, along with callback
	 // specific data.
	 //
		public void addCallback(SoCallbackListCB f, Object userData) {
		
			  if (f == null)
				    return;
				   
				    SoCallbackStruct cb = new SoCallbackStruct();
				    cb.func = f;
				    cb.userData = userData;
				   
				    list.append(cb);
				  	}
		
		// Adds a function to or removes a function from the list of callback functions. 
		 //
		   //  removeCallback - removes the function f associated with userData from the.
		   //  callback list.
		   //
		   public  void
		   removeCallback(SoCallbackListCB f, Object userData)
		   //
		   {
		       int len = list.getLength();
		       SoCallbackStruct cb;
		       boolean found = false;
		       
		       for (int i = 0; (i < len) && (! found); i++) {
		           cb = (SoCallbackStruct ) list.operator_square_bracket(i);
		           if ((cb.func == f) && (cb.userData == userData)) {
		               list.remove(i);
		               //delete cb;
		               //cb.close(); java port
		               found = true;
		           }
		       }
		       
//		   #ifdef DEBUG
//		       if (! found)
//		           SoDebugError::post("SoCallbackList::removeCallback",
//		                              "Passed function and userData not found in "
//		                              "callback list");
//		   #endif
		   }   
		   		
		   /**
		    * Invokes each callback function in the list, 
		    * passing each function the user data supplied when they were registered here,
		    * and callbackData, the callback-specific data supplied by the caller. 
		    * 
		    * @param callbackData
		    */
		   public void invokeCallbacks(Object callbackData) {
			     int len = list.getLength();
			          SoCallbackStruct cb;
			          
			          for (int i = 0; i < len; i++) {
			              cb = (SoCallbackStruct ) list.operator_square_bracket(i);
			              cb.func.invoke(callbackData); //(cb.userData, callbackData);
			          }			     			 
		   }
		   
		     //! Clears all callback functions from the list.
		   public     void    clearCallbacks()                        { list.truncate(0); }
		   		   
		   //////////////////////////////////////////////////////////////////////////////
		    //
		    //  Destructor
		    //
		   		   public void destructor() {
			   
			     int len = list.getLength();
			      
			          for (int i = 0; i < len; i++) {
			              SoCallbackStruct soCallbackStruct = (SoCallbackStruct ) list.operator_square_bracket(i);
			              soCallbackStruct.func = null;
			              soCallbackStruct.userData = null;
			          }
			     		   }
}
