package apgraphicslib;

import java.awt.Font;
import java.awt.Graphics;

public class APLabel extends Physics_drawable implements CameraMovable {
	private String message = "";
	private Font font = new Font("TimesRoman", Font.PLAIN, 15);
	
	public APLabel(Object_draw drawer, double x, double y) {
		super(drawer, x, y);
	}
	
	public void setMessage(String newMessage) {
		message = newMessage;
	}
	
	public void setFont(Font newFont) {
		font = newFont;
	}
	
	@Override
	public void paint(Graphics page) {
		page.setFont(font);
		page.drawString(message, (int) (getX() - message.length()*Math.sqrt(font.getSize2D()) ), (int) ( getY() - Math.sqrt(font.getSize2D())));
	}
	
	@Override
	public void paint(Camera cam, Graphics page) {
		double x,y=0;
		x = cam.getCoordinates().getX();
		try {
			y = cam.getCoordinates().getY();
		}catch(ClassCastException c) {}
		
		page.setFont(font);
		page.drawString(message, (int) ( getX() - x + cam.getFrameWidth()/2 - message.length()*Math.sqrt(font.getSize2D()) ), (int) ( + getY() - y +cam.getFrameHeight()/2 - Math.sqrt(font.getSize2D())));
	}

	/**
	 * {@summary this component is already too lightweight to benefit from Camera data usage (it just calculates in the paint)}
	 * {@code does nothing}
	 */
	@Override
	public void createCameraPaintData(Camera cam) {}

	/**
	 * {@summary this component is already too lightweight to benefit from Camera data usage (it just calculates in the paint)}
	 * {@code does nothing}
	 */
	@Override
	public void deleteCameraPaintData(Camera cam) {}

	/**
	 * {@summary this component is already too lightweight to benefit from Camera data usage (it just calculates in the paint)}
	 * {@code does nothing}
	 */
	@Override
	public void updateCameraPaintData(Camera cam) {}
	
	
	

}
