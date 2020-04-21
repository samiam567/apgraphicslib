package apgraphicslib;

/**
 * 
 * @author samiam567
 *{@summary this class isn't used for anything but is a template for how a 3DDrawMovable is created }
 */
public abstract class Physics_3DDrawMovable extends Physics_2DDrawMovable implements Three_dimensional {
	
	
	protected double zSize;
	
	public Physics_3DDrawMovable(Object_draw drawer, double x, double y, double z) {
		super(drawer,x,y);
		coordinates = new Coordinate3D(x,y,z);
		setSpeed(new Vector3D(0,0,0));
		setAcceleration(new Vector3D(0,0,0));
	}
	
	@Override
	public void setSpeed(Vector newSpeed) {
		try { //vec is 3d
			speed = ((Vector3D) newSpeed);
		}catch(ClassCastException c) { //if the speed vector has too few dimensions, just use the ones we are given
			try{ //vec is 2d
				((Vector3D) getSpeed()).setSize(((Vector2D) newSpeed).getI(), ((Vector2D) newSpeed).getJ());
			}catch(ClassCastException e) {
				super.setSpeed(newSpeed); //let the super class deal with this one-dimensional vector
			}
		}
		
	}
	
	/**
	 * {@summary sets the size of the object. This may be interpreted differently by different objects}
	 * {@code also updates maxSize}
	 */
	public void setSize(double xSize, double ySize) {
		super.setSize(xSize, ySize);
		maxSize = Math.sqrt(xSize*xSize+ySize*ySize+zSize*zSize);
	}
	
	/**
	 * {@summary sets the size of the object. This may be interpreted differently by different objects}
	 * {@code also updates maxSize}
	 */
	public void setSize(double xSize, double ySize, double zSize) {
		super.setSize(xSize,ySize);
		this.zSize = zSize;
		maxSize = Math.sqrt(xSize*xSize+ySize*ySize+zSize*zSize);
	}


	@Override
	public void setPos(double x, double y, double z) {
		((Coordinate3D) coordinates).setPos(x,y,z);
		
	}

	/**
	 * sets the acceleration of the object. This must be overridden buy higher dimensional objects
	 */
	@Override
	public void setAcceleration(Vector newAcceleration) {
		try { //vec is 3d
			acceleration = ((Vector3D) newAcceleration);
		}catch(ClassCastException c) { //if the acceleration vector has too few dimensions, just use the ones we are given
			try{ //vec is 2d
				((Vector3D) getAcceleration()).setSize(((Vector2D) newAcceleration).getI(), ((Vector2D) newAcceleration).getJ());
			}catch(ClassCastException e) {
				super.setAcceleration(newAcceleration); //let the super class deal with this one-dimensional vector
			}
		}
	}
	
	@Override
	public Coordinate3D getCoordinates() {
		return (Coordinate3D) coordinates;
	}


	public double getZ() {
		return ((Coordinate3D) coordinates).getZ();
	}

	@Override
	public double getZSize() {
		return zSize;
	}
	
	/**
	 * {@summary this controls the order at which the objects are painted. the higher the number, the further back the object will be and it will be painted earlier}
	 */
	@Override
	public double getPaintOrderValue() { 	
		return getZ();
	}

}
