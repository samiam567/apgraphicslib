package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Shapes.Rectangle;
import apgraphicslib.CollisionEvent;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;

public class Laser extends Rectangle implements Tangible {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1946879174197910435L;

	private boolean isTangible = true;
	public Laser(Object_draw drawer1, int x, int y, double xSize, double ySize) {
		super(drawer1,x, y, xSize, ySize);
		setColor(Color.red);
		
		setName("thing");
		
	}
	
	public void Update(double frames) {
		super.Update(frames);
		
		if (getX() < 0) {
			setPos(Math.random() * 1500 + Settings.width+100, Math.random() * (Settings.height-getXSize()-150));
			getSpeed().setI(-JetPack_JoyRide.jetpack_speed);
		}
		
		else if ( (getX()+20 < JetPack_JoyRide.jetpack.getX()) || (getX()-50 >JetPack_JoyRide.jetpack.getX()) ) {
			isTangible = false;
		}else {
			isTangible = true;
		}
		
		getSpeed().setI(-JetPack_JoyRide.jetpack_speed);
	}
		
		

	
	
	public void paint(Graphics page) {
		page.fillRect((int) (getX() - getXSize()/2),(int) (getY() - getYSize()/2), (int) Math.round(getXSize()),(int) Math.round(getYSize()));
		page.setColor(Color.ORANGE);
		page.fillRect((int) (getX() - getXSize()/4), (int) (getY() - getYSize()/4), (int) Math.round(getXSize()/2),(int) Math.round(getYSize()/2));
	}

	@Override
	public void collision(CollisionEvent object) {}
}
