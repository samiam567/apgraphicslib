package LegendOfJava2;

import java.awt.Graphics;
import java.util.ArrayList;

import LegendOfJava.PlayerHead;
import LegendOfJava.PlayerTorso;
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
import apgraphicslib.Vector3D;
import shapes.Cylinder;
import shapes.Egg;

public class Character extends Physics_3DDrawMovable {
	public static int platePointSize = 5;
	private LegendOfJava2 runner;
	
	public static double headDiameter;
	private static double Upper_arm_length = Settings.height * 0.1;
	private static double height;
	public double jumpSpeed = -400;
	
	Character_Head head;
	private Upper_arm left_arm, right_arm;
	
	private ArrayList<Physics_3DTexturedPolygon> bodyParts = new ArrayList<Physics_3DTexturedPolygon>();

	
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
	
	/**
	 * {@summary overriden to control all the body parts}
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
	}
	
	


	@Override
	public void paint(Graphics page) {}
	
	
	public class Character_Head extends Egg implements Tangible{
		
		private Character parent;
		
		public Character_Head(Character parent, double x, double y, double z) {
			super(parent.getDrawer(), x, y, z, headDiameter, headDiameter, headDiameter, platePointSize);
			parent.bodyParts.add(this);
			this.parent = parent;
			setName("Character head");
			setTexture("./src/LegendOfJava2/assets/pointyHead.jpg");
			rotatePoints(new Vector3D(Math.PI/2,0,Math.PI));
			rotatePoints(new Vector3D(0,0,-Math.PI/2));
			
			parent.runner.camera.add(this);
			parent.runner.drawer.add(this);
		
			
		}
		
		@Override
		public void Update(double frames) {
			//link this with the head
			
			
			super.Update(frames);
			speed = parent.speed;
			
			
			
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
		
		
		public Coordinate3D checkForCollision(Tangible object) {
			//just worry about other obs hitting this as this method would take too long to be worth it. 
			//Note that this means characters cannot hit each other
			return null;
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
			setTexture("./src/LegendOfJava2/assets/pointyHead.jpg");
			
			Coordinate3D cPoint;
			
			for(PolyPoint cP : getPlatePoints()) {
				cPoint = (Coordinate3D) cP;
				cPoint.setPos(cPoint.getX(), cPoint.getY() + getYSize()*2);
			}
			
			
			
			parent.runner.drawer.add(this);
			parent.runner.camera.add(this);
		}
		
		@Override
		public void Update(double frames) {
			//link this with the head
			
			super.Update(frames);
			
			speed = head.getSpeed();
			angularVelocity = head.getAngularVelocity();
		
			
			getCoordinates().setY(head.getY() + headDiameter/2);
		}
		
		//make a cylinder whose center point is at the far end
		@Override 
		protected double[] equation(double theta, double phi) {
			double x = getXSize() * Math.sin(theta);
			double y = -getYSize() + getYSize() * phi;
			double z = getZSize() * Math.cos(theta);
				
			return new double[] {x,y,z};
		}
		
	}


}
