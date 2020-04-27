package LegendOfJava;

import apgraphicslib.Settings;


public class PlayerTorso extends PlayerBodyPart {
	
	public static final double torsoXSize = Settings.width/40, torsoYSize = Settings.width/40, torsoZSize = Settings.width/40;

	public PlayerTorso(Character parentPlayer, double x, double y, int ppSize) {
		super(parentPlayer, x, y,parentPlayer.getZ(), ppSize);
		
		textureSrc = "./src/LegendOfJava/assets/shieldAndSword.jpg";
		
		setSize(torsoXSize,torsoYSize,torsoZSize);
		
		System.out.println("writing points");
		
		setTexture(textureSrc);
	}
	
	/**
	 * @param theta
	 * @param phi
	 * @return {x, y, z}
	 */
	protected double[] equation(double theta,double phi) {
		double x = getXSize() * Math.sin(theta);
		double z = getZSize() * Math.cos(theta);
		double y = -getYSize() + getYSize() * phi;
		
		return new double[] {x,y,z};		
	}


}
