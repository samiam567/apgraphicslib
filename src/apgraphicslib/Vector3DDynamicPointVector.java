package apgraphicslib;

/**
 * {@summary this special type of Vector will ALWAYS point from the initial point to the final point.}
 * {@code All calculations are done from the retrieving of data rather than setting and the vector never has to be updated. This type of Vector is extremely fast for IJK values but a conventional vector may be faster if angles are needed since the angles are not stored. (They are calculated every time they are requested)}
 * @author samaiam567
 *
 */
public class Vector3DDynamicPointVector extends Vector3D {
	private Coordinate3D pI, pF;
	
	public Vector3DDynamicPointVector(Coordinate3D pI, Coordinate3D pF) {
		super(1657934);
		this.pI = pI;
		this.pF = pF;
		rectangularCalculated = true;
	}
	
	/**
	 * @return a conventional Vector equivalent to this one
	 */
	public Vector3D getVector() {
		return new Vector3D(pI,pF);
	}
	
	public double getI() {
		return (pF.getX()-pI.getX());
	}
	
	public double getJ() {
		return (pF.getY()-pI.getY());
	}
	public double getK() {
		return (pF.getZ()-pI.getZ());
	}
	
	public double getR() {
		return (Math.sqrt(getI()*getI() + getJ()*getJ() + getK()*getK()));
	}
	
	public double getTheta() {
		//set our variables to the current position of the pI and pF
		i = getI();
		j = getJ();
		k = getK();
		
		polarCalculated = false;
		rectangularToPolar(); //calculate theta,phi
		return theta;
	}
	
	public double getPhi() {
		//set our variables to the current position of the pI and pF
		i = getI();
		j = getJ();
		k = getK();
		polarCalculated = false;
		rectangularToPolar(); //calculate theta,phi
		return phi;
	}

	
	

	
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setSize(double xSize, double ySize, double zSize) {}
	
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setIJK(double i, double j, double k) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setI(double i) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setJ(double j) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setK(double k) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setTheta(double theta) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setPhi(double phi) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setPolar(double r, double theta, double phi) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setAngles(double theta, double phi) {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public void setR() {}
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public Vector3D add(Vector3D addVec) {return null;}
	
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public Vector3D add(Vector addVec) {return null;}
	
	
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public Vector3D subtract(Vector3D subVec) {return null;}
	
	/**
	 * @deprecated
	 * {@summary this doesn't do anything as DynamicPointVectors are only controlled by their points}
	 */
	public Vector3D subtract(Vector subVec) {return null;}
	
	

}
