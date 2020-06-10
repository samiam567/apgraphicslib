package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import shapes.Square;


public class JetPack_fire extends Square {

	private static JetPack_fire[] preMadeFires = new JetPack_fire[100];
	
	private static int nextFireIndx = 0;

	public JetPack_fire(Object_draw drawer1) {
		super(drawer1,-100, -100, 5);
		setIsFilled(true);
	}
	
	public void init(double x, double y,double AngularVelocity,double xSpeed) {
		setPos(x, y);
		setSize(JetPack_JoyRide.jetpack.getXSize()/5, JetPack_JoyRide.jetpack.getXSize()/5);

  	  	setColor(Color.ORANGE);
  	  	getSpeed().setIJ(xSpeed * 10, 500 + JetPack_JoyRide.jetpack.getSpeed().getI());	
  	  	
  	  	getAcceleration().setIJ(0,9.8);
  	  	getAngularVelocity().setR(AngularVelocity);
	}
	
	public static void loadFires(Object_draw drawer) {
		for (int i = 0; i < preMadeFires.length; i++) {
			preMadeFires[i] = new JetPack_fire(drawer);
			preMadeFires[i].setIsVisible(false);
			drawer.add(preMadeFires[i]);
		}
	}
	
	public static void showFire() {
		
		JetPack_fire fire = preMadeFires[nextFireIndx];
	
	
		if (nextFireIndx % 2 == 0) {
			fire.init(JetPack_JoyRide.jetpack.getX() - JetPack_JoyRide.jetpack.getXSize()/5 ,JetPack_JoyRide.jetpack.getY() + JetPack_JoyRide.jetpack.getYSize() + JetPack_JoyRide.jetpack.getYSize()/2*0.2 ,Math.random() /2 ,-1); 
		}else {
			fire.init(JetPack_JoyRide.jetpack.getX() + JetPack_JoyRide.jetpack.getXSize()/5,JetPack_JoyRide.jetpack.getY() + JetPack_JoyRide.jetpack.getYSize()/2 + JetPack_JoyRide.jetpack.getYSize()*0.2,-Math.random() / 2 ,1); 	
		}
		
		fire.setIsVisible(true);
		if (nextFireIndx < preMadeFires.length-1) {
			nextFireIndx++;
		}else {
			nextFireIndx = 0;
		}
	}
	
	public void Update(double frames) {
		super.Update(frames);
		if (getY() > (Settings.height)) {
			setIsVisible(false);
		}else if (getY() < 0) {
			setIsVisible(false);
		}
	}
	


	@Override
	public void paint(Graphics page) {
		
		//page.fillRect((int) getX(),(int) getY(),(int) getXSize(),(int) getYSize());
		
		super.paint(page);
		
	}

}
