package LegendOfJava;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;

public class Pot extends Physics_3DTexturedEquationedPolygon implements Hittable, RoomObjectable {

	public Pot(Object_draw drawer, double x, double y, double z, double size, double ppSize) {
		super(drawer, x, y, z, size, ppSize);
		setSize(size,size,size);
	}

	@Override
	public void add(Room room) {
		getDrawer().add(this);
		
	}

	@Override
	public void hit(double attackPower) {
		getDrawer().remove(this);
		System.out.println("pot hit!");
		LegendOfJavaRunner.Ryan.HP++;
	}
	
	/**
	 * @param theta
	 * @param phi
	 * @return {x, y, z}
	 */
	protected double[] equation(double theta,double phi) {
		double x,y,z;
		
		//x = getXSize() * Math.cos(theta) * Math.pow(phi,1/2) * Math.sin(phi * 1.2 + 1.5) + getXSize()/4 * Math.abs(Math.cos(theta))/Math.cos(theta);
		//z = getZSize() * Math.sin(theta) * Math.pow(phi,1/2) * Math.sin(phi * 1.2 + 1.5) + getZSize()/4 * Math.abs(Math.sin(theta))/Math.sin(theta);
		
		x = getXSize() * Math.cos(theta);
		z = getYSize() * Math.sin(theta);
		
		if (phi < 1) {
			
			y = 0.5 * getYSize();
		    x = getXSize() * Math.cos(theta) * Math.sin(phi);
			z = getZSize() * Math.sin(theta) * Math.sin(phi);
		}else {
			y = getYSize() * phi - 50;
		}
		
		return new double[] {x,y,z};		
	}
	

}
