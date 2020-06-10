package shapes;

import apgraphicslib.CameraMovable;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;

public class Egg extends Physics_3DTexturedEquationedPolygon implements CameraMovable {
	public Egg(Object_draw drawer, double x, double y, double z,double xSize, double ySize, double zSize, double ppSize) {
		super(drawer, x, y, z,xSize, ppSize);
		setSize(xSize, ySize, zSize);
	}

	@Override 
	protected double[] equation(double theta, double phi) {
		double x = getXSize() * Math.cos(theta) * Math.sin(phi);
		double y = getYSize() * Math.sin(theta) * Math.sin(phi);
		double z = getZSize() * Math.cos(phi);
			
		return new double[] {x,y,z};		
		
	}
}
