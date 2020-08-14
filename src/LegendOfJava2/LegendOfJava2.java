package LegendOfJava2;

import java.awt.Color;

import apgraphicslib.APLabel;
import apgraphicslib.Camera3D;
import apgraphicslib.CameraMovable;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Drawable;
import apgraphicslib.FCPS_display;
import apgraphicslib.FPS_display;
import apgraphicslib.Object_draw;
import apgraphicslib.Polygon_drawer_tools;
import apgraphicslib.Settings;

public class LegendOfJava2 {
	Object_draw drawer;
	LOJ2Camera camera;
	Character Ryan;
	
	Controller controller;
	
	public double gravity = 900;
	
	public static void main(String[] args) {
		new LegendOfJava2();
	}
	
	public LegendOfJava2() {
		Settings.advancedRotation = false;
		Settings.perspective = true;
		
		camera = new LOJ2Camera(this, new Coordinate3D(Settings.width/2, Settings.height/2,0));
		
		drawer = new Object_draw(camera);
		
		
		FPS_display fpsView = new FPS_display(drawer, Settings.width * 0.01 + 28, Settings.height*0.05);
		fpsView.setColor(Color.white);
		drawer.add(fpsView);
		camera.addPaintOnly(fpsView);
		FCPS_display fcpsView = new FCPS_display(drawer, Settings.width * 0.01 + 43, Settings.height*0.05 + 15);
		fcpsView.setColor(Color.white);
		drawer.add(fcpsView);
		camera.addPaintOnly(fcpsView);
		
		
		Ryan = new Character(this, drawer.getFrameWidth()*0.5, drawer.getFrameHeight()*0.5, 0);
	
		Ryan.getAcceleration().setIJ(0,gravity);	

		APLabel LOJTitle = new APLabel(drawer, drawer.getFrameWidth()*0.5, 25);
		LOJTitle.setName("LOJ Title text");
		LOJTitle.setMessage("Legend of Java 2 : idk yet");
		drawer.add(LOJTitle);
		camera.addPaintOnly(LOJTitle);
		
		
		controller = new Controller(this);
		
		
		@SuppressWarnings("unused")
		Room room1 = new Room_doubleDecker(this, drawer.getFrameWidth()/2, drawer.getFrameHeight()/2,0);
		
		
		drawer.start();
	}
}
