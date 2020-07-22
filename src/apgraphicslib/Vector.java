package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary A 1-dimensional vector in space with a magnitude but no direction. Children will be two, three, four+ dimensional}
 */
public class Vector {
	private static Vector tempVec = new Vector(0);
	
	protected double r = 0;
	
	public Vector() {}
	
	public Vector(double r) {
		this.r = r;
	}
	
	public void setR(double r) {
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
	
	public Vector statAddInto(Vector addVec, Vector outputVec) {
		outputVec.setR(getR() + addVec.getR());
		return outputVec;
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
	
	/**
	 * scales this Vector by the multiple without changing this vector
	 * @param outputVec make this vec into the multiple
	 */
	public Vector statMultiplyInto(double mult, Vector outputVec) {
		outputVec.setR(getR() * mult);
		return outputVec;
	}
	
	

	/**
	 * {@code WARNING this method uses the temp protocol. If the return isn't IMMEDIATELY used it may be overwritten causing terrible awful errors}
	 * @return a Vector representing the scaling of this Vector by the multiple without changing this vector
	 */
	public Vector tempStatMultiply(double multi) {
		return statMultiplyInto(multi, tempVec);
	}

	public Vector tempStatAdd(Vector vector) {
		return statAddInto(vector,tempVec);
	}

	




	
}
