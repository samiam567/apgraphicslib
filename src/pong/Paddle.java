package pong;

import java.awt.Color;
import java.awt.Graphics;

import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Resizable;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Vector2D;
import apgraphicslib.Vector3D;
import shapes.Rectangle3D;
import shapes.Rectangle3D_Textured;


public class Paddle extends Rectangle3D_Textured implements Resizable {
	public String side;
	public static double paddleHomingSpeedMulti = 0.35;
	public int multi;
	public static double zSize = 50;
	
	public static int ppointSize = 2;
	
	public Paddle(Object_draw drawer, String side1) {
		super(drawer,Settings.width/2,Settings.height/2, Settings.depth/2, Settings.width/5,Settings.height/5,ppointSize);
		

		side = side1;
		

		setRotation(0.02,0.02,0);
		
		setTexture("./src/pong/assets/potTexture.jpg");
		
		switch (side) {
			case("near"):
				multi = -1;
				setPos(Settings.width/2,Settings.height/2,100);
				setSize(Settings.width/5,Settings.height/5,Settings.width/5);
				setColor(Color.green);
				setRotation(0.02,0.02,0);
			break;
			
			case("far"):
				multi = 1;
				setPos(Settings.width/2,Settings.height/2,Settings.depth-200);
				setSize(Ball.getRectSizeWidth(getZ())/4,Ball.getRectSizeHeight(getZ())/3,Settings.width/10);
				setColor(Color.yellow);
				
			break;
		}
		
		setSize(Settings.width/5,Settings.height/5,Settings.width/5);
		
//		setMass(10);
	}
	
	

	public void paint2(Graphics page) {
		if (side.equals("near")) {
			page.drawRect((int) (getX() - xSize/2),(int)  (getY()- ySize/2), (int) xSize,(int) ySize);
		}else {
			page.fillRect((int) (getX() - xSize/2),(int)  (getY()- ySize/2), (int) xSize,(int) ySize);
		}
	}

	
	
	public void Update(double frames) {
		
		super.Update(frames);
		
		checkForBallCollide();
		
		Vector3D speed = (Vector3D) getSpeed();
				
		if (Pong_runner.cheatMode) {
			if (side.equals("near")) {
				if (Pong_runner.ball.getZ() < (300)) {
					setPos(Pong_runner.ball.getX(),Pong_runner.ball.getY(),100);
				}
			}
		}
		
		
		
		
		//homing in on ball
		if (side.equals("far") && Pong_runner.p2AI) {
			if (multi * ((Vector3D) Pong_runner.ball.getSpeed()).getK() > 0 && Pong_runner.ball.getZ() <= getZ() ) {
				
				
				double paddleHomingSpeed =paddleHomingSpeedMulti * Pong_runner.AI_difficulty * Pong_runner.ball.ballZSpeed;
				
				speed = (Vector3D) getSpeed();

				//x
				if (Pong_runner.ball.getX() > getX()) {
					setSpeed(new Vector3D(paddleHomingSpeed,speed.getJ(),0));
				}else if (Pong_runner.ball.getX() < getX()) {
					setSpeed(new Vector3D(-paddleHomingSpeed,speed.getJ(),0));
				}else {
					setSpeed(new Vector3D(0,speed.getJ(),0));
				}
				
				speed = (Vector3D) getSpeed();
				
				
				//y
				if (Pong_runner.ball.getY() > getY()) {
					setSpeed(new Vector3D(speed.getI(),paddleHomingSpeed,0));
				}else if (Pong_runner.ball.getY() < getY()) {
					setSpeed(new Vector3D(speed.getI(),-paddleHomingSpeed,0));
				}else {
					setSpeed(new Vector3D(speed.getI(),0,speed.getJ()));
				}
				
			}else {
				setSpeed(new Vector3D(0,0,0));
			}

		}
		
		
//		if (side.equals("far")) {
//			if (Math.sqrt(Math.pow(0.000000000001 + getX()-Settings.width/2,2)) + getXSize() >= Ball.getRectSizeWidth(getZ())/1.5) {
//				if (getX()-Settings.width/2 > 0) {
//					setSpeed(new Vector3D(-Math.sqrt(0.00000001+Math.pow(((Vector3D) getSpeed()).getI(),2)),((Vector3D) getSpeed()).getJ(),((Vector3D) getSpeed()).getK()));
//				}else {
//					setSpeed(new Vector3D(Math.sqrt(0.00000001+Math.pow(((Vector3D) getSpeed()).getI(),2)),((Vector3D) getSpeed()).getJ(),((Vector3D) getSpeed()).getK()));
//				}
//			}
//			
//			if (Math.sqrt(Math.pow(0.000000000001 + getY()-Settings.height/2,2)) + getYSize() >= Ball.getRectSizeHeight(getZ())/1.5) {
//				if (getY()-Settings.height/2 > 0) {
//					setSpeed(new Vector3D(((Vector3D) getSpeed()).getI(),-Math.sqrt(0.00000001+Math.pow(((Vector3D) getSpeed()).getJ(),2)),((Vector3D) getSpeed()).getK()));
//				}else {
//					setSpeed(new Vector3D(((Vector3D) getSpeed()).getI(),Math.sqrt(0.00000001+Math.pow(((Vector3D) getSpeed()).getJ(),2)),((Vector3D) getSpeed()).getK()));
//				}
//			}
//		}
		
	}
	
	public void resize() {
		
//		setSize(Settings.width/10,Settings.height/10,1);
		
		switch (side) {
			case("near"):
				setPos(Settings.width/2,Settings.height/1.5);
				setSize(Settings.width/5,Settings.height/5);
				setColor(Color.green);
				setRotation(0.02,0.02);
			break;
			
			case("far"):
				setPos(Settings.width/2,Settings.height/3,Settings.depth-200);
				setSize(Settings.width/20,Settings.height/20,Settings.width/10);
				setColor(Color.yellow);
				
			break;
		}	
	}



	
	private void checkForBallCollide() {
		if (Math.abs(getX() - Pong_runner.ball.getX()) < xSize && Math.abs(getY() - Pong_runner.ball.getY()) < ySize && Math.abs(getZ() - Pong_runner.ball.getZ()) < zSize) {
			Pong_runner.ball.paddleCollision(this);
		}
	}



	

	
	
	
}
