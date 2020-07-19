package LegendOfJava2;

import apgraphicslib.Camera3D;
import apgraphicslib.Coordinate3D;

public class LOJ2Camera extends Camera3D {
	private LegendOfJava2 runner;
	public LOJ2Camera(LegendOfJava2 runner, Coordinate3D cameraPosition) {
		super(cameraPosition);
		this.runner = runner;
	}

	@Override
	public void prePaintUpdate() {
		setCameraPanVelocity(runner.Ryan.getSpeed());
		super.prePaintUpdate();
	}

}
