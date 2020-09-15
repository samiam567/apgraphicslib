package shapes;


import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_2DPolygon;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Vector3D;

/**
 * {@code A rectangle. Supports rotation and collision (but you must implement Tangible}
 * @author apun1
 *
 */
public class Rectangle extends Physics_2DPolygon {
	
	private boolean isTangible = true;
	
	public Rectangle(Object_draw drawer, double x, double y, double xSize, double ySize) {
		super(drawer, x, y);
		
		addPoint(-xSize/2, -ySize/2);
		addPoint(xSize/2, -ySize/2);
		addPoint(xSize/2, ySize/2);
		addPoint(-xSize/2, ySize/2);
		
	}
	
	
	public Coordinate2D checkForCollision(Tangible cT, Vector3D directionVec) {
		for (PolyPoint cP : getPoints() ) {
			if (cT.checkForCollision((Coordinate2D) cP, (Tangible) this, directionVec, 1)) {
				return (Coordinate2D) cP;
			}
		}	
		return null;
	}


	public boolean checkForCollision(Coordinate2D point, Tangible ob, Vector3D directionVec, double radius) {
		if (Math.abs(rotation.getR()) % Math.PI/2 < Math.PI/8) { // if our rotation allows for AABB collision (Axis-alligned boundry boxes)
			return ( (Math.abs(getX() - ob.getX()) < (ob.getXSize()/2+getXSize()/2)) && (Math.abs(getY() - ob.getY()) < (ob.getYSize()/2+getYSize()/2))  );
		}else {
			Coordinate3D coords3D = new Coordinate3D(getX(), getY(), 0);
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
			
			return (Physics_engine_toolbox.objectRelativePointDistance3D(obX, obY, obZ, point3D, getX(), getY(), 0, coords3D) <= radius + Math.sqrt(getXSize()*getXSize()+getYSize()*getYSize()));
		}
	}

	public CollisionEvent getCollisionEvent(Tangible o2, Coordinate2D pointOfCollision) {
		return new CollisionEvent(pointOfCollision,rotation,(Tangible) this,getSpeed());
	}


	public boolean getIsTangible() {
		return isTangible;
	}


	public void setIsTangible(boolean isTangible) {
		this.isTangible = isTangible;
	}

}
