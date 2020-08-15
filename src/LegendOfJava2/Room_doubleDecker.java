package LegendOfJava2;

import apgraphicslib.Vector3D;

public class Room_doubleDecker extends Room {

	//public Room_floor(Room parent, double xOffset, double yOffset,double zOffset, double xWidth, double yWidth, Vector3D[] rotation) {
	
	public Room_doubleDecker(LegendOfJava2 runner, double x, double y, double z) {
		super(runner, x, y, z);
		roomWidth = runner.drawer.getFrameWidth() * 2.5;
		roomHeight = runner.drawer.getFrameHeight() * 2.5;
		new Room_floor(this,0,roomHeight/2,0,roomWidth,roomHeight,new Vector3D[] {new Vector3D(Math.PI/2,0,0)});
		new Room_floor(this,0,-roomHeight/2,-roomHeight/8,roomWidth,roomHeight,new Vector3D[] {new Vector3D(Math.PI/2,0,0)});
		new Room_floor(this,0,0,roomHeight/2,roomWidth,roomHeight/4, new Vector3D[] {new Vector3D(Math.PI/2,0,0),new Vector3D(0,0,Math.PI/4)});
	} 
	
}
