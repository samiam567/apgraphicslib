package apgraphicslib;

public class Coordinate3D extends Coordinate2D implements Three_dimensional {
	double z;
	public Coordinate3D(double x, double y, double z) {
		super(x, y);
		this.z = z;
		
	}
	
	public Coordinate3D getCoordinates() {
		return this;
	}
	
	public double getZ() {
		return z;
	}
	
	@Override
	public void setPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void setPos(Coordinate2D newCoords) {
		try {
			setPos(newCoords.getX(), newCoords.getY(), ((Coordinate3D) newCoords).getZ());	
		}catch(ClassCastException c) {
			super.setPos(newCoords);
		}
	}
	
	/**
	 * {@summary adds the passed coordinate's values to this one's}
	 * @param addV
	 */
	@Override
	public void add(Coordinate addCoord) {	
		try {
			x += ((Coordinate2D) addCoord).getX();
			y += ((Coordinate2D) addCoord).getY();
		
			z += ((Coordinate3D) addCoord).getZ();
	
		}catch(ClassCastException c) {}
		
	}
	
	/**
	 * {@summary subtracts the passed coordinate's values from this one's}
	 * @param addV
	 */
	@Override
	public void subtract(Coordinate addCoord) {	
		try {
			x -= ((Coordinate2D) addCoord).getX();
			y -= ((Coordinate2D) addCoord).getY();
		
			z -= ((Coordinate3D) addCoord).getZ();
	
		}catch(ClassCastException c) {}
		
	}
	

	/**
	 * {@summary adds the vector to the coordinate as if it were a vector. (x + i, y + j, z + k)}
	 * @param addV
	 */
	@Override
	public void add(Vector2D addV) {

		x += addV.getI();
		y += addV.getJ();
		

		try {
			z += ((Vector3D) addV).getK();
	
		}catch(ClassCastException c) {}
		
	}
	
	/**
	 * @deprecated a coordinate does not have a size
	 */
	@Override
	public void setSize(double xSize, double ySize, double zSize) {
		super.setSize(xSize,ySize);
		Exception e = new Exception("a coordinate does not have a size");
		e.printStackTrace();
	}
	
	/**
	 * @deprecated a coordinate does not have a size
	 */
	@Override
	public double getZSize() {
		Exception e = new Exception("a coordinate does not have a size");
		e.printStackTrace();
		return 0;
	}
	
	@Override
	public String toString() {
		return "" + getX() + "," + getY() + "," + getZ();
	}
}
