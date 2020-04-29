package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Physics_engine.*;
import Physics_engine.Physics_engine_toolbox.faces;
import Shapes.Square;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Tangible;
import apgraphicslib.Vector2D;

public class Coin extends Square implements Tangible {
	
	private boolean isTangible = true;
	
	public Coin(Object_draw drawer1, int x, int y) {
		super(drawer1, x, y, 20);
		setName("_coin");
		setColor(Color.YELLOW);

	}

	public void coinReLocate() {
		setPos(Settings.width * 20 * Math.random() + (Settings.width+100), Math.random() * (Settings.height-getXSize()-150));
		((Vector2D) getSpeed()).setI(-JetPack_JoyRide.jetpack_speed);
	}	
	
	
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
			if (getX() < 0) {
				setPos(Settings.width+100, Math.random() * (Settings.height-getXSize()-150));
				((Vector2D) getSpeed()).setI(-JetPack_JoyRide.jetpack_speed);
			}else if ( (getX()+10 < JetPack_JoyRide.jetpack.getX()) || (getX()-50 >JetPack_JoyRide.jetpack.getX()) ) {
				isTangible = false;
			}else {
				isTangible = true;
			}
		
			((Vector2D) getSpeed()).setI(-JetPack_JoyRide.jetpack_speed);
		}
		

	
	public void paint(Graphics page) {
	
		page.fillOval((int) (getX() - getXSize()/2) ,(int) ( getY() - getYSize()/2), (int) Math.round(getXSize()),(int) Math.round(getYSize()));
		
	}
}
