package apgraphicslib;

import java.awt.Graphics;

/**
 * 
 * @author samiam567
 *{@summary the 2D implementation of the Vector class. @see Vector  contains i,j,r,and theta}
 */
public class Vector2D extends Vector implements Two_dimensional {
	protected double i,j,theta;
	protected boolean rectangularCalculated = true, polarCalculated = true;
	private static Vector2D tempVec = new Vector2D(0,0);
	
	public Vector2D(int key) {
		if (key != 1657934) {
			Exception e = new Exception("ONLY Dynamic Vectors can use this constructor");
			e.printStackTrace();
		}
	}
	
	public Vector2D(double xComponent, double yComponent) {
		setI(xComponent);
		setJ(yComponent);
	}
	
	public Vector2D(Coordinate2D pI, Coordinate2D pF) {
		setI(pF.getX() - pI.getX());
		setJ(pF.getY() - pI.getY());
	}

	
	
	/**
	 * @author samiam567
	 * {@summary converts the rectangular values and updates the angles and r of the Vector}
	 */
	protected void rectangularToPolar() {
		if (! polarCalculated) {
			theta = Math.atan2(j,i);
			r = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
			polarCalculated = true;
		}
	}
	
	/** 
	 * @param xComponent the x component of the Vector (i)
	 * @param yComponent the y component of the Vector (j)
	 * @return a double array representing the polar values of a vector corresponding to the passed components in the form {r,theta}
	 */
	public static double[] rectangularToPolar(double xComponent,double yComponent) {
		return new double[] {Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2)),Math.atan2(yComponent,xComponent)};
	}
	
	/**
	 * @author samiam567
	 * {@summary takes the polar values of the Vector and updates the rectangular values based off of them}
	 */
	protected void polarToRectangular() {
		if (! rectangularCalculated) {
			i = r * Math.cos(theta);
			j = r * Math.sin(theta);
			rectangularCalculated = true;
		}
	}
	
	/**
	 * @author samiam567
	 * @param r the magnitude of the vector
	 * @param theta the angle between the x and y axis
	 * @return an array representing the rectangular values of a vector with the given polar values in the form {i,j}
	 */
	public static double[] polarToRectangular(double r, double theta) {
		return new double[] {r * Math.cos(theta), r * Math.sin(theta) };
	}
	
	public double getI() {
		polarToRectangular();
		return i;
	}
	public double getJ() {
		polarToRectangular();
		return j;
	}
	
	@Override
	public double getR() {
		rectangularToPolar();
		return r;
	}
	public double getTheta() {
		rectangularToPolar();
		return theta;
	}
	
	/**
	 * 
	 * @param r
	 * 
	 * {@summary Should NEVER be used inside a Vector class because it calls polarToRectangular()}
	 * {@code sets the r of the vector and then calls polarToRectangular() @see polarToRectangular()}
	 */
	@Override
	public void setR(double r) {
		rectangularToPolar();
		this.r = r;
		rectangularCalculated = false;
	}

	
	/**
	 * {@code sets the rectangular components of this Vector2D}
	 * @param i the new i component
	 * @param j the new j component
	 */
	public void setIJ(double i, double j) {
		this.i = i;
		this.j = j;
		rectangularCalculated = true;
		polarCalculated = false;
	}
	
	public void setI(double newI) {
		polarToRectangular();
		i = newI;
		polarCalculated = false;
	}

	public void setJ(double newJ) {
		polarToRectangular();
		this.j = newJ;
		polarCalculated = false;
	}

	/**
	 * @param Vector2D addVec
	 * {@summary adds the passed vector to this Vector}
	 */
	public Vector2D add(Vector addVec) {
		try {
			setI(((Vector2D) addVec).getI() + getI());
			setJ(((Vector2D) addVec).getJ() + getJ());
		}catch(ClassCastException c) {
			setR(getR() + addVec.getR());
		}
    	return this;
	}
	
	public Vector2D subtract(Vector sVec) {
		try {
			Vector2D subVec = (Vector2D) sVec;
			i -= subVec.getI();
			j -= subVec.getJ();
			rectangularToPolar();
		}catch(ClassCastException c) {
			setR(getR()-sVec.getR());
		}
		return this;
	}


	
	public static Vector2D add(Vector2D vec1, Vector2D vec2) {
		return new Vector2D(vec1.getI() + vec2.getI(),vec1.getJ() + vec2.getJ());
	}
	
	/**
	 * @param Vector2D addVec
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector2D representing the addition of this Vector and the passed Vector
	 */
	public Vector2D statAdd(Vector addVec) {
		return new Vector2D(i + ((Vector2D) addVec).getI(), j + ((Vector2D) addVec).getJ());
	}
	
	/**
	 * @param Vector3D addVec
	 * @param outputVec puts the result of adding the passed Vector to this vector into the outputVec
	 * @return outputVec
	 */
	public Vector statAddInto(Vector addVec, Vector outputVec) {
		try {
			Vector2D outVec = (Vector2D) outputVec;
			outVec.setI(getI() + ((Vector2D) addVec).getI());
			outVec.setJ(getJ() + ((Vector2D) addVec).getJ());
			return outVec;
		}catch(ClassCastException c) {
			return super.statAddInto(addVec, outputVec);
		}
		
	}
	
	/**
	 * {@code WARNING this method uses the temp protocol. If the return isn't IMMEDIATELY used it may be overwritten causing terrible awful errors}
	 * {@summary adds the passed Vector to this Vector without changing this Vector}
	 * @return a Vector3D representing the addition of this Vector and the passed Vector
	 */
	public Vector tempStatAdd(Vector addVec) {
		return statAddInto(addVec, tempVec);
	}

	public static Vector2D subtract(Vector2D vec1, Vector2D vec2) {
		return new Vector2D(vec1.getI() - vec2.getI(),vec1.getJ() - vec2.getJ());
	}
	
	/**
	 * @param double multi
	 * {@summary scales this Vector2D by the passed value}
	 * @return 
	 */
	public Vector2D multiply(double multi) {
		setR(getR() * multi);
		rectangularCalculated = false;
		return this;
	}
	
	/**
	 * @param double multi
	 * {@summary returns a Vector2D representing the result of scaling this Vector by the multi}
	 */
	public Vector2D statMultiply(double multi) {
		return new Vector2D(getI() * multi,getJ() * multi);
	}
	
	/**
	 * scales this Vector by the multiple without changing this vector
	 * @param outputVec make this vec into the multiple
	 */
	public Vector2D statMultiplyInto(double mult, Vector2D outputVec) {
		outputVec.setI(getI() * mult);
		outputVec.setJ(getJ() * mult);
		return outputVec;
	}
	
	/**
	 * {@code WARNING this method uses the temp protocol. If the return isn't IMMEDIATELY used it may be overwritten causing terrible awful errors}
	 * @return a Vector representing the scaling of this Vector by the multiple without changing this vector
	 */
	public Vector2D tempStatMultiply(double multi) {
		return statMultiplyInto(multi, tempVec);
	}
	
	/**
	 * {@summary rotates this Vector using AffineRotation}
	 * @param rotation
	 */
	public void rotate(Vector rotation) {
		rotate(new AffineRotation(rotation));
	}
	
	/**
	 * {@summary rotates this Vector the passed affineRotation}
	 * @param affRot
	 */
	public void rotate(AffineRotation rotation) {
		setIJ(rotation.a * getI() + rotation.b * getJ(), rotation.c * getI() + rotation.d * getJ());
	}
	
	public void setTheta(double theta1) {
		theta = theta1;
		polarToRectangular();
	}
	
	@Override
	public void setSize(double xSize, double ySize) {
		setI(xSize);
	    setJ(ySize);
	}
	

	@Override
	public double getXSize() {
		return getI();
	}

	@Override
	public double getYSize() {	
		return getJ();
	}
	
	@Override
	public String toString() {
		return "i: " + getI() + ", j: " + getJ() + ", r: " + getR() + ", theta: "  + getTheta();
	}
	
	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public void setPos(double x, double y) {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
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
	 * @deprecated Vectors do not have coordinates
	 */
	@Override
	public Coordinate2D getCoordinates() {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
		return null;
	}

	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public void setX(double x) {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
	}

	/**
	 * @deprecated vectors do not have coordinates
	 */
	@Override
	public void setY(double y) {
		Exception e = new Exception("Vectors do not have coordinates");
		e.printStackTrace();
	}


	public void paint(Graphics page) {
		page.drawLine( Settings.width/2, Settings.height/2,(int) (Settings.width/2 + getI()),(int) ( Settings.height/2 + getJ()));
	}
	
}
