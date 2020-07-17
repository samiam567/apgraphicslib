package LegendOfJava2;

import java.awt.Graphics;

import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_3DDrawMovable;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;
import apgraphicslib.Settings;
import apgraphicslib.Vector3D;
import shapes.Cylinder;
import shapes.Egg;

public class Character extends Physics_3DDrawMovable {
	public static int platePointSize = 5;
	private LegendOfJava2 runner;
	
	public static double headDiameter;
	private static double Upper_arm_length = Settings.height * 0.1;
	
	private Character_Head head;
	private Upper_arm left_arm, right_arm;

	
	public Character(LegendOfJava2 runner, double x, double y, double z) {
		super(runner.drawer, x, y, z);
		this.runner = runner;
		
		runner.drawer.add(this);
		
		double frameDiagonal = Math.sqrt(Math.pow(runner.drawer.getFrameWidth(),2) + Math.pow(runner.drawer.getFrameHeight(), 2));
		headDiameter = frameDiagonal/20;
		
		head = new Character_Head(this,x,y,z);
		left_arm = new Upper_arm(this,head,-1);
		right_arm = new Upper_arm(this,head,1);
	
	}

	@Override
	public void paint(Graphics page) {}
	
	
	private class Character_Head extends Egg {
		
		private Character parent;
		
		public Character_Head(Character parent, double x, double y, double z) {
			super(parent.getDrawer(), x, y, z, headDiameter, headDiameter, headDiameter, platePointSize);
	
			this.parent = parent;
			setName("Character head");
			setTexture("./src/LegendOfJava2/assets/pointyHead.jpg");
			rotatePoints(new Vector3D(Math.PI/2,0,Math.PI));
			rotatePoints(new Vector3D(0,0,-Math.PI/2));
			
			parent.runner.camera.addPaintOnly(this);
			parent.runner.drawer.add(this);
		}
		
	}
	
	private class Upper_arm extends Physics_3DTexturedEquationedPolygon {
		
		private Character parent;
		
		private int side; //-1=left or 1=right
		
		public Upper_arm(Character parent, Character_Head head, int side) {
			super(parent.getDrawer(), head.getX() + headDiameter*0.6*side , head.getY() + headDiameter/2, head.getZ(), 10, platePointSize);
			this.parent = parent;
			
			this.side = side;
			
			setName("Upper arm");
			setTexture("./src/LegendOfJava2/assets/pointyHead.jpg");
			
			Coordinate3D cPoint;
			
			for(PolyPoint cP : getPlatePoints()) {
				cPoint = (Coordinate3D) cP;
				cPoint.setPos(cPoint.getX(), cPoint.getY() + getYSize()*2);
			}
			
			//link this with the head
			speed = head.getSpeed();
			acceleration = head.getAcceleration();
			angularAcceleration = head.getAngularAcceleration();
			
			parent.runner.drawer.add(this);
			parent.runner.camera.addPaintOnly(this);
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
