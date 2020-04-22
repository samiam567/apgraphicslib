package LegendOfJava;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_drawable;

public class HealthBar extends Physics_drawable{
	
	private MainCharacter player;
	private BufferedImage img;
	private static String imgSrc = "src/LegendOfJava/assets/heart.png";
	private static int heartSize = 25;
	public HealthBar(Object_draw drawer, double x, double y, MainCharacter player) {
		super(drawer, x, y);
		this.player = player;
		File imgFile = new File(imgSrc);
		try {
			img = ImageIO.read(imgFile);
		} catch (IOException e) {
			System.out.println("cannot read " + imgSrc);
			e.printStackTrace();
		}
		
		//resize
		Image tmp = img.getScaledInstance(heartSize, heartSize, Image.SCALE_SMOOTH);
		img = new BufferedImage(heartSize, heartSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
		
	
	}
  
	
	@Override
	public void paint(Graphics page) {
 		for (int i = 0; i < player.HP; i++) {
 			page.drawImage(img,(int) (i*heartSize + getX()),(int) getY(),new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					
					return false;
				}
 			});
 		}
 	}

}
