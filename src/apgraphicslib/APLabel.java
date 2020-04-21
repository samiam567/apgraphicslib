package apgraphicslib;

import java.awt.Font;
import java.awt.Graphics;

public class APLabel extends Physics_drawable {
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
		page.drawString(message, (int) ( getX() - message.length()*Math.sqrt(font.getSize2D()) ), (int) (getY() - Math.sqrt(font.getSize2D())));
	}

	

}
