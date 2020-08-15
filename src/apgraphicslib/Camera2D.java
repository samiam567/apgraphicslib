package apgraphicslib;

import apgraphicslib.Physics_2DPolygon.Point2D;

/**
 * {@code a 2D camera. see Camera}
 * @author apun1
 *
 */
public class Camera2D extends Camera {
	
	protected Vector2D directionFacing = new Vector2D(0,1);
	protected Vector2D directionFacingBuffer = new Vector2D(0,1);
	
	public Camera2D(Coordinate2D cameraPosition) {
		super(cameraPosition);
		cameraPanVelocity = new Vector2D(0,0);
	}
	
	public void setPos(double x, double y) {
		cameraPosition.setX(x);
		cameraPosition.setY(y);
	}
	
	@Override
	public void Update(double frames) {
		updateDirectionFacing();
	}
	
	protected void updateDirectionFacing() {
		directionFacingBuffer.setIJ(0,1);
		directionFacingBuffer.rotate(cameraRotation);
		directionFacing.setIJ(directionFacingBuffer.getI(), directionFacingBuffer.getJ());
	}

	public void setCameraPosition(Point2D newPos) {
		cameraPosition = newPos;
	}
	
	public void setCameraPanVelocity(Vector2D newV) {
		cameraPanVelocity = newV;
	}
	
	public void setCameraAngularVelocity(Vector vector) {
		cameraAngularVelocity = vector;
	}
	
	public Coordinate2D getCameraPosition() {
		return cameraPosition;
	}
	
	public Vector2D getDirectionFacing() {
		return directionFacing;
	}
	
}
