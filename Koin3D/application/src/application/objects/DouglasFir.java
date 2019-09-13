/**
 * 
 */
package application.objects;

import java.util.Random;

/**
 * @author Yves Boyadjian
 *
 */
public class DouglasFir {
	
	private static Random random = new Random();

	final static float min_height_meters = 20;
	
	final static float max_height_meters = 60;
	
	final float trunk_diameter_angle_degree = 5.0f;

	public static float getHeight() {
		
		float alpha = random.nextFloat();
		
		return min_height_meters + (max_height_meters - min_height_meters) * alpha;
	} 
	
	
}
