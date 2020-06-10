package shapes;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DPolygon;

public class Rectangle3D extends Physics_3DPolygon {
	public Rectangle3D(Object_draw drawer, double x, double y, double z, double xSize, double ySize) {
		super(drawer, x, y, z);
		addPoint(-xSize/2, -ySize/2);
		addPoint(xSize/2, -ySize/2);
		addPoint(xSize/2, ySize/2);
		addPoint(-xSize/2, ySize/2);
	}
	
}
