package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;

public class Vector3D_Drawable extends Vector3D implements Drawable {
	protected Coordinate3D coordinates;
	private boolean isVisible = true;
	
	private String name;
	
	private Object_draw drawer;
	private Color color = Color.BLACK;
	
	public Vector3D_Drawable(double i, double j, double k, Object_draw drawer, double x, double y) {
		super(i,j,k);
		coordinates = new Coordinate3D(x,y,0);
		this.drawer = drawer;
	}

	@Override
	public double getX() {
		return coordinates.getX();
	}

	@Override
	public double getY() {
		return coordinates.getY();
	}
	
	public double getZ() {
		return coordinates.getZ();
	}
	
	
	public void setColor(Color newColor) {
		color = newColor;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public Coordinate3D getCoordinates() {
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

	public void setCoordinates(Coordinate3D newCoords) {
		this.coordinates = newCoords;
	}
	
	public void setPos(double x, double y, double z) {
		this.coordinates.setPos(x, y, z);
	}
	
	public boolean getIsVisible() {
		return isVisible;
	}



	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public double getPaintOrderValue(Camera cam) {
		return Physics_engine_toolbox.distance(getCoordinates(), cam.getCoordinates());
	}



	@Override
	public String getName() {
		return name;
	}



	@Override
	public void setName(String newName) {
		this.name = newName;
		
	}



	@Override
	public Object_draw getDrawer() {
		return drawer;
	}

	
	public void paint(Graphics page) {
		page.drawLine((int) getX(),(int) getY(),(int) (Settings.width/2 + getI()),(int) ( Settings.height/2 + getJ()));
	}


}
