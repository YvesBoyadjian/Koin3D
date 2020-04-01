/**
 * 
 */
package jscenegraph.port;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yves Boyadjian
 *
 */
public class KDebug {
	
	public static boolean active = true;
	
    public static String getCallerClassName(String exclude) { 
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (
            		!ste.getClassName().equals(KDebug.class.getName()) 
            		&& ste.getClassName().indexOf("java.lang.Thread")!=0
            		&& ste.getClassName().indexOf(exclude) == -1) {
                return ste.getClassName();
            }
        }
        return null;
     }
    
    static Map<String,Long> counts = new HashMap<String,Long>();
    
    public static void count(String exclude) {
    	String caller = getCallerClassName(exclude);
    	
    	if(caller != null) {
    		
    		if(caller.indexOf("SoVRMLBillboard")!= -1) {
    			int i=0;
    		}
    		if( active) {
	    		Long count = counts.get(caller);
	    		count = count == null ? 1 : count + 1;
	    		counts.put(caller, count);
    		}
    	}
    }
    
    public static void dump() {
    	long max = 0;
    	String max_key = "";
    	for (String key : counts.keySet() ) {
    		long count = counts.get(key);
    		if( count > max) {
    			max = count;
    			max_key = key;
    		}
    	}
    	System.out.println("Most calling class : "+ max_key);
    }
}
