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

	
	public LOJ2Camera(LegendOfJava2 runner, Coordinate3D cameraPosition) {
		super(cameraPosition);
		this.runner = runner;
		directionFacing = new Vector3D_Drawable(0,0,1000,runner.drawer, 0,0);
		
	}

	@Override
	public void prePaintUpdate() {
		setCameraPanVelocity(runner.Ryan.getSpeed());
		((Vector3D_Drawable) directionFacing).setCoordinates(runner.Ryan.getCoordinates());
		super.prePaintUpdate();
	}

	public Vector3D getDirectionFacing() {
		return directionFacing;
	}

}
