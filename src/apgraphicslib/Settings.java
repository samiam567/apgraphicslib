package apgraphicslib;

import java.awt.Color;

public abstract class Settings {
	public static final String version = "2.0.8.1";
	
	public static int width = (int) (1000);
	public static int height = (int) (800);
	public static long depth =  (long) (1000);


	public static boolean perspective = true;
	public static double distanceFromScreenMeters = 0.1; //0.3025; //the distance in meters the viewer is away from the screen
	public static final double distanceFromScreen = distanceFromScreenMeters / 0.000115; //should not be changed
	
			
	public static Color frameColor = Color.gray;
	
	public static double frameTime = 1.0D/30; //a number 1D/60 will make the frameRate around 60 fps (first number MUST be a decimal) unless the engine isn't fast enough to keep up
	public static boolean autoResizeFrame = true;
}
