package shapes;

import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;

public class Sphere extends Egg {
	public Sphere(Object_draw drawer, double x, double y, double z, double size, double ppSize) {
		super(drawer, x, y, z,size, size, size, ppSize);
		setSize(size, size, size);
	}


	
	public boolean checkForCollision(Coordinate2D point, Tangible ob, double radius) {
		
		//getting the three-dimensional coordinates of point
		Coordinate3D point3D;
		try {
			//try to make the point a 3D point
			point3D = (Coordinate3D) point;
		}catch(ClassCastException c) { //if the point was 2D, just set it at zPos 0 and carry on
			point3D = new Coordinate3D(point.getX(),point.getY(),0);
		}
		
		
		
		//getting the Three-dimensional equivalent position of the object the point is in
		double obX = ob.getX();
		double obY = ob.getY();
		double obZ;
		try {
			obZ = ((Three_dimensional) ob).getZ();
		}catch(ClassCastException c) {
			obZ = 0;
		}
		
		double size = getXSize();
		
		//do a cylindrical boundary box covering the character
		
		return (Physics_engine_toolbox.distance3D(getX(), getY(),getZ(), obX, obY, obZ) <  radius);

	}

}
