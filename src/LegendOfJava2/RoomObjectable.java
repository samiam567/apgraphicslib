package LegendOfJava2;


import apgraphicslib.Coordinate2D;
import apgraphicslib.Physics_engine_compatible;
import apgraphicslib.Vector;

/**
 * {@summary can be in a LOJ room}
 */
public interface RoomObjectable extends Physics_engine_compatible {
	public void add(Room room);
	public void setSpeed(Vector speed);
	public void rotateAbout(Vector rotation, Coordinate2D pointOfRotation);
	
	public void setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit);
	public void setOrbitalAngularVelocity(Vector newAngV);
	public void Update(double frameStep);
}
