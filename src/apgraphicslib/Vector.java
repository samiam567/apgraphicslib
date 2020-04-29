package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary A 1-dimensional vector in space with a magnitude but no direction. Children will be two, three, four+ dimensional}
 */
public class Vector {
	protected double r = 0;
	
	public Vector() {}
	
	public Vector(double r) {
		this.r = r;
	}
	
	public double getR() {
		return r;
	}
	
	/**
	 * @param Vector addVec
	 * {@summary adds the passed vector to this Vector}
	 * @return 
	 */
	public Vector add(Vector addVec) {
		r += addVec.getR();
		return this;
	}
	
	/**
	 * @param addVec
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector representing the addition of this Vector and the passed Vector
	 */
	public Vector statAdd(Vector addVec) {
		return new Vector(getR() + addVec.getR());
	}
	
	/**
	 * 
	 * @param vec1
	 * @param vec2
	 * @return a vector representing the addition of vec1 and vec2
	 */
	public static Vector add(Vector vec1, Vector vec2) {
		return new Vector(vec1.getR() + vec2.getR());
	}
	
	/**
	 * 
	 * @param vec1
	 * @param vec2
	 * @return a vector representing the subtraction of vec1 and vec2
	 */
	public static Vector subtract(Vector vec1, Vector vec2) {
		return new Vector(vec1.getR() - vec2.getR());
	}

	/**
	 * @param double multi
	 * {@summary scales this vector by the passed value}
	 * @return 
	 */
	public Vector multiply(double multi) {
		r *= multi;
		return this;
	}
	
	/**
	 * @param double multi
	 * {@summary returns a Vector representing the result of scaling this Vector by the multi}
	 */
	public Vector statMultiply(double multi) {
		return new Vector(getR() * multi);
	}
	
	@Override
	public String toString() {
		return "" + getR();
	}



	
}
