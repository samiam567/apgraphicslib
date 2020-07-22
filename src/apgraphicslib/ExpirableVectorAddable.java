package apgraphicslib;

/**
 * {@summary used to apply a one-time angularVelocity or angularAcceleration to an object}
 * @see ExpirableVectorAdd
 * @author apun1
 */
public interface ExpirableVectorAddable {
	
	/**
	 * {@summary returns the addition of this to the passed Vector}
	 * @param vecToAddTo
	 * @return
	 */
	public Vector tempStatAdd(Vector vecToAddTo);
}
