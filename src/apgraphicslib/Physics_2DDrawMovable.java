package apgraphicslib;

public abstract class Physics_2DDrawMovable extends Physics_drawable implements Updatable, Movable {

	protected Vector2D speed, acceleration;
	public Physics_2DDrawMovable(Object_draw drawer, double x, double y) {
		super(drawer, x, y);
		speed = new Vector2D(0,0);
		acceleration = new Vector2D(0,0);
	}

	/**
	 * @param frames the frameStep of the Object_draw that this object is a part of 
	 * ALL children should call super.Update(frames) in their Update(frames) method
	 */
	@Override
	public void Update(double frames) {
		getCoordinates().add(speed.tempStatMultiply(frames));
		speed.add(acceleration.tempStatMultiply(frames));
	}
	
	@Override 
	public void prePaintUpdate() {
		//do nothing
	}
	
	
	

	
	/**
	 * {@summary sets the speed of the object}
	 * @param newSpeed a Vector representing the new speed of the object. If the passed speed is a lower dimensional Vector then our speed, we will use that Vector's dimensions that we are given
	 */
	@Override
	public void setSpeed(Vector newSpeed) {
		if (Vector2D.class.isAssignableFrom(newSpeed.getClass())) {
			speed = (Vector2D) newSpeed;
		}else{ //if the speed vector has too few dimensions, just use the ones we are given
			speed.setR(newSpeed.getR());
		}
		
	}

	@Override
	public Vector2D getSpeed() {
		return speed;
	}

	/**
	 * sets the acceleration of the object. This must be overridden buy higher dimensional objects
	 */
	@Override
	public void setAcceleration(Vector newAcceleration) {
		if (Vector2D.class.isAssignableFrom(newAcceleration.getClass())) {
			acceleration = (Vector2D) newAcceleration;
		}else{ //if the speed vector has too few dimensions, just use the ones we are given
			speed.setR(newAcceleration.getR());
		}
		
	}

	@Override
	public Vector2D getAcceleration() {
		return acceleration;
	}


}
