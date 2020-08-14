package LegendOfJava2;

import java.awt.Graphics;
import java.util.ArrayList;

import LegendOfJava2.Character.Character_Head;
import apgraphicslib.CollisionEvent;
import apgraphicslib.Physics_3DDrawMovable;
import apgraphicslib.Physics_3DTexturedPolygon;
import apgraphicslib.Tangible;
import apgraphicslib.Vector3D;
import shapes.Rectangle3D_Textured;

public class Room extends Physics_3DDrawMovable {
	
	private Room_floor floor;
	protected double roomHeight;
	protected double roomWidth;
	private LegendOfJava2 runner;
	private ArrayList<Physics_3DTexturedPolygon> roomObjects = new ArrayList<Physics_3DTexturedPolygon>();
	private static double incCharHeightOnCollide = 0.1;
	private static int platePointSize = 15;
	
	
	/**
	 * {@summary room constructor that automatically builds a floor}
	 */
	public Room(LegendOfJava2 runner, double x, double y, double z, double roomWidth, double roomHeight) {
		super(runner.drawer, x, y, z);
		this.runner = runner;
		this.roomHeight = roomHeight;
		this.roomWidth = roomWidth;
		floor = new Room_floor(this);

	}
	
	/**
	 * {@summary generic room constructor that doesn't create any room objects}
	 * {@code roomWidth and roomHeight must be set manually}
	 * @param runner
	 * @param x
	 * @param y
	 * @param z
	 */
	public Room(LegendOfJava2 runner, double x, double y, double z) {
		super(runner.drawer, x, y, z);
		this.runner = runner;
	}

	@Override
	public void paint(Graphics page) {}
	
	
	public class Room_floor extends Rectangle3D_Textured implements Tangible {
		private Room parent;
		
		public Room_floor(Room parent) {
			super(parent.getDrawer(), parent.getX(), parent.getY() + roomHeight/2, parent.getZ(), roomWidth, roomHeight,platePointSize);
			this.parent = parent;
			initiateRoom();
			rotatePoints(new Vector3D(Math.PI/2,0,0));
		}
		
		public Room_floor(Room parent, double xOffset, double yOffset, double zOffSet, double xWidth, double yWidth, Vector3D[] rotation) {
			super(parent.getDrawer(), parent.getX() + xOffset, parent.getY() + yOffset, parent.getZ() + zOffSet, xWidth, yWidth,platePointSize);
			this.parent = parent;
			initiateRoom();
			for (Vector3D vec : rotation) {
				rotatePoints(vec);
			}
		}
		
		private void initiateRoom() {
			parent.roomObjects.add(this);
			setTexture2D("./src/LegendOfJava2/assets/Dungeon side wall 3by3.png");
			
			runner.drawer.add(this);
			runner.camera.add(this);
			
			setCollisionCheckingAccuracy(3);
		}
		
		public void Update(double frames) {
			//room elements don't move so they don't need to be updated
		}
		
		public void collision(CollisionEvent e) {
			//note; hitfloorOnLastFrame is always false at this point in execution. If you want to use it for something you're probably gonna want IsOnFloor
			
			if (e.objectHit.equals(runner.Ryan.head)) {	
				Character r = runner.Ryan;
				
				//if (Math.abs(runner.Ryan.getSpeed().getJ()) < 0.1) {
				/*if (Math.abs(runner.Ryan.getSpeed().getJ()) > runner.gravity) {
					
				}else if ((Math.abs(r.getSpeed().getI()) + Math.abs(((Vector3D) r.getSpeed()).getK())) > 1 ){
					
				}
					incCharHeightOnCollide = 1;
				}
			*/	
				if (r.isOnFloor) {
					incCharHeightOnCollide = (int) (r.getSpeed().getR() * getDrawer().getFrameStep() * 0.1);
				}else {
					incCharHeightOnCollide = (int) (r.getSpeed().getJ() * getDrawer().getFrameStep() * 0.1);
				}
			
				((Vector3D) runner.Ryan.getAcceleration()).setIJK(0,0,0);
				runner.Ryan.getSpeed().setJ(0);
				runner.Ryan.setPos(runner.Ryan.getX(), runner.Ryan.getY()-incCharHeightOnCollide ,runner.Ryan.getZ());
				runner.camera.getCameraPosition().setY(runner.camera.getCameraPosition().getY()-incCharHeightOnCollide);
				runner.Ryan.hitFloorLastFrame = true;
			
			}
		}
	}

}
