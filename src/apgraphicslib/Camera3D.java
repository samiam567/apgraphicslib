package apgraphicslib;

import apgraphicslib.Physics_2DPolygon.Point2D;
import apgraphicslib.Physics_3DPolygon.Point3D;

public class Camera3D extends Camera2D {
	
	public Camera3D(Coordinate3D cameraPosition) {
		super(cameraPosition);
		
		this.cameraPosition = new Point3D(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());
		
		cameraPanVelocity = new Vector3D();
		cameraRotation = new Vector3D();
		cameraAngularVelocity = new Vector3D();
		
		orbitalRotation = new Vector3D();
		orbitalAngularVelocity = new Vector3D();
		orbitalAngularAcceleration = new Vector3D();
		
		rotationMatrix = new AffineRotation3D();
		pORCoordsTemp = new Coordinate3D(0,0,0);
	}

	@Override
	public void setCameraPosition(Point2D newPos) {
		((Coordinate3D) cameraPosition).setPos(newPos.getX(), newPos.getY(),0);
	}
	public void setCameraPosition(Point3D newPos) {
		cameraPosition = newPos;
	}
	
	public void setPos(double x, double y, double z) {
		((Coordinate3D) cameraPosition).setPos(x, y, z);
	}
	
	@Override
	public void setCameraPanVelocity(Vector2D newV) {
		((Vector3D) cameraPanVelocity).setIJK(newV.getI(), newV.getJ(),((Vector3D) cameraPanVelocity).getK());
	}
	public void setCameraPanVelocity(Vector3D newV) {
		cameraPanVelocity = newV;
	}
	
	@Override
	public void setCameraAngularVelocity(Vector newAngV) {
		((Vector3D)getCameraAngularVelocity()).setR(newAngV.getR());
	}
	public void setCameraAngularVelocity(Vector3D newAngV) {
		cameraAngularVelocity = newAngV;
	}

	public Coordinate3D getCameraPosition() {
		return (Coordinate3D) cameraPosition;
	}
	
	

}
