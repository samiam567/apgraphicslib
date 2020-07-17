package shapes;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedPolygon;

public class Rectangle3D_Textured extends Physics_3DTexturedPolygon {
	
	public Rectangle3D_Textured(Object_draw drawer, double x, double y, double z, double xSize, double ySize, int ppSize) {
		super(drawer, x, y, z, ppSize);
		
		addPoint(-xSize/2, -ySize/2,0);
		addPoint(xSize/2, -ySize/2,0);
		addPoint(xSize/2, ySize/2,0);
		addPoint(-xSize/2, ySize/2,0);
		
	}
}
