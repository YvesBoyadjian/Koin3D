/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class SbBoxTest {

	public static void main(String[] args) {
		SbBox3f box = new SbBox3f();
		SbVec3f min = new SbVec3f(1557,3308,850);
		SbVec3f max = new SbVec3f(3113,30157,1886);
		box.extendBy(min);
		box.extendBy(max);
		
		SbVec3f point = new SbVec3f(1524,13794,851);
		
		SbVec3f closest = box.getClosestPoint(point);
		
		System.out.println("wrong_closest : "+ closest.getX()+" "+closest.getY()+" "+closest.getZ());

		SbVec3f rightClosest = box.getClosestExternalPoint(point);
		
	
		System.out.println("right_closest : "+ rightClosest.getX()+" "+rightClosest.getY()+" "+rightClosest.getZ());
	}
}
