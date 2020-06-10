package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary a coordinate in the form {x,y,..}. 
 * Can be any number of dimensions}
 *
 */
public class Coordinate implements One_dimensional {
	protected double x;
	
	
	/**
	 * {@summary adds the vector to the coordinate as if it were a vector. (x + i, y + j)}
	 * @param addV
	 */
	public void add(Vector addV) {
		x += addV.getR();
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return x;
	}
}
