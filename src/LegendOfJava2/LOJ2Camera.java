package LegendOfJava2;

import apgraphicslib.Camera3D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Drawable;
import apgraphicslib.Physics_engine_compatible;
import apgraphicslib.Vector3D;
import apgraphicslib.Vector3D_Drawable;

public class LOJ2Camera extends Camera3D {
	private LegendOfJava2 runner;
	
	private Vector3D directionFacing = new Vector3D(0,0,1);
	private Vector3D directionFacingBuffer = new Vector3D(0,0,1);

	
	public LOJ2Camera(LegendOfJava2 runner, Coordinate3D cameraPosition) {
		super(cameraPosition);
		this.runner = runner;
	
		
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		directionFacingBuffer.setIJK(0,0,1);
		directionFacingBuffer.rotate(cameraRotation);
		directionFacing.setIJK(directionFacingBuffer.getI(), directionFacingBuffer.getJ(), directionFacingBuffer.getK());
	}

	@Override
	public void prePaintUpdate() {
		super.prePaintUpdate();
	}

	public Vector3D getDirectionFacing() {
		return directionFacing;
	}

}
