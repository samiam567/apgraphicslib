package LegendOfJava;

import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DDrawMovable;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Settings;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Updatable;
import apgraphicslib.Vector3D;

public class Room extends Physics_3DDrawMovable implements Updatable, Three_dimensional {
	
	private Wall leftWall, rightWall, backWall, ceilingWall, floorWall, frontWall;
	
	private LinkedList<Wall> walls = new LinkedList<Wall>();
	private LinkedList<RoomObjectable> roomObs = new LinkedList<RoomObjectable>();
	
	private Vector3D rotation = new Vector3D(0,0,0);
	public Character player;
	
	

	private double roomWidth;
	private double roomDepth;
	private double roomHeight;
	
	private int roomPPSize = 10;
	
	private boolean loaded = false, wallsSet = false;
	
	public static Vector3D roomAngV = new Vector3D(0,0,0), roomSpeed = new Vector3D(0,0,0);
	
	public Room nextRoom = null;
	
	public Room(Object_draw drawer, Character player, double xSize, double ySize, double zSize) {
		super(drawer, Settings.width/2, Settings.height/2, Settings.depth/2 + zSize/3 - Settings.distanceFromScreen);
		roomWidth = xSize;
		roomHeight = ySize;
		roomDepth = zSize;
		setPos(Settings.width/2, Settings.height - roomHeight/2, Settings.depth/2);
		this.player = player;

		loaded = false;
		
		
	}
		
	private void setWalls() {
		
		walls.clear();
		
		leftWall = new Wall(this,getX() - roomWidth/2,getY(),getZ(),roomDepth,roomHeight,new Vector3D(0,Math.PI/2,0),roomPPSize);
		leftWall.setName("leftWall");
		
		rightWall = new Wall(this,getX() + roomWidth/2,getY(),getZ(),roomDepth,roomHeight,new Vector3D(0,-Math.PI/2,0),roomPPSize);
		rightWall.setName("rightWall");
		
		backWall = new Wall(this,getX(),getY(),getZ() - roomDepth/2,roomWidth,roomHeight, new Vector3D(0,0,0),roomPPSize);
		backWall.setName("backWall");
		
		frontWall = new Wall(this,getX(),getY(),getZ() + roomDepth/2,roomWidth,roomHeight, new Vector3D(0,0,0),roomPPSize);
		frontWall.setName("frontWall");
		
		floorWall = new Wall(this, getX(),getY() + roomHeight/2,getZ(),roomWidth,roomDepth, new Vector3D(Math.PI/2,0,0),roomPPSize);
		floorWall.setName("floorWall");
		
		ceilingWall = new Wall(this, getX(), getY() - roomHeight/2,getZ(),roomWidth,roomDepth, new Vector3D(Math.PI/2,0,0),roomPPSize);
		ceilingWall.setName("ceilingWall");
		
		
		walls.add(backWall);
		walls.add(leftWall);
		walls.add(rightWall);
		walls.add(floorWall);
		walls.add(frontWall);
		walls.add(ceilingWall);
		
		
		
		wallsSet = true;
	}
	
	public void addRoomOb(RoomObjectable newRoomOb) {
		roomObs.add(newRoomOb);
	}
	
	public void setRoomSize(double width, double height) {
		roomWidth = width;
		roomHeight = height;
		wallsSet = false;
		loaded = false;
	
	}
	
	public void load() {
		
		for (Wall cWall : walls) {
			cWall.loadTexture();
		}
	
		loaded = true;
	}
	
	/**
	 * {@summary sets all of the walls to the passed texture}
	 * @param newTexture
	 */
	public void setWallTexture(String newTexture) {
		for (Wall cWall : walls) {
			cWall.setTexture(newTexture);
		}
		loaded = false;
	}
	
	
	public void setWallTexture() {
		backWall.setTexture("src/LegendOfJava/assets/Dungeon side wall 7by3.png");
		frontWall.setTexture("src/LegendOfJava/assets/Dungeon wall 7by3.png");
		leftWall.setTexture("src/LegendOfJava/assets/Dungeon side wall 7by3.png");
		rightWall.setTexture("src/LegendOfJava/assets/Dungeon side wall 7by3.png");
		floorWall.setTexture("src/LegendOfJava/assets/Dungeon side wall 7by3.png");
		ceilingWall.setTexture("src/LegendOfJava/assets/Dungeon side wall 7by3.png");
		loaded = false;
	}
	
	public void run() {
		setPos(Settings.width/2,Settings.height/2,Settings.depth/2);
		if (wallsSet) {
			for (Wall cWall : walls) {
				if (cWall.wallTexture.equals("noTexture")) setWallTexture();
			}
		}else {
			setWalls();
			setWallTexture();
		}
		
		remove();
		if (! loaded) load();
		add();
		roomAngV.setIJK(0,0.00000000000000001,0);
	}
	private void add() {
		getDrawer().add(this);
		for (Wall cWall : walls) {
			cWall.setPointOfRotation(player.getCoordinates(), true);
			getDrawer().add(cWall);
		}
		for (RoomObjectable rOb : roomObs) {
			rOb.setPointOfRotation(player.getCoordinates(), true);
			rOb.add(this);
		}
	
	}
	
	public void remove() {
		getDrawer().remove(this);
		for (Wall cWall : walls) {
			getDrawer().remove(cWall);
		}
		for (RoomObjectable rOb : roomObs) {
			getDrawer().remove(rOb);
		}
	}
	
	public LinkedList<Wall> getWalls() {
		return walls;
	}
	
	@Override
	public void Update(double frames) {
		
		
		/*
		wallAngV = ((Vector3D) backWall.getRotation()).statAdd(player.torso.getRotation().statMultiply(-1));
		wallAngV.multiply(frames * 0.00000000001);
		wallAngV.multiply( 0.1);
		*/
		
		
		//rotate walls around mainCharacter
		rotation.add(roomAngV);
		for (Wall cOb : walls) {
			cOb.setOrbitalAngularVelocity(roomAngV);
			cOb.setSpeed(roomSpeed);
		}
		
		//rotate room objects around mailCharacter
		for (RoomObjectable rOb : roomObs) {
			
			rOb.setOrbitalAngularVelocity(roomAngV);
			rOb.setSpeed(roomSpeed);
		}
		
		
		//check if player is at the door
		if (Physics_engine_toolbox.distance3D(frontWall.getCoordinates(), player.getCoordinates()) < PlayerHead.headZSize + roomHeight/2) {
			if (EnemyCharacter.numEnemys == 0) {
				LegendOfJavaRunner.loadNextRoom();
			}else {
				LegendOfJavaRunner.console.setMessage("you cannot leave the room while there are still enemies!");
			}
		}
		
	}
	
	@Override
	public Coordinate3D getCoordinates() {
		return backWall.getCoordinates();
	}
	
	@Override
	public void paint(Graphics page) {
		//walls are painted throught their own methods
	}

	@Override
	public void setSize(double xSize, double ySize, double zSize) {
		setSize(xSize, ySize);
	}

	@Override
	public double getZSize() {
		return leftWall.getZSize();
	}

	/**
	 * {@summary this method doesn't really work for this class}
	 */
	@Override
	public void setPos(double x, double y, double z) {
		setPos(x,y);
	}

	public double getRoomHeight() {
		return roomHeight;
	}

	public void setPPSize(int roomPPSize) {
		loaded = false;
		
		double roomSize = Math.sqrt(roomWidth*roomWidth+roomHeight*roomHeight);
		
		int newPPSize = (int) Math.round(roomPPSize * roomSize/Math.sqrt(Settings.width*Settings.width+Settings.height*Settings.height));
		
		this.roomPPSize = newPPSize;
		
		wallsSet = false;
	}

	public double getRoomWidth() {
		return roomWidth;
	}

	
	

}
