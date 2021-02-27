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
	
	final static float min_height_meters = 5;
	
	final static float max_height_meters = 57;//60;
	
	public final static float trunk_diameter_angle_degree = 1.5f;

	public static float getHeight(Random random) {
		
		float alpha = random.nextFloat();
		
		//alpha = (float)Math.pow(alpha,1.1f);
		
		return min_height_meters + (max_height_meters - min_height_meters) * alpha;
	} 
	
	
}
