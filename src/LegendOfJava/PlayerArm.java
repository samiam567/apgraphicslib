package LegendOfJava;

import LegendOfJava.Character.Side;
import apgraphicslib.Settings;
import apgraphicslib.Vector3D;

public class PlayerArm extends PlayerBodyPart {
	
	public static final int armXSize = Settings.width/100;
	public static final int armYSize = Settings.width/40;
	public static final int armZSize = Settings.width/100;
	
	private double loops = 0;
	protected Side side;

	public static final Side swordArm = Side.right;
	
	public PlayerArm(Character parent, double x, double y, Character.Side side, int ppSize) {
		super(parent, x, y + 35, parent.getZ(), ppSize);
		this.side = side;

		textureSrc = "src/LegendOfJava/assets/strawberry.jpg";
		
		if (this.side == swordArm) {
			setSize(armXSize,armYSize/2,armZSize);
		}else {
			setSize(armXSize,armYSize,armZSize);
		}
		
		
		System.out.println("writing points");


		
	}
	
	
	public void load() {
		setTexture(textureSrc);
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		loops += frames;
		
		
		double velocity = (parentPlayer.getSpeed().getR()+1.010021);

		Vector3D armSwingAngV = new Vector3D(0,0,0);
		if (side == Side.left) {
			armSwingAngV.setIJK(velocity*Math.sin(loops*velocity - Math.PI/2),0,0);
		}else {
			armSwingAngV.setIJK(velocity*(-Math.sin(loops*velocity - Math.PI/2)),0,0);
		}
	//	Vector3D normalVec = new Vector3D(parentPlayer.normalPoint.getX(), parentPlayer.normalPoint.getY(),parentPlayer.normalPoint.getZ());
	//	setAngularVelocity(Vector3D.proj((Vector3D)armSwingAngV.multiply(1),((Vector3D) normalVec).statMultiply(0.001) ));
	
		if (loops > 1000000000) loops = 0;
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

