package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Physics_engine.*;
import Physics_engine.Physics_engine_toolbox.faces;

public class Laser extends rectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1946879174197910435L;

	public Laser(object_draw drawer1, int x, int y, double xSize, double ySize) {
		super(drawer1,x, y, 0, xSize, ySize, 1);
		setColor(Color.red);
		drawMethod = "paint";
		affectedByBorder = false;
		name = "thing";
		
	}
	
	public void secondaryUpdate() {
		points[0].setPos(xReal, yReal, zReal);
		if (getXReal() < 0) {
			setPos(Math.random() * 1500 + Settings.width+100, Math.random() * (Settings.height-getXSize()-150), getZReal());
			setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
		}
		
		else if ( (getXReal()+20 < JetPack_JoyRide.jetpack.getXReal()) || (getXReal()-50 >JetPack_JoyRide.jetpack.getXReal()) ) {
			isTangible = false;
		}else {
			isTangible = true;
		}
		
		setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
	}
		
		

	
	
	public void paint(Graphics page) {
		page.fillRect(getX(),getY(), (int) Math.round(xSizeAppearance),(int) Math.round(ySizeAppearance));
		page.setColor(Color.ORANGE);
		page.fillRect(getX() + (int) Math.round(xSizeAppearance/4),getY() + (int) Math.round(ySizeAppearance/4), (int) Math.round(xSizeAppearance/2),(int) Math.round(ySizeAppearance/2));
	}
}
