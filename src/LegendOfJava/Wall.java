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
		super(parentRoom.getDrawer(), x, y, z, ppSize);
		
		this.parentRoom = parentRoom;
		
		addPoint(-xSize/2, -ySize/2, 0);
		addPoint(xSize/2, -ySize/2, 0);
		addPoint(xSize/2, ySize/2, 0);
		addPoint(-xSize/2, ySize/2, 0);
		
		wallRotation = rotation;
		
		setMinZToPaintPoints(0);
		setCollisionCheckingAccuracy(2);
		
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
	public void prePaintUpdate() {
		reCalculateSize();
	}
	
	@Override
	public double getPaintOrderValue() { 	
		return super.getPaintOrderValue() ;
	}
	

	
	
	
	@Override
	public boolean checkForCollision(Coordinate2D point, Tangible ob, Vector3D directionVec, double radius) {
		try {
			//if the object is another wall return false
			@SuppressWarnings("unused")
			Wall w = (Wall) ob;
			return false;
		}catch(ClassCastException c) {
			//otherwise check collision normally
			return super.checkForCollision(point, ob,directionVec, radius);
		}
	}
	
	
	@Override
	public Coordinate3D checkForCollision(Tangible object, Vector3D directionVec) {	
		if ( ! Wall.class.isAssignableFrom(object.getClass())) {
			return super.checkForCollision(object,directionVec);
		}
		return null;
	}
	
	public void collision(CollisionEvent e) {
		super.collision(e);
		
		//if there is a collision with a wall, move the room away from the player
		
		//just go the other way...
		Room.roomSpeed.multiply(-1.001);
		Room.roomAngV.multiply(-1.001);
		
		getDrawer().pause();
		for (Wall cWall : parentRoom.getWalls()) {
			cWall.Update(getDrawer().getFrameStep());
		}
		for (RoomObjectable cOb : parentRoom.roomObs) {
			cOb.Update(getDrawer().getFrameStep());
		}
		Room.roomSpeed.setIJK(0, 0, 0);
		getDrawer().resume();
		
	}
	
	
}
