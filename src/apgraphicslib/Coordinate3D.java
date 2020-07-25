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
		if (Coordinate3D.class.isAssignableFrom(newCoords.getClass())) {
			setPos(newCoords.getX(), newCoords.getY(), ((Coordinate3D) newCoords).getZ());	
		}else{
			super.setPos(newCoords);
		}
	}
	
	/**
	 * {@summary adds the passed coordinate's values to this one's}
	 * @param addV
	 */
	@Override
	public void add(Coordinate addCoord) {	
		if (Coordinate3D.class.isAssignableFrom(addCoord.getClass())) {
			x += ((Coordinate2D) addCoord).getX();
			y += ((Coordinate2D) addCoord).getY();
			z += ((Coordinate3D) addCoord).getZ();
		}else {
			super.add(addCoord);
		}
		
	}
	
	/**
	 * {@summary subtracts the passed coordinate's values from this one's}
	 * @param addV
	 */
	@Override
	public void subtract(Coordinate subCoord) {	
		if (Coordinate3D.class.isAssignableFrom(subCoord.getClass())) {
			x -= ((Coordinate2D) subCoord).getX();
			y -= ((Coordinate2D) subCoord).getY();
			z -= ((Coordinate3D) subCoord).getZ();
		}else {
			super.subtract(subCoord);
		}
	}
	
	/**
	 * {@summary adds the vector to the coordinate as if it were a vector. (x + i, y + j, z + k)}
	 * @param addV
	 */
	@Override
	public void add(Vector2D addV) {

		x += addV.getI();
		y += addV.getJ();
		

		if (Vector3D.class.isAssignableFrom(addV.getClass())) {
			z += ((Vector3D) addV).getK();
		}
		
	}
	
	@Override
	public void add(Vector addV) {
		if (Vector2D.class.isAssignableFrom(addV.getClass())) {
			add((Vector2D) addV);
		}else {
			super.add(addV);
		}
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
