package LegendOfJava2;

import java.awt.Graphics;
import java.util.ArrayList;

import LegendOfJava.PlayerHead;
import LegendOfJava.PlayerTorso;
import apgraphicslib.AffineRotation3D;
import apgraphicslib.Camera;
import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_3DDrawMovable;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;
import apgraphicslib.Physics_3DTexturedPolygon;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Updatable;
import apgraphicslib.Vector2D;
import apgraphicslib.Vector3D;
import shapes.Cylinder;
import shapes.Egg;

public class Character extends Physics_3DDrawMovable {
	public static int platePointSize = 10;
	public static int movementSpeed = 1000;
	
	private LegendOfJava2 runner;
	
	public static double headDiameter;
	private static double Upper_arm_length = Settings.height * 0.1;
	private static double height;
	public double jumpSpeed = -400;
	
	private AffineRotation3D piovertwoinY = new AffineRotation3D(new Vector3D(0,Math.PI/2,0));
	private AffineRotation3D negativepiovertwoinY = new AffineRotation3D(new Vector3D(0,-Math.PI/2,0));
	
	public int movementDirection = 0; //0 = none, 1 = w, 2 = s, 3 = a, 4 = d

	private Vector3D directionFacing = new Vector3D(0,0,1);
	
	public Character_Head head;
	private Upper_arm left_arm, right_arm;
	
	private ArrayList<Physics_3DTexturedPolygon> bodyParts = new ArrayList<Physics_3DTexturedPolygon>();
	public boolean isOnFloor = false;
	public boolean hitFloorLastFrame = false;
	
	

	
	public Character(LegendOfJava2 runner, double x, double y, double z) {
		super(runner.drawer, x, y, z);
		this.runner = runner;
		
		runner.drawer.add(this);
		
		double frameDiagonal = Math.sqrt(Math.pow(runner.drawer.getFrameWidth(),2) + Math.pow(runner.drawer.getFrameHeight(), 2));
		headDiameter = frameDiagonal/20;
		
		
		//must be updated as new bodyparts are added
		height = headDiameter/2 + Upper_arm_length;
		
		
		head = new Character_Head(this,x,y,z);
		left_arm = new Upper_arm(this,head,-1);
		right_arm = new Upper_arm(this,head,1);
		
		
	}
	
	public Vector3D getRotation() {
		return (Vector3D) head.getRotation();
	}
	
	public void setRotation(Vector3D rotation) {
		for (Physics_3DTexturedPolygon cBodyPart : bodyParts) {
			cBodyPart.setOrbitalRotation(rotation);
		}
	}
	
	public void setAngularVelocity(Vector3D angV) {
		for (Physics_3DTexturedPolygon cBodyPart : bodyParts) {
			if (Character_Head.class.isAssignableFrom(cBodyPart.getClass())) {
				cBodyPart.setAngularVelocity(angV);
			}else {
				cBodyPart.setOrbitalAngularVelocity(angV);
			}
		}
	}
	
	public void setAngularVelocity(double i, double j, double k) {
		for (Physics_3DTexturedPolygon cBodyPart : bodyParts) {
			if (Character_Head.class.isAssignableFrom(cBodyPart.getClass())) {
				((Vector3D) cBodyPart.getAngularVelocity()).setIJK(i,j,k);
			}else {
				((Vector3D) cBodyPart.getOrbitalAngularVelocity()).setIJK(i,j,k);
			}
		}
	}
	
	public void addAngularVelocity(Vector3D angV) {
		for (Physics_3DTexturedPolygon cBodyPart : bodyParts) {
			cBodyPart.getAngularVelocity().add(angV);
		}
	}
	
	/**
	 * {@summary overridden to control all the body parts}
	 */
	@Override
	public void setPos(double x, double y, double z) {
		double xChange = x-getX();
		double yChange = y-getY();
		double zChange = z-getZ();

		for (Physics_3DTexturedPolygon cP : bodyParts) {
			cP.setPos(xChange + cP.getX(), yChange + cP.getY(), zChange + cP.getZ());
		}
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		
		
		if (movementDirection != 0) { //we are moving
			Vector3D speedVec = new Vector3D(runner.camera.getDirectionFacing().getI(),0,runner.camera.getDirectionFacing().getK());			
			speedVec.multiply(1/runner.camera.getDirectionFacing().getR());
			speedVec.multiply(movementSpeed);
			
		
			
			if (movementDirection == 1) { //forwards
				//speedVec is already pointing in the right direction
				
			}else if (movementDirection == 2) { //backwards
				speedVec.multiply(-1);		
			}else if (movementDirection == 3) { //left
				speedVec.rotate(piovertwoinY);				
			}else if (movementDirection == 4) { //right
				speedVec.rotate(negativepiovertwoinY);
			}
			
			//Set our speed
			getSpeed().setI(speedVec.getI());
			((Vector3D) getSpeed()).setK(speedVec.getK());
			
			
			//turn towards the direction we are walking
			double angleDiff = Math.atan2(speedVec.getK(), speedVec.getI()) - Math.atan2(directionFacing.getK(), directionFacing.getI());
			
			if (Math.abs(angleDiff) <= Math.PI) {
				setAngularVelocity(0,10*angleDiff,0);
			}else {
				setAngularVelocity(0,-10*angleDiff,0);
			}
		
		}else {
			setAngularVelocity(0,0,0);
		}
		
		runner.camera.getCameraPosition().add(getSpeed().tempStatMultiply(frames));
	}
	
	


	@Override
	public void paint(Graphics page) {}
	
	
	public class Character_Head extends Egg implements Tangible{
		
		private Character parent;
		private int loopsWithoutCollision = 0;
		
		public Character_Head(Character parent, double x, double y, double z) {
			super(parent.getDrawer(), x, y, z, headDiameter, headDiameter, headDiameter, platePointSize);
			parent.bodyParts.add(this);
			this.parent = parent;
			setName("Character head");
			setTexture("./src/LegendOfJava2/assets/pointyHead.jpg");
			
			rotatePoints(new Vector3D(Math.PI/4,Math.PI,-Math.PI/2));
			
			parent.runner.camera.add(this);
			parent.runner.drawer.add(this);
			
			setPointOfRotation(getCoordinates(),true);
			
		}
		
		@Override
		public void Update(double frames) {
			super.Update(frames);
			directionFacing.rotate(head.getAngularVelocity().statMultiply(frames));
			
			speed = parent.speed;
			
				
			if (hitFloorLastFrame) {
				loopsWithoutCollision = 0;
			}else {
				loopsWithoutCollision++;
			}
			
			if (loopsWithoutCollision < 4) {
				isOnFloor = true;
			}else {
				isOnFloor = false;
			}
			
			if (! isOnFloor) {
				parent.getAcceleration().setJ(runner.gravity);
			}
			
			
			hitFloorLastFrame = false; //if we run into the floor this will be true by next update cycle

		}
		
	
		
		public boolean checkForCollision(Coordinate2D point, Tangible ob, double radius) {
			
			//getting the three-dimensional coordinates of point
			Coordinate3D point3D;
			try {
				//try to make the point a 3D point
				point3D = (Coordinate3D) point;
			}catch(ClassCastException c) { //if the point was 2D, just set it at zPos 0 and carry on
				point3D = new Coordinate3D(point.getX(),point.getY(),0);
			}
			
			
			
			//getting the Three-dimensional equivalent position of the object the point is in
			double obX = ob.getX();
			double obY = ob.getY();
			double obZ;
			try {
				obZ = ((Three_dimensional) ob).getZ();
			}catch(ClassCastException c) {
				obZ = 0;
			}
			
			
			//do a cylindrical boundary box covering the character
			if ((point.getY() + obY >= getY() - headDiameter/2 - radius/2) && (point.getY() + obY <= getY() + height + radius/2) ) {
				return (Physics_engine_toolbox.distance2D(getX(), getZ(), obX + point3D.getX(), obZ + point3D.getZ()) < headDiameter + radius);
			}
			
			return false;
		}
		
		public void collision(CollisionEvent e) {
			//do nothing
		}
		
		public Coordinate3D checkForCollision(Tangible object) {
			//just worry about other obs hitting this as this method would take too long to be worth it. 
			//Note that this means characters cannot hit each other
			return null;
		}	
		
		@Override
		public double getPaintOrderValue(Camera cam) { 	
			return super.getPaintOrderValue(cam) - 10000000;
		}
	}
	
	private class Upper_arm extends Physics_3DTexturedEquationedPolygon {
		
		private Character parent;
		
		private int side; //-1=left or 1=right
		
		public Upper_arm(Character parent, Character_Head head, int side) {
			super(parent.getDrawer(), head.getX() + headDiameter*0.6*side , head.getY() + headDiameter/2, head.getZ(), 10, platePointSize);
			this.parent = parent;
			parent.bodyParts.add(this);
			this.side = side;
			
			setName("Upper arm");
			setTexture("./src/LegendOfJava2/assets/ShieldAndSword.jpg");
			
			Coordinate3D cPoint;
			
			for(PolyPoint cP : getPlatePoints()) {
				cPoint = (Coordinate3D) cP;
				cPoint.setPos(cPoint.getX(), cPoint.getY() + getYSize()*2);
			}
			
			setPointOfRotation(parent.head.getCoordinates(),true);
			
			parent.runner.drawer.add(this);
			parent.runner.camera.add(this);
		}
		
		@Override
		public void Update(double frames) {
			//link this with the head
			
			speed = head.getSpeed();
			
			super.Update(frames);
			
			
		
			
			getCoordinates().setY(head.getY() + headDiameter/2);
		}
		
		
		@Override
		public double getPaintOrderValue(Camera cam) { 	
			return super.getPaintOrderValue(cam) - 10000000;
		}
		
		@Override 
		protected double[] equation(double theta, double phi) {
			double x = getXSize() * Math.sin(theta);
			double y = -getYSize() + getYSize() * phi;
			double z = getZSize() * Math.cos(theta);
				
			return new double[] {x,y,z};
		}
		
	}


}
