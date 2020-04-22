package apgraphicslib;

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Physics_frame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2710513018741366660L;
	public Container cp;
	public Object_draw drawer;
	public Rectangle boundingRectangle;

	private static int frameCount;
	
	public Physics_frame(Object_draw drawer) {
		frameCount++;
		setSize((int) (Settings.width), (int) (Settings.height));
		setTitle("Physics-Engine V" + Settings.version + "           Programmed by Alec Pannunzio ID:" + frameCount);
		cp = getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBackground(Settings.frameColor);		
		cp.setBackground(Settings.frameColor);
		
		cp.add(drawer); //add the object_draw to the content pane
		

		
		setVisible(true);
	}
	
	
	public void setColor(Color newColor) { 
		if (newColor != getBackground()) {
			setBackground(newColor);		
			cp.setBackground(newColor);
		}
	}
	
	public boolean checkIsInFrame(Drawable current_object) { //this dont work
		
		if ( (current_object.getX() < getWidth()) && (current_object.getX() > 0) && (current_object.getY() < getHeight()) && (current_object.getY() > 0)) {
				return true;
		}
		
		return false;
		
	}
	
	public boolean checkIsInFrame(Two_dimensional current_object) { //this dont work
		
		if ( (current_object.getX() - current_object.getXSize()/2 < getWidth()) && (current_object.getX() + current_object.getXSize()/2 > 0) && (current_object.getY() - current_object.getYSize()/2 < getHeight()) && (current_object.getY() + current_object.getYSize()/2 > 0)) {
				return true;
		}
		
		return false;
		
	}
	
}
