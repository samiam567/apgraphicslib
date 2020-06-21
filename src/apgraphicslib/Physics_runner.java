package apgraphicslib;

import shapes.Cylinder;
import shapes.Egg;
import shapes.Rectangle;
import shapes.Rectangle3D;
import shapes.Rectangle_Textured;
import shapes.Square;

public class Physics_runner {

	private static Object_draw drawer;

	
	
	
	public static void main(String[] args) {
		Settings.perspective = true;
		Settings.targetFPS = 60;
		
		
		Camera3D cam = new Camera3D(new Coordinate3D(Settings.width/2, Settings.height/2,0));
		drawer = new Object_draw(cam);

		drawer.add(new Object_border_tether(drawer));
		FPS_display fps = new FPS_display(drawer, Settings.width * 0.01 + 35, Settings.height*0.05);
		drawer.add(fps);
		cam.add(fps);
		
		FCPS_display fcps = new FCPS_display(drawer, Settings.width * 0.01 + 45, Settings.height*0.05 + 15);		
		drawer.add(fcps);
		cam.add(fcps);
		
		Cylinder square1 = new Cylinder(drawer, Settings.width/2, Settings.height/2,0,100,100,100,10);
		square1.setIsFilled(true);
		
		square1.setTexture("./src/LegendOfJava/assets/strawberry.jpg");
		drawer.add(square1);	
		cam.add(square1);
				
		drawer.start();
		

	//	cam.setCameraPanVelocity(new Vector2D(100,5));
		cam.setCameraAngularVelocity(new Vector3D(1,2,1));
	
	//	square1.setAngularVelocity(new Vector3D(-1,-0,-1));
	
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
