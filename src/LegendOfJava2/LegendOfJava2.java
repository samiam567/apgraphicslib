package LegendOfJava2;

import apgraphicslib.APLabel;
import apgraphicslib.Camera3D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class LegendOfJava2 {
	Object_draw drawer;
	Camera3D camera;
	Character Ryan;
	
	Controller controller;
	
	public static void main(String[] args) {
		new LegendOfJava2();
	}
	
	public LegendOfJava2() {
		camera = new Camera3D(new Coordinate3D(Settings.width/2, Settings.height/2,0));
		
		drawer = new Object_draw(camera);
		drawer.start();
		Ryan = new Character(this, drawer.getFrameWidth()*0.5, drawer.getFrameHeight()*0.5, 0);

		APLabel LOJTitle = new APLabel(drawer, drawer.getFrameWidth()*0.5, 25);
		LOJTitle.setName("LOJ Title text");
		LOJTitle.setMessage("Legend of Java 2 : idk yet");
		drawer.add(LOJTitle);
		camera.addPaintOnly(LOJTitle);
		
		
		controller = new Controller(this);
		
		
		
	}
}
