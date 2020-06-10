package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;

import apgraphicslib.CollisionEvent;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Resizable;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import apgraphicslib.Vector2D;
import shapes.Rectangle;

public class JetPack extends Rectangle implements Tangible, Resizable {
	
	public double fireSize = 0.4;
	
	public double power = 4000;
	
	public double current_power;
	
	public JetPack(Object_draw drawer1, int x, int y, int z, int size, double mass) {
		super(drawer1,x, y, z, size);
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		getSpeed().setI(0);
		
		current_power = power + JetPack_JoyRide.jetpack_speed;
		
		if (getY() < 0) {
			setPos(getX(),40);
			getSpeed().setJ(1);
		}else if (getY() + getYSize()/2 > Settings.height-40) {
			setPos(getX(),Settings.height - 2*getYSize() - 100);
			getSpeed().setJ(-10);
		}
	}
	
	@Override
	public void collision(CollisionEvent event) {
		if (event.objectHit != null) {
			try {
			//this try/catch checks if the object is of the Laser class
			    Laser c = (Laser) event.objectHit;
			    System.out.println("game over");
			    System.out.println("you hit " + event.objectHit.getName());
				JetPack_JoyRide.game_over = 1;
				
			} catch (ClassCastException e) {}
			
			try {
				//this try/catch checks if the object is of the Missile class
			    Missile c = (Missile) event.objectHit;
			    System.out.println("game over");
			    System.out.println("you hit " + event.objectHit.getName());
				JetPack_JoyRide.game_over = 1;
			} catch (ClassCastException e) {}
			
			//this try/catch checks if the object is of the Coin class
			try {
			    Coin coin = (Coin) event.objectHit;
			    JetPack_JoyRide.coins++;
			    JetPack_JoyRide.coinsEarned++;
			    coin.coinReLocate();
			    
			} catch (ClassCastException e) {}
			
	
		}
	}
	
	
	
	public void paint(Graphics page) {

	
		page.fillRect((int) (getX() - getXSize()/2),(int) (getY() - getYSize()/2), (int) Math.round(getYSize()/2), (int) Math.round(getYSize()));
		page.fillRect((int) Math.round(getX()) - 3, (int) (getY() - getYSize()/2), (int) Math.round(getYSize()/2), (int) Math.round(getYSize()));
		
		page.setColor(Color.DARK_GRAY);
		page.drawRect((int) (getX() - getXSize()/2),(int) (getY() - getYSize()/2), (int) Math.round(getYSize()/2), (int) Math.round(getYSize()));
		page.drawRect((int) Math.round(getX()) , (int) (getY() - getYSize()/2), (int) Math.round(getYSize()/2)-3, (int) Math.round(getYSize()));
		
		if (fireSize > 0.4) {
			page.setColor(Color.yellow);
			page.fillRect((int)  (getX() - getXSize()/2)+3,(int) ( getY()+Math.round(getYSize()/2)), (int) Math.round(getYSize()/2)-6, (int) Math.round(fireSize*getYSize()/2));
			page.fillRect((int) ((int) getX()+1),(int) ( getY()+Math.round(getYSize()/2)), (int) Math.round(getYSize()/2)-6, (int) Math.round(fireSize*getYSize()/2));
		}
		
		page.setColor(Color.orange);
		page.fillRect((int) ((getX() - getXSize()/2)+2),(int) ( getY()+Math.round(getYSize()/2)), (int) Math.round(getYSize()/2)-4, (int) Math.round(0.45*getYSize()/2));
		page.fillRect((int) ((int) getX()),(int) ( getY()+Math.round(getYSize()/2)), (int) Math.round(getYSize()/2)-4, (int) Math.round(0.45*getYSize()/2));
	
		}

	@Override
	public void resize() {
		JetPack_JoyRide.resize();
	}



}
