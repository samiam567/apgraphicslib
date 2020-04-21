package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary
 * Object supports complex collisions.}
 *
 */
public interface MultiTangible extends Tangible {
	
	/**
	 * @param the object that is colliding with the MultiTangible
	 * @param velocity of the object
	 * @return a force vector that is the force acted on the object
	 * {@summary this will determine the force applied to an object colliding with the given velocity vector to this multiTangible}
	 */
	public Vector collision(MultiTangible ob, Vector velocity);
}
