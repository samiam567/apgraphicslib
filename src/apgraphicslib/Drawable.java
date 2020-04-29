package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;



/**
 * @author samiam567
 * {@summary Object can be drawn out on the screen.
 * Must have 2D coordinates and a paint method}
 */
public interface Drawable extends Physics_engine_compatible {
	public void paint(Graphics page);

	public double getX();
	public double getY();
	public Coordinate getCoordinates();
	
	public void setColor(Color newColor);
	public Color getColor();
	
	/**
	 * {@summary this controls the order at which the objects are painted. the higher the number, the further back the object will be and it will be painted earlier}
	 * @return the paint order value
	 */
	public double getPaintOrderValue(); 
	
	public boolean getIsVisible();
}
