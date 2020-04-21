package apgraphicslib;

/**
 * @author samiam567
 * {@summary a rotatable object. Rotation will be set with vectors}
 */
public interface Rotatable extends Updatable {
	public void setRotation(Vector rotVec);
	public void setAngularVelocity(Vector angVelVec);
	public void setAngularAcceleration(Vector angAccelVec);
	
	void setOrbitalRotation(Vector rotVec);
	public void setOrbitalAngularVelocity(Vector angVelVec);
	public void setOrbitalAngularAcceleration(Vector angAccelVec);
	
	public void rotate(Vector rotVec);
	public void rotateAbout(Vector rotVec, Coordinate2D pointOfRotation);
	
	public Vector getRotation();
	public Vector getAngularVelocity();
	public Vector getAngularAcceleration();
	
	public Vector getOrbitalRotation();
	public Vector getOrbitalAngularVelocity();
	public Vector getOrbitalAngularAcceleration();
	
	public void setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit);
	
}
