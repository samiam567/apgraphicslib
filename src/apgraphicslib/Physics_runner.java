package apgraphicslib;

import shapes.Cylinder;

public class Physics_runner {

	private static Object_draw drawer;

	
	public static void main(String[] args) {
		Settings.perspective = true;
		Settings.targetFPS = 40;
		
		
		drawer = new Object_draw();
		
		
		drawer.add(new Object_border_tether(drawer));
		FPS_display fps = new FPS_display(drawer, Settings.width * 0.01 + 35, Settings.height*0.05);
		drawer.add(fps);
		
		
		FCPS_display fcps = new FCPS_display(drawer, Settings.width * 0.01 + 45, Settings.height*0.05 + 15);		
		drawer.add(fcps);
		
		
		Vector3D_Drawable vec1 = new Vector3D_Drawable(100,200,0,drawer, drawer.getFrameCenterX(), drawer.getFrameCenterY());
		drawer.add(vec1);
				
		drawer.start();
		

		Vector rotation = new Vector2D(0,0);
		
		long tryStartTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			try {
				((Vector3D) rotation).setK(Math.random());
			}catch(ClassCastException c) {
				((Vector2D) rotation).setJ(-Math.random());
			}
		}
		long tryEndTime = System.nanoTime();
		
		long ifStartTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			if (rotation.getClass().equals(Vector3D.class)) {
				((Vector3D) rotation).setK(Math.random());
			} else {
				((Vector2D) rotation).setJ(-Math.random());
			}
		}
		long ifEndTime = System.nanoTime();
		
		System.out.println("try: " + (tryEndTime-tryStartTime));
		System.out.println("if: " + (ifEndTime-ifStartTime));
		System.out.println("difference: " + (((double)(ifEndTime-ifStartTime))/((double)(tryEndTime-tryStartTime))) );
		System.out.println("difference: " + (((double)(tryEndTime-tryStartTime))/((double)(ifEndTime-ifStartTime))) );
		
		
		System.exit(1);
		
		//wait for close
		while (drawer.getFrame().isVisible()) {
			vec1.rotate(rotation);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("stopping");
     	drawer.stop();
     	System.out.println("stopped");
		System.exit(1);
	}
}
