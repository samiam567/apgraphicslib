package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary a coordinate in the form {x,y,..}. 
 * Can be any number of dimensions}
 *
 */
public abstract class Coordinate implements One_dimensional {
	protected double x;
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return x;
	}
}
