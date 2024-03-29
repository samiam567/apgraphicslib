package apgraphicslib;

import java.awt.Color;

public abstract class Settings {
	public static final String version = "2.0.11.2";
	
	public static int width = (int) (1200);
	public static int height = (int) (800);
	public static long depth =  (long) (1000);


	public static boolean perspective = true;
	public static double distanceFromScreenMeters = 0.1; //0.3025; //the distance in meters the viewer is away from the screen
	public static double distanceFromScreen = distanceFromScreenMeters / 0.000115; //should not be changed
	
	public static double timeSpeed = 1; //speed up or slow down everything
			
	public static Color frameColor = Color.gray;
	
	public static double targetFPS = 30; //a number 1D/60 will make the frameRate around 60 fps (first number MUST be a decimal) unless the engine isn't fast enough to keep up
	public static boolean autoResizeFrame = true;

	public static boolean advancedRotation = true; //rotation ABOUT Vectors rather than an x, y, and z rotation each with magnatude of the Vector's components

	public static boolean JOptionPaneErrorMessages = true;
	
	public static void setDistanceFromScreenMeters(double distance) {
		distanceFromScreenMeters = distance;
		distanceFromScreen = distanceFromScreenMeters / 0.000115; 
	}
}
