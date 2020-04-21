package apgraphicslib;

public interface Movable extends Updatable {
	public void setSpeed(Vector newSpeed);
	public Vector getSpeed();
	
	public void setAcceleration(Vector newAcceleration);
	public Vector getAcceleration();
}
