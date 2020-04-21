package apgraphicslib;

/**
 * {@summary an event detailing one half of a collision between two Tangibles. See the comments in the CollisionEvent class for more details}
 * Note that all fields should be in the highest dimension of the object creating this event
 * @author samaim567
 */
public class CollisionEvent {
	public Coordinate pointOfCollision; // the point of contact
	public Vector collisionSurface; // a normal Vector to the plane on objectHit that the object collided into
	public Tangible objectHit; // the object that was collided into (send this event to the other object)
	public Vector relativeSpeed; // the speed of the other Tangible relative to objectHit
	
	/**
	 * @param pOfC pointOfCollision : the point of contact
	 * @param cSurf collisionSurface : a normal Vector to the plane on objectHit that the object collided into
	 * @param obHit objectHit : the object that was collided into (send this event to the other object)
	 * @param rSpeed relativeSpeed : the speed of the other Tangible relative to objectHit
	 */
	public CollisionEvent(Coordinate pOfC, Vector cSurf,Tangible obHit, Vector rSpeed) {
		pointOfCollision = pOfC;
		collisionSurface = cSurf;
		objectHit = obHit;
		relativeSpeed = rSpeed;
	}
	
}
