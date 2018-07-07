/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class Soleil {

	/**
	 * https://github.com/osefrance/SolarConcentrator/blob/master/position%20du%20soleil%20matlab/delta.m
	 * % calcul de delta declinaison du soleil (en radian)
	 * @param n numero du jour
	 * @return
	 */
	public static float delta(float n) {
		float l=0.01707f*(n-80); //%0.0172 vitesse angulaire de rotation moyenne de la Terre (rad/jour)
		float eps = 23.45f*(float)Math.PI/180f; //% inclinaison equateur/ecliptique (en radian)
		float d = (float)Math.asin(Math.sin(eps)*Math.sin(l));
		return d;
	}
	
	/**
	 * https://github.com/osefrance/SolarConcentrator/blob/master/position%20du%20soleil%20matlab/soleil_xyz.m
	 * % Calcul de la trajectoire du soleil en coord cartesiennes
	 * @param n numero du jour de l'année
	 * @param t heure solaire (heures)
	 * @param Phi latitude du lieu (degres)
	 * @return
	 */
	public static SbVec3f soleil_xyz(float n, float t, float Phi) {
		float delt = delta(n);
		
		float omega = 15*(t-12); //% angle solaire
		omega = omega*(float)Math.PI/180; // %en radian
		float phi=Phi*(float)Math.PI/180; //% latitude terrestre du lieu

		float Zsol= (float)(Math.sin(delt)*Math.sin(phi)+Math.cos(delt)*Math.cos(phi)*Math.cos(omega)); //% =sin(h)
		float Xsol= (float)(-Math.sin(delt)*Math.cos(phi)+Math.cos(delt)*Math.sin(phi)*Math.cos(omega)); 
		float Ysol= (float)(Math.cos(delt)*Math.sin(omega));
		
		return new SbVec3f(Xsol, Ysol, Zsol);
	}
}
