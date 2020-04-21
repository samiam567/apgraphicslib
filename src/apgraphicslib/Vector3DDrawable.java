package apgraphicslib;

import java.awt.Graphics;

public class Vector3DDrawable extends Vector3D implements Drawable {
	
	protected Coordinate3D coordinates;
	private String name = "Unnamed Vector3DDrawable";
	private Object_draw drawer;
	
	public Vector3DDrawable(Object_draw drawer, double i, double j, double k, double x, double y, double z) {
		super(i, j, k);
		this.drawer = drawer;
		coordinates = new Coordinate3D(x,y,z);
	}
	
	
	
	public void setSize(double xSize, double ySize) {
		this.i = xSize;
		this.j = ySize;
	}
	


	@Override
	public double getX() {
		return coordinates.getX();
	}

	@Override
	public double getY() {
		return coordinates.getY();
	}
	
	@Override 
	public double getZ() {
		return coordinates.getZ();
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

	public double getPaintOrderValue() {
		return getZ();
	}

	@Override
	public String getName() {
		return name;
	}



	@Override
	public void setName(String newName) {
		name = newName;
	}



	@Override
	public Object_draw getDrawer() {
		return drawer;
	}



	@Override
	public void paint(Graphics page) {
		page.drawRect((int) getX()-1,(int) getY()-1,(int) (2),(int) (2));
		page.drawLine((int) getX(),(int) getY(),(int) (getX() + getI()),(int) (getY() + getJ()));
		
	}





}
