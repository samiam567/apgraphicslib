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
		
		
		Cylinder square1 = new Cylinder(drawer, Settings.width/2, Settings.height/2,0,100,100,100,5);
		square1.setIsFilled(true);
		
		square1.setTexture("./src/LegendOfJava/assets/strawberry.jpg");
		drawer.add(square1);	
		
				
		drawer.start();
		

	//	cam.setCameraPanVelocity(new Vector2D(100,5));
	
	
		square1.setAngularVelocity(new Vector3D(-1,-0,-1));
	
		//wait for close
		while (drawer.getFrame().isVisible()) {
			try {
				Thread.sleep(1000);
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
