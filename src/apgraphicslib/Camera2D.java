package apgraphicslib;

import apgraphicslib.Physics_2DPolygon.Point2D;

/**
 * {@code a 2D camera. see Camera}
 * @author apun1
 *
 */
public class Camera2D extends Camera {
	
	public Camera2D(Coordinate2D cameraPosition) {
		super(cameraPosition);
		cameraPanVelocity = new Vector2D(0,0);
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
	
}
