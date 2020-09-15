package apgraphicslib;


/**
 * {@summary an object that can be collided into}
 * @author apun1
 *
 */
public interface Tangible extends Updatable, Movable, Two_dimensional {
	
	
	/*
	 * the implementation of these methods is critical and also likely different for every application.
	 * Write the methods so that they are as fast as possible for your application.
	 * checkForCollision(object) is likely going to be very similar to checkForCollision(point) if not almost identical.
	 * There is a well-written example of an implementation of these methods in Physics_3DTexturedPolygon
	 */
	
	/**
	 * {@summary checks for a collision between this and another tangible}
	 * @param object the object to check if this one is collided with
	 * @return the point of contact or null if the objects are not collided
	 */
	public Coordinate2D checkForCollision(Tangible current_object, Vector3D directionVec);
	
	/**
	 * @param point the coordinate to check if it is in the object
	 * @param ob the object that the point is in
	 * @param radius the radius around the point to check in
	 * @return whether the passed coordinate is within the passed radius of the edges of this object
	 */
	public boolean checkForCollision(Coordinate2D point,Tangible ob, Vector3D directionVec, double radius);
	
	/**
	 * {@summary called when a collision is detected
	 * @param object
	 */
	public void collision(CollisionEvent object);

	/**
	 * creates a collision event
	 * @param o2 the object running into this one
	 * @param pointOfCollision the point of contact
	 * @return a CollisionEvent representing the half of the collision where o2 runs into this object
	 */
	public CollisionEvent getCollisionEvent(Tangible o2, Coordinate2D pointOfCollision);

	public boolean getIsTangible();
}
