package apgraphicslib;

/**
 * @author samaim567
 *{@summary A Vector in 3D space. Extends Vector2D}
 *{@code NOTE: we use the spherical coordinate system for Vector3D which we call "polar". This is r, theta, and phi.        Vector3D has i,j,k,r,theta,phi}
 */
public class Vector3D extends Vector2D implements Three_dimensional {
	protected double k = 0, phi = 0;
	private static Vector3D tempVec = new Vector3D(0,0,0);
	
	/**
	 * {@summary this constructor should ONLY be used by the Dynamic point vector}
	 * @param key ensures only classes that know the key can use this constructor
	 */
	public Vector3D(int key) {
		super(key);
		if (key != 1657934) {
			Exception e = new Exception("ONLY Dynamic Vectors can use this constructor");
			e.printStackTrace();
		}
	}
	
	public Vector3D() {
		super(1657934);
		setIJK(0,0,0);
	}
	
	public Vector3D(double xComponent, double yComponent, double zComponent) {
		super(1657934);
		setIJK(xComponent,yComponent,zComponent);
	}
	
	public Vector3D(Coordinate3D pI, Coordinate3D pF) {
		super(1657934);
		setIJK(pF.getX() - pI.getX(),pF.getY() - pI.getY(),pF.getZ() - pI.getZ());
	}
	
	public void setIJK(double i, double j, double k) {
		//since we are setting all three we don't need to calculate rectangular
		this.i = i;
		this.j = j;
		this.k = k;
		rectangularCalculated = true;
		polarCalculated = false;
	}

	public void setK(double newK) {
		this.k = newK;
		rectangularToPolar();
	}
	
	/**
	 * 
	 * @param r
	 * @param theta
	 * @param phi
	 * {@summary sets the polar values of the Vector}
	 */
	public void setPolar(double r, double theta, double phi) {
		//since we are setting everything we don't need to calculate polar
		this.theta = theta;
		this.phi = phi;
		this.r = r;		
		polarCalculated = true;
		rectangularCalculated = false;
	}
	
	/**
	 * {@summary sets the polar angles of the vector. This will not change the magnitude of the vector.}
	 * @param theta
	 * @param phi
	 */
	public void setAngles(double theta, double phi) {
		rectangularToPolar();
		this.theta = theta;
		this.phi = phi;
		rectangularCalculated = false;
	}
	
	/**
	 * {@summary calculates the polar values based on the rectangular values}
	 */
	@Override
	protected void rectangularToPolar() {
		
		if (! polarCalculated) {
		/*	//error detection removed for performance
			if (! rectangularCalculated) {
				Exception e = new Exception("both polar and rectangular are not calculated");
				e.printStackTrace();
			}
		*/	
			r = (Math.sqrt(i*i + j*j + k*k));
			
			theta = Math.PI + Math.atan2(j,i);
			
			if (k == 0) {
				phi = Math.PI/2;
			}else {
				phi = Math.acos(k/r);
			}
			polarCalculated = true;
		}
	}
	
	/**
	 * {@summary calculates the rectangular values based on the polar values}
	 */
	@Override
	protected void polarToRectangular() {
		if (! rectangularCalculated) {
		/*	//error detection removed for performance
			if (! polarCalculated) {
				Exception e = new Exception("both polar and rectangular are not calculated");
				e.printStackTrace();
			}
		*/	
			i = -r * Math.cos(theta) * Math.sin(phi);
			j = r * Math.sin(theta) * Math.sin(phi);
			k = r * Math.cos(phi);
			rectangularCalculated = true;
		}
	}
	
	/**
	 * 
	 * @param uI
	 * @param uJ
	 * @param uK
	 * @param vI
	 * @param vJ
	 * @param vK
	 * @return the dot product of Vectors u and v with the passed i,j,k values
	 */
	public static double dot(double uI, double uJ, double uK, double vI, double vJ, double vK) {
		return uI*vI + uJ*vJ + uK*vK;
	}

	/**
	 * 
	 * @param Vector3D u
	 * @param Vector3D v
	 * @return the dot product of u and v
	 */
	public static double dot(Vector3D u, Vector3D v) {
		return u.getI()*v.getI() + u.getJ()*v.getJ() + u.getK()*v.getK();
	}
	
	/**
	 * 
	 * @param Vector3D u
	 * @param mult
	 * @return a Vector3D that is the product of scaling u by mult
	 */
	public static Vector3D multiply(Vector u, double mult) {
		return new Vector3D(((Vector3D) u).getI() * mult, ((Vector3D) u).getJ() * mult, ((Vector3D) u).getK() * mult);
	}
	
	/**
	 * @return a Vector representing the scaling of this Vector by the multiple without changing this vector
	 */
	@Override
	public Vector3D statMultiply(double mult) {
		return new Vector3D(getI() * mult, getJ() * mult, getK() * mult);
	}
	
	
	
	/**
	 * scales this Vector by the multiple without changing this vector
	 * @param outputVec make this vec into the multiple
	 */
	public Vector3D statMultiplyInto(double mult, Vector3D outputVec) {
		outputVec.setIJK(getI() * mult, getJ() * mult, getK() * mult);
		return outputVec;
	}
	
	/**
	 * {@code WARNING this method uses the temp protocol. If the return isn't IMMEDIATELY used it may be overwritten causing terrible awful errors}
	 * @return a Vector representing the scaling of this Vector by the multiple without changing this vector
	 */
	@Override
	public Vector3D tempStatMultiply(double multi) {
		return statMultiplyInto(multi, tempVec);
	}
	
	/**
	 * @param Vector3D addVec
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector3D representing the addition of this Vector and the passed Vector
	 */
	@Override
	public Vector3D statAdd(Vector addVec) {
		return new Vector3D(getI() + ((Vector2D) addVec).getI(), getJ() + ((Vector2D) addVec).getJ(),getK() + ((Vector3D) addVec).getK());
	}
	
	/**
	 * @param Vector3D addVec
	 * @param outputVec puts the result of adding the passed Vector to this vector into the outputVec
	 * @return outputVec
	 */
	public Vector3D statAddInto(Vector addVec, Vector3D outputVec) {
		outputVec.setIJK(getI() + ((Vector2D) addVec).getI(), getJ() + ((Vector2D) addVec).getJ(),getK() + ((Vector3D) addVec).getK());
		return outputVec;
	}
	
	/**
	 * {@code WARNING this method uses the temp protocol. If the return isn't IMMEDIATELY used it may be overwritten causing terrible awful errors}
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector3D representing the addition of this Vector and the passed Vector
	 */
	@Override
	public Vector3D tempStatAdd(Vector addVec) {
		return statAddInto(addVec, tempVec);
	}
	
	/**
	 * @param Vector3D addVec
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector3D representing the addition of this Vector and the passed Vector
	 */
	public Vector3D statAddInto(Vector addVec) {
		return new Vector3D(getI() + ((Vector2D) addVec).getI(), getJ() + ((Vector2D) addVec).getJ(),getK() + ((Vector3D) addVec).getK());
	}
	
	/**
	 * @param addVec the vector to add 
	 * {@summary adds addVec to this vector}
	 */
	public Vector3D add(Vector3D addVec) {
		setIJK(getI() + addVec.getI(),getJ() + addVec.getJ(), getK() + addVec.getK());
		rectangularToPolar();
		return this;
	}
	
	/**
	 * @param addVec the vector to add 
	 * {@summary adds addVec to this vector}
	 */
	public Vector3D add(Vector addVec) {
		try {
			add((Vector3D) addVec);
		}catch(ClassCastException c) {
			super.add(addVec);
		}
		return this;
	}
	
	
	/**
	 * @param subVec the vector to subtract 
	 * {@summary adds addVec to this vector}
	 */
	public Vector3D subtract(Vector3D subVec) {	
		setIJK(getI() - subVec.getI(),getJ() - subVec.getJ(), getK() - subVec.getK());
		rectangularToPolar();
		return this;
	}
	
	/**
	 * @param addVec the vector to subtract 
	 * {@summary adds addVec to this vector}
	 */
	public Vector3D subtract(Vector subVec) {
		try {
			subtract((Vector3D) subVec);
		}catch(ClassCastException c) {
			super.subtract(subVec);
		}
		return this;
	}
	
	/**
	 * @param vec1
	 * @param vec2
	 * @return a Vector3D representing the addition of vec1 and vec2
	 */
	public static Vector3D add(Vector3D vec1, Vector3D vec2) {
		return new Vector3D(vec1.getI() + vec2.getI(), vec1.getJ() + vec2.getJ(), vec1.getK() + vec2.getK());
	}
	
	/**
	 * @param vec1
	 * @param vec2
	 * @return a Vector3D representing the subraction of vec1 and vec2
	 */
	public static Vector3D subtract(Vector3D vec1, Vector3D vec2) {
		return new Vector3D(vec1.getI() - vec2.getI(), vec1.getJ() - vec2.getJ(), vec1.getK() - vec2.getK());
	}
	
	/**
	 * 
	 * @param u
	 * @param q
	 * @return a Vector3D that is the cross product of u and q
	 */
	public static Vector3D cross(Vector3D u, Vector3D q) {
		return new Vector3D(u.getJ()*q.getK()-u.getK()*q.getJ(),-(u.getI()*q.getK()-u.getK()*q.getI()),u.getI()*q.getJ()-u.getJ()*q.getI());
	}
	
	/**
	 * 
	 * @param u
	 * @param v
	 * @return a Vector3D that is the projection of u onto v
	 */
	public static Vector3D proj(Vector3D u, Vector3D v) {
		return Vector3D.multiply(v,Vector3D.dot(u,v) / Math.pow(v.getR(),2));
	}
	
	/**
	 * @param uI the first Vec's i value
	 * @param uJ the first Vec's j value
	 * @param uK the first Vec's k value
	 * @param qI the second Vec's i value
	 * @param qJ the second Vec's j value
	 * @param qK the second Vec's k value
	 * @return a double[] array of the form {i,j,k} that represents the cross products of the vectors represented by the passed i,j,k values
	 */
	public static double[] cross(double uI, double uJ, double uK, double qI, double qJ, double qK) {
		return new double[] {uJ*qK-uK*qJ,-(uI*qK-uK*qI),uI*qJ-uJ*qI};
	}

	public double getK() {
		polarToRectangular();
		return k;
	}
	
	public double getPhi() {
		rectangularToPolar();
		return phi;
	}

	@Override
	public void setSize(double xSize, double ySize, double zSize) {
		super.setSize(xSize, ySize);
		this.k = zSize;
	}
	
	@Override
	public double getZSize() {
		return getK();
	}


	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public void setPos(double x, double y, double z) {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
	}
	/**
	 * @deprecated Vectors do not have coordinates
	 */
	@Override
	public Coordinate3D getCoordinates() {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
		return null;
	}
	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public double getX() {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
		return 0;
	}
	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public double getY() {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
		return 0;
	}
	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public double getZ() {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
		return 0;
	}
	
	@Override
	public String toString() {
		return "i: " + getI() + " , j: " + getJ() + " , k: " + getK() + " , theta: " + getTheta() + " , phi: " + getPhi();
	}

	

}
