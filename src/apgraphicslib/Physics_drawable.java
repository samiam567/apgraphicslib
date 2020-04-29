package apgraphicslib;

import java.awt.Color;

/**
 * @author samiam567
 * {@summary Generic class that implements Drawable. @see Drawable }
 */
public abstract class Physics_drawable extends Physics_object implements Drawable, Two_dimensional {

	protected Coordinate2D coordinates;
	private double xSize, ySize;
	protected double maxSize;
	private boolean isVisible = true;
	
	private Color color = Color.BLACK;
	
	public Physics_drawable(Object_draw drawer, double x, double y) {
		super(drawer);
		coordinates = new Coordinate2D(x,y);
	}
	
	
	
	public void setSize(double xSize, double ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		maxSize = Math.sqrt(xSize*xSize+ySize*ySize);
	}
	


	@Override
	public double getX() {
		return coordinates.getX();
	}

	@Override
	public double getY() {
		return coordinates.getY();
	}
	
	public void setColor(Color newColor) {
		color = newColor;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public Coordinate2D getCoordinates() {
		return coordinates;
	}

	@Override
	public void setPos(double x, double y) {
		coordinates.setPos(x, y);
		
	}
	
	public void setX(double x) {
		coordinates.setX(x);
	}
	
	public void setY(double y) {
		coordinates.setY(y);
	}


	@Override
	public double getXSize() {
		return xSize;
	}

	@Override
	public double getYSize() {
		return ySize;
	}
	
	/**
	 * {@summary this controls the order at which the objects are painted. the higher the number, the further back the object will be and it will be painted earlier}
	 */
	public double getPaintOrderValue() { 
		try {
			return ((Three_dimensional) this).getZ();
		}catch(ClassCastException c) {
			return -Settings.distanceFromScreen - 0.1; //always paint 2D components on top
		}
	}



	public boolean getIsVisible() {
		return isVisible;
	}



	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	


	
}
