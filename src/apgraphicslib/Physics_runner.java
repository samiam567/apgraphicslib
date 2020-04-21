package apgraphicslib;

public class Physics_runner {

	private static Object_draw drawer = new Object_draw();

	
	@SuppressWarnings("unused")
	private static class Egg extends Physics_3DTexturedEquationedPolygon {
		public Egg(Object_draw drawer, double x, double y, double z,double size, double ppSize) {
			super(drawer, x, y, z,size, ppSize);
		}

		@Override 
		protected double[] equation(double theta, double phi) {
			boolean egg = false;
			
			if (egg) {
				double x = getXSize() * Math.cos(theta) * Math.sin(phi);
				double y = getYSize() * Math.sin(theta) * Math.sin(phi);
				double z = getZSize() * Math.cos(phi);
				
				return new double[] {x,y,z};		
			}else {

				double x = getXSize() * Math.sin(theta);
				double y = getYSize() * Math.cos(theta);
				double z = -getZSize() + getZSize() * phi;
				
				return new double[] {x,y,z};
			}
		}
	}
	
	public static void main(String[] args) {
		

		drawer.add(new Object_border_tether(drawer));
		drawer.add(new FPS_display(drawer, Settings.width * 0.01 + 35, Settings.height*0.05));
		drawer.add(new FCPS_display(drawer, Settings.width * 0.01 + 45, Settings.height*0.05 + 15));
		
		
		Egg square2 = new Egg(drawer,Settings.width/2,Settings.height/2,0,50,2);
		Physics_3DTexturedPolygon square1 = new Physics_3DTexturedPolygon(drawer,Settings.width/2,Settings.height/2,0,2);
		square1.setName("square1");
		
	
		

		double xSize = 330;
		double ySize = 90;
		square1.setSize(xSize,ySize);
		square1.addPoint(-xSize/2,-ySize/2);
		square1.addPoint(xSize/2,-ySize/2);
		square1.addPoint(xSize/2,ySize/2);
		square1.addPoint(-xSize/2,ySize/2);
	
		square1.setTexture("src/LegendOfJava/assets/sword.png");
		
		square1.rotatePoints(new Vector3D(0,0,Math.PI/2));
		
		
	
		square2.setTexture("src/LegendOfJava/assets/sword.png");
		
		square2.rotatePoints(new Vector3D(0,0,Math.PI/2));
		
		square2.setAngularVelocity(new Vector3D(0.2,0.2,0.2));
		
		

		//drawer.add(square1);
		drawer.add(square2);
		
		Coordinate3D rotPoint = new Coordinate3D(400,500,0);
		Vector rotVec = new Vector3D(0,0,2*Math.PI);
		
		
		square1.setPointOfRotation(rotPoint,true);
		square1.setOrbitalAngularVelocity(rotVec);
		
		drawer.start();
		boolean bla = true;
	
		
		
		//wait for close
		while (true) {//drawer.getFrame().isActive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		drawer.stop();
		System.exit(1);
	}
}
