package LegendOfJava;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;
import apgraphicslib.Vector3D;

public class Pot extends Physics_3DTexturedEquationedPolygon implements Hittable, RoomObjectable {

	public Pot(Object_draw drawer, double x, double y, double z, double xSize, double ySize, double zSize, double ppSize) {
		super(drawer, x, y, z, Math.sqrt(xSize*xSize + ySize*ySize + zSize*zSize), ppSize);
		setSize(xSize,ySize,zSize);
		setTexture("src/LegendOfJava/assets/potTexture.jpg");
		rotatePoints(new Vector3D(Math.PI,0,0));
	}

	@Override
	public void add(Room room) {
		getDrawer().add(this);
		
	}

	@Override
	public void hit(double attackPower) {
		AudioManager.playPotBreakAudio();
		getDrawer().remove(this);
		System.out.println("pot hit!");
		LegendOfJavaRunner.Ryan.HP++;
		AudioManager.playHeartRestoreAudio();
	}
	
	/**
	 * @param theta
	 * @param phi
	 * @return {x, y, z}
	 */
	protected double[] equation(double theta,double phi) {
		double x,y,z,r;
	
	//	x = getXSize() * (Math.cos(theta))* Math.pow(phi,1/2) * Math.sin(phi * 1.2 + 1.5) ;
	//	z = getZSize() * (Math.sin(theta)) * Math.pow(phi,1/2) * Math.sin(phi * 1.2 + 1.5) ;
		
		x = getXSize() * Math.cos(theta) - getXSize()/2;
		y = getYSize() * phi - getYSize();
		z = getZSize() * Math.sin(theta) - getZSize()/2;
		
	
		
		if (y > getYSize()/2) {			
			y = getYSize()/2;
		    x = getXSize() * Math.cos(theta) * Math.sin(phi) - getXSize()/2;
			z = getZSize() * Math.sin(theta) * Math.sin(phi) - getZSize()/2;
		}
		
		return new double[] {x,y,z};		
	}
	

}
