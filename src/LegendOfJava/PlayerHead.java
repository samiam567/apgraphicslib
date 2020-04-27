package LegendOfJava;


import apgraphicslib.Settings;
import apgraphicslib.Vector3D;

public class PlayerHead extends PlayerBodyPart {
	public static final double headXSize = Settings.width/20, headYSize = Settings.width/20, headZSize = Settings.width/20;


	public PlayerHead(Character parentPlayer, int ppSize) {
		super(parentPlayer, parentPlayer.getX(), parentPlayer.getY(), parentPlayer.getZ(), ppSize);
		textureSrc = "./src/LegendOfJava/assets/pointyHead.jpg";
		
		
		setSize(headXSize,headYSize,headZSize);
		

	
		
	}
	
	public void load() {
		setTexture(textureSrc);
		rotatePoints(new Vector3D(0,Math.PI/10,0));
		rotatePoints(new Vector3D(0,0,Math.PI/2));
		rotatePoints(new Vector3D(0,-Math.PI,0));
	}
	

	/**
	 * @param theta
	 * @param phi
	 * @return {x, y, z}
	 */
	protected double[] equation(double theta,double phi) {
		double x = getXSize() * Math.cos(theta) * Math.sin(phi);
		double y = getYSize() * Math.sin(theta) * Math.sin(phi);
		double z = getZSize() * Math.cos(phi);
		
		return new double[] {x,y,z};		
	}
	
	
	

}
