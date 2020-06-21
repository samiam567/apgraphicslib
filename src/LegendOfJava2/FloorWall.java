package LegendOfJava2;

import apgraphicslib.Vector3D;

public class FloorWall extends Wall {
	public FloorWall(Room parentRoom, double x, double y, double z, double xSize, double ySize, Vector3D rotation, int ppSize) {
		super(parentRoom, x, y, z, xSize, ySize, rotation, ppSize);
	}

	@Override
	public double getPaintOrderValue() { 	
		return super.getPaintOrderValue()*4 + 100000000;
	}
}
