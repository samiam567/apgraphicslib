package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary a coordinate in the form {x,y,..}. 
 * Can be any number of dimensions}
 *
 */
public abstract class Coordinate implements One_dimensional {
	public double x;
	
	public void add(Vector tempStatMultiply) {
		
	}
	
	public double getX() {
		return x;
	}
}
