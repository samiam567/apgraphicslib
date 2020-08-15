package LegendOfJava2;

import apgraphicslib.Camera3D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Drawable;
import apgraphicslib.Physics_engine_compatible;
import apgraphicslib.Vector3D;
import apgraphicslib.Vector3D_Drawable;

public class LOJ2Camera extends Camera3D {
	private LegendOfJava2 runner;
	
	

	
	public LOJ2Camera(LegendOfJava2 runner, Coordinate3D cameraPosition) {
		super(cameraPosition);
		this.runner = runner;
	
		
	}


	@Override
	public void prePaintUpdate() {
		super.prePaintUpdate();
	}

	

}
