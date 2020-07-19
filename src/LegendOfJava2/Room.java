package LegendOfJava2;

import java.awt.Graphics;

import apgraphicslib.CollisionEvent;
import apgraphicslib.Physics_3DDrawMovable;
import apgraphicslib.Tangible;
import apgraphicslib.Vector3D;
import shapes.Rectangle3D_Textured;

public class Room extends Physics_3DDrawMovable {
	
	private Room_floor floor;
	private double roomHeight;
	private double roomWidth;
	private LegendOfJava2 runner;
	
	public Room(LegendOfJava2 runner, double x, double y, double z) {
		super(runner.drawer, x, y, z);
		this.runner = runner;
		roomHeight = runner.drawer.getFrameHeight();
		roomWidth = runner.drawer.getFrameWidth();
		floor = new Room_floor(this);
		
		
	}

	@Override
	public void paint(Graphics page) {}
	
	
	public class Room_floor extends Rectangle3D_Textured implements Tangible {

		public Room_floor(Room parent) {
			super(parent.getDrawer(), parent.getX(), parent.getY() + roomHeight/2, parent.getZ(), roomWidth, roomHeight,5);
			setTexture2D("./src/LegendOfJava2/assets/texture.jpg");
			rotatePoints(new Vector3D(Math.PI/2,0,0));
			
			runner.drawer.add(this);
			runner.camera.add(this);
			
			setCollisionCheckingAccuracy(5);
		}
		
		
		public void collision(CollisionEvent e) {
			super.collision(e);
			
			if (e.objectHit.equals(runner.Ryan.head)) {
				((Vector3D) runner.Ryan.getAcceleration()).setR(0);
				runner.Ryan.getSpeed().setR(0);
				runner.Ryan.setPos(runner.Ryan.getX(), runner.Ryan.getY()-1 ,runner.Ryan.getZ());
//				runner.camera.getCameraPosition().setY(runner.camera.getY()-1); //this should work but it doesn't
			}
		}
	}

}
