package apgraphicslib;

public class Camera3D extends Camera2D {
	
	public Camera3D(Coordinate3D cameraPosition) {
		super(cameraPosition);
		cameraPanVelocity = new Vector3D(0,0,0);
		cameraRotation = new Vector3D(0,0,0);
		cameraAngularVelocity = new Vector3D(0,0,0);
	}

	@Override
	public void setCameraPosition(Coordinate2D newPos) {
		cameraPosition.setPos(newPos.getX(), newPos.getY());
	}
	public void setCameraPosition(Coordinate3D newPos) {
		cameraPosition = newPos;
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
		((Vector3D)cameraAngularVelocity).setR(newAngV.getR());
	}
	public void setCameraAngularVelocity(Vector3D newAngV) {
		cameraAngularVelocity = newAngV;
	}
	
	

}
