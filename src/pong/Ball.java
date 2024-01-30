package pong;

import java.awt.Color;
import java.awt.Graphics;


import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Movable;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DTexturedEquationedPolygon;
import apgraphicslib.Resizable;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import apgraphicslib.Vector3D;
import shapes.Egg;
import shapes.Sphere;



public class Ball extends Sphere implements Resizable {
	
	public static double ballZSpeed = Pong_runner.gameSetSpeed * 500;
	public static int spinMulti = 30;
	
	public Ball(Object_draw drawer) {
		super(drawer,Settings.width/2,Settings.height/2,Settings.depth/2,50,5);
//		hasNormalCollisions = false;
		int direction;
		if (Math.random() < 0) {
			direction = -1;
		}else {
			direction = 1;
		}
		
		setTexture("./src/pong/assets/pointyHead.jpg");
		
		
		setSpeed(new Vector3D(Pong_runner.ballSpeed/2 * (Math.random() - 0.5) ,Pong_runner.ballSpeed/2 *(Math.random() - 0.5),ballZSpeed * direction ));
		

	}


	
	@Override
	public void Update(double frames) {
		super.Update(frames);
//		System.out.println("X: " + getX() + " y: " + getY() + " z: " + getZ());
		double size = 100 * Settings.width/(getZ() + 500);
		setSize(size,size);
		
		Vector3D speed =(Vector3D) getSpeed();
		
		double xSpeed = speed.getI();
		double ySpeed = speed.getJ();
		double zSpeed = speed.getK();
		
		if (Math.sqrt(Math.pow(0.000000000001 + getX()-Settings.width/2,2)) + size >= getRectSizeWidth(getZ())/2) {
			if (getX()-Settings.width/2 > 0) {
				setSpeed(new Vector3D(-Math.sqrt(0.00000001+Math.pow(xSpeed,2)),ySpeed,zSpeed));
			}else {
				setSpeed(new Vector3D(Math.sqrt(0.00000001+Math.pow(xSpeed,2)),ySpeed,zSpeed));
			}
		}
		
		if (Math.sqrt(Math.pow(0.000000000001 + getY()-Settings.height/2,2)) + size >= getRectSizeHeight(getZ())/2) {
			if (getY()-Settings.height/2 > 0) {
				setSpeed(new Vector3D(xSpeed,-Math.sqrt(0.00000001+Math.pow(ySpeed,2)),zSpeed));
			}else {
				setSpeed(new Vector3D(xSpeed,Math.sqrt(0.00000001+Math.pow(ySpeed,2)),zSpeed));
			}
		}
		
		
	
		// border bounce
		
		double newXSpeed = xSpeed, newYSpeed = ySpeed, newZSpeed = zSpeed;
		double newX = getX(), newY = getY(), newZ = getZ();
		
		if (getX() > Settings.width) {
			newXSpeed = -Math.abs(xSpeed);
			newX = Settings.width-1;
		}else if (getX() < 0) {
			newXSpeed = Math.abs(xSpeed);
			newX = 1;
		}
		
		if (getY() > Settings.height) {
			newYSpeed = -Math.abs(ySpeed);
			newY = Settings.height-1;
		}else if (getY() < 0) {
			newYSpeed = Math.abs(ySpeed);
			newY = 1;
		}
		
		if (getZ() > Settings.depth) {
			newZSpeed = -Math.abs(zSpeed);
			newZ = Settings.depth-1;
			
			Pong_runner.nScore.setColor(Color.BLUE);
			Pong_runner.nScore.AddScore(1);
			
			getDrawer().pause();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
			getDrawer().resume();			
			
			Pong_runner.nScore.setColor(Color.GREEN);
			
			reset();
			return;
		}else if (getZ() < 0) {
			Pong_runner.fScore.setColor(Color.BLUE);
			Pong_runner.fScore.AddScore(1);
			getDrawer().pause();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
			getDrawer().resume();
			
			Pong_runner.fScore.setColor(Color.GREEN);
			newZSpeed = Math.abs(zSpeed);
			newZ = 1;
			reset();
			return;
		}
		setSpeed(new Vector3D(newXSpeed, newYSpeed, newZSpeed));
		setPos(newX, newY, newZ);
		
//		System.out.println("X: " + getX() + " y: " + getY() + " z: " + getZ());
		
	}
	

	public void paddleCollision(Paddle cPad) {
	
		setAcceleration(new Vector3D(0,0,0));
		
		Vector3D speed =(Vector3D) getSpeed();
		double xSpeed = speed.getI();
		double ySpeed = speed.getJ();
		double zSpeed = speed.getK();
		

		
		System.out.println("PAD");
		
		
		setSpeed(new Vector3D(xSpeed,ySpeed,-zSpeed));
		
		System.out.println("speed: " + zSpeed);
		
		
		if (getZ() < Settings.depth/2) {
			setPos(getX(), getY(), Paddle.zSize*5);
		}else {
			setPos(getX(), getY(), Settings.depth-Paddle.zSize*5);
		}
		updatePoints();
		
		
	
		//spin
		Vector3D padSpeed = (Vector3D) cPad.getSpeed();
		double xPadSpeed = padSpeed.getI();
		double yPadSpeed = padSpeed.getJ();
		
		setAcceleration(new Vector3D(-spinMulti*(xSpeed + xPadSpeed)/100,spinMulti*(ySpeed - yPadSpeed)/100,0));
		setAngularVelocity(new Vector3D(spinMulti*(ySpeed - yPadSpeed)/5000,spinMulti*(xSpeed + xPadSpeed)/5000,0));
		
		System.out.println((ySpeed - yPadSpeed));
	
	}
	
	public void reset() {
		setPos(Settings.width/2,Settings.height/2,Settings.depth/2);
		
		int direction;
		if (Math.random() < 0.5) {
			direction = -1;
		}else {
			direction = 1;
		}
		
		setSpeed(new Vector3D(Math.round(Pong_runner.ballSpeed)/2 *(Math.random() - 0.5) ,Math.round(Pong_runner.ballSpeed)/2 *(Math.random() - 0.5),ballZSpeed * direction));
	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static int getRectSizeWidth(double z) {
		return (int) (1000*Settings.width/(z + 500));
	}
	
	static int getRectSizeHeight(double z) {
		return (int) (1000*Settings.height/(z + 500));
	}
	public void paint(Graphics page) {
		
		//drawing border boxes
		
		int alpha = 150; // 1/transparency of the shape
		Color barsColor = Color.getHSBColor(Color.red.getRGB(),1f, 150 );
		page.setColor(new Color(barsColor.getRed(),barsColor.getGreen(),barsColor.getBlue(),alpha));
		for (int z = 0; z < Settings.depth; z+= 50) {
			page.drawRect(Settings.width/2-getRectSizeWidth(z)/2,Settings.height/2-getRectSizeHeight(z)/2,getRectSizeWidth(z),getRectSizeHeight(z));
			
		}
		
		
		
		page.setColor(Color.cyan);
	
		//distance bars
		page.fillRect(0, 0, Settings.width/100, (int) (Settings.height - ((Settings.height) * getZ())/Settings.depth));
		page.drawRect(0, 0, Settings.width/100, (int) (Settings.height));
		
		page.fillRect((int) (Settings.width - Settings.width/100 - 20), 0, Settings.width/100, (int) (Settings.height - ((Settings.height) * getZ())/Settings.depth));
		page.drawRect((int) (Settings.width - Settings.width/100 - 20), 0, Settings.width/100, (int) (Settings.height));
		
		//drawing ball position box
		page.drawRect(Settings.width/2-getRectSizeWidth(getZ())/2,Settings.height/2-getRectSizeHeight(getZ())/2,getRectSizeWidth(getZ()),getRectSizeHeight(getZ()));
		
		super.paint(page);	
	}

	public void resize() {
		setSize(Settings.width/50,Settings.width/50,Settings.width/50);

	}
}
