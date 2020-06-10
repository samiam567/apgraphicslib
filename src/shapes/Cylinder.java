package shapes;

import apgraphicslib.CameraMovable;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;

public class Cylinder extends Physics_3DTexturedEquationedPolygon implements CameraMovable {
	public Cylinder(Object_draw drawer, double x, double y, double z,double xSize, double ySize, double zSize, double ppSize) {
		super(drawer, x, y, z,xSize, ppSize);
		setSize(xSize, ySize, zSize);
	}

	@Override 
	protected double[] equation(double theta, double phi) {
		double x = getXSize() * Math.sin(theta);
		double y = getYSize() * Math.cos(theta);
		double z = -getZSize() + getZSize() * phi;
			
		return new double[] {x,y,z};
	}
	
}
