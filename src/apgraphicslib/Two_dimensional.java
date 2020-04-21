package apgraphicslib;

/**
 * 
 * @author samiam567
 *{@summary a Two dimensional object. Has x,y, length, and width values as well as a center position}
 */
public interface Two_dimensional {
	public void setPos(double x, double y);
	public void setX(double x);
	public void setY(double y);
	public double getX();
	public double getY();
	
	public void setSize(double xSize, double ySize);
	public double getXSize();
	public double getYSize();
	public Coordinate2D getCoordinates();
}
