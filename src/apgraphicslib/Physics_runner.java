package apgraphicslib;

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
				
		
		

		Vector rotation = new Vector();
		
		long tryStartTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			try {
				((Vector3D) rotation).setK(Math.random());
			}catch(ClassCastException c) {
				try {
					((Vector2D) rotation).setJ(1);
				}catch(ClassCastException d) {
					rotation.setR(1);
				}
			}
		}
		long tryEndTime = System.nanoTime();
		
		long ifStartTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			if (Vector3D.class.isAssignableFrom(rotation.getClass())) {
				((Vector3D) rotation).setK(Math.random());
			} else if ((Vector2D.class.isAssignableFrom(rotation.getClass()))) {
				((Vector2D) rotation).setJ(-Math.random());
			}else {
				rotation.setR(1);
			}
		}
		long ifEndTime = System.nanoTime();
		
		System.out.println("try: " + (tryEndTime-tryStartTime));
		System.out.println("if:  " + (ifEndTime-ifStartTime));
		System.out.println("difference: " + (((double)(ifEndTime-ifStartTime))/((double)(tryEndTime-tryStartTime))) + " of the try time" );
		System.out.println("difference: " + (((double)(tryEndTime-tryStartTime))/((double)(ifEndTime-ifStartTime))) + " times faster");
		
		
		
		
		drawer.start();
		
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
