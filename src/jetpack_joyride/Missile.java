package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import apgraphicslib.CollisionEvent;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import shapes.Rectangle;

public class Missile extends Rectangle implements Tangible {

	public static final int missileHomingSpeed = 40;
	
	private static final double missileSpeedMultiplier = 2.3;


	
	public Missile(Object_draw drawer1,double x, double y) {
		super(drawer1,x, y, 30, 10);
	
		setColor(Color.red);
		
		setName("_missile");
		
		reCalculateSize();
	}

	public void Update(double frames) {
		super.Update(frames);
		getSpeed().setI(-JetPack_JoyRide.jetpack_speed * missileSpeedMultiplier);

		if (getX() < 0) {
			setPos(Settings.width+1000 + Math.random() * 1500, Math.random() * (Settings.height-getXSize()-150));
		}else if ( (getX()+10 < JetPack_JoyRide.jetpack.getX()) || (getX()-50 >JetPack_JoyRide.jetpack.getX()) ) {
			setIsTangible(false);
		}else {
			setIsTangible(true);
		}
		

		if (JetPack_JoyRide.jetpack.getX() < getX() + 10) { //only activate guidance when the missile is converging on target
			//homing in on jetpack
			if (JetPack_JoyRide.jetpack.getY() > getY()) {
				getSpeed().setJ(missileHomingSpeed);
			}else if (JetPack_JoyRide.jetpack.getY() < getY()) {
				getSpeed().setJ(-missileHomingSpeed);
			}else {
				getSpeed().setJ(0);
			}
		}else {
			getSpeed().setJ(0);
		}
		
	}
	
	public void paint(Graphics page) {
		
		
		//fire
		page.setColor(Color.orange);
		page.fillRect((int) (getX() + 0.9 * getXSize()) ,(int) ( getY() + getYSize()/5), (int) Math.round( getXSize())/2, 2 * (int)Math.round( getYSize())/3 );
			
		page.setColor(Color.red);	
		
		//body
		page.fillRoundRect((int) getX(),(int) getY(), (int) Math.round( getXSize()), (int)Math.round( getYSize()),  (int)(  getYSize()/2) ,(int)( getYSize()/2));
			
		//missile warning sign
		if (getX() > Settings.width) {
			page.fillRoundRect(Settings.width - (int)(2.2 * getYSize()) - 10, (int) getY(), (int)( getYSize()), (int)( getYSize()), (int)( getYSize()/2) ,(int)( getYSize()/2));
		}
		
			
		
	}

	@Override
	public void collision(CollisionEvent event) {
		
	}

}
