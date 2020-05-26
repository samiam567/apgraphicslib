package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary the lowest dimensional Coordinate. Higher dimensional coordinates will be children of this one.
 * Works in 2D with an x and y values}
 *
 */
public class Coordinate2D extends Coordinate implements Two_dimensional {
	protected double y;
	
	public Coordinate2D(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	@Override
	public double getY() {
		return y;
	}
	
	/**
	 * {@summary adds the vector to the coordinate as if it were a vector. (x + i, y + j)}
	 * @param addV
	 */
	public void add(Vector2D addV) {
		x += addV.getI();
		y += addV.getJ();
	}

	/**
	 * @deprecated a coordinate does not have a size
	 */
	@Override
	public void setSize(double xSize, double ySize) {
		Exception e = new Exception("a coordinate does not have a size");
		e.printStackTrace();
	}

	
	/**
	 * @deprecated a coordinate does not have a size
	 */
	@Override
	public double getXSize() {
		return 0;
	}
	
	/**
	 * @deprecated a coordinate does not have a size
	 */
	@Override
	public double getYSize() {
		return 0;
	}
	

	@Override
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Coordinate2D getCoordinates() {
		return this;
	}

	@Override
	public void setX(double x) {
		this.x = x;
		
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "" + getX() + "," + getY();
	}

	public void setPos(Coordinate2D newCoords) {
		setPos(newCoords.getX(), newCoords.getY());	
	}
	
	/**
	 * {@summary adds the passed coordinate's values to this one's}
	 * @param addV
	 */
	public void add(Coordinate addCoord) {
		try {
			x += ((Coordinate2D) addCoord).getX();
			y += ((Coordinate2D) addCoord).getY();
		}catch(ClassCastException c) {}		
	}
	
	/**
	 * {@summary subtracts the passed coordinate's values from this one's}
	 * @param addV
	 */
	public void subtract(Coordinate addCoord) {
		try {
			x -= ((Coordinate2D) addCoord).getX();
			y -= ((Coordinate2D) addCoord).getY();
		}catch(ClassCastException c) {}		
	}
}
