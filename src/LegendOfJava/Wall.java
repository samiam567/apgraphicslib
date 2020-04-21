package LegendOfJava;

import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_3DTexturedPolygon;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Vector3D;


public class Wall extends Physics_3DTexturedPolygon implements Three_dimensional, Tangible {
	private Room parentRoom;
	String wallTexture = "noTexture";
	private Vector3D wallRotation;
	
	public Wall(Room parentRoom, double x, double y, double z, double xSize, double ySize, Vector3D	rotation, int ppSize) {
		super(parentRoom.getDrawer(), x, y, 0, ppSize);
		this.parentRoom = parentRoom;
		
		addPoint(-xSize/2, -ySize/2, 0);
		addPoint(xSize/2, -ySize/2, 0);
		addPoint(xSize/2, ySize/2, 0);
		addPoint(-xSize/2, ySize/2, 0);
		
		wallRotation = rotation;
		
		setPos(x,y,z);
	}
	
	/**
	 * {@summary will change the staged texture to this value. This will NOT load the texure. You must call the loadTexture() method to see the new texture on the wall}
	 */
	@Override
	public void setTexture(String texture) {
		wallTexture = texture;
	}
	
	/**
	 * {@summary will actually load the texture onto the wall}
	 */
	public void loadTexture() {
	
		setTexture2D(wallTexture);
		rotate(wallRotation);
	}
	
	
	@Override
	public double getPaintOrderValue() { 	
		return super.getPaintOrderValue() + parentRoom.player.getPaintOrderValue() + parentRoom.getRoomWidth();
	}
	
	
	
	@Override
	public boolean checkForCollision(Coordinate2D point, Tangible ob, double radius) {
		try {
			//if the object is another wall return false
			@SuppressWarnings("unused")
			Wall w = (Wall) ob;
			return false;
		}catch(ClassCastException c) {
			//otherwise check collision normally
			return super.checkForCollision(point, ob, radius);
		}
	}
	
	
	@Override
	public Coordinate3D checkForCollision(Tangible object) {	
		try {
			//if the object is another wall return null
			@SuppressWarnings("unused")
			Wall w = (Wall) object;
			return null;
		}catch(ClassCastException c) {
			//otherwise check collision normally
			return super.checkForCollision(object);
		}
	}
	
	public void collision(CollisionEvent e) {
		super.collision(e);
		
		if (getZ() > parentRoom.player.getZ()) {
			Room.roomSpeed.setIJK(0, 0, 2*Room.roomSpeed.getR());
		}else {
			Room.roomSpeed.setIJK(0, 0, -2*Room.roomSpeed.getR());
		}
		Room.roomAngV.multiply(-1);
		getDrawer().pause();
		for (Wall cWall : parentRoom.getWalls()) {
			
			cWall.Update(0.1);
		}
		Room.roomSpeed.setIJK(0, 0, 0);
		getDrawer().resume();
	}
	
	
}
