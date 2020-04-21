package apgraphicslib;

import java.awt.Graphics;

public class FPS_display extends ScoreBoard {
	private int loops = 0;
	private long lastRenderTime = System.nanoTime();

	public FPS_display(Object_draw drawer1, double x, double y) {
		super(drawer1, x, y, "FPS:", 0);
		roundScore = true;
		setName("Unnamed FPSDisplay");
	}

	public void paint(Graphics page) {
		if (loops % 10 == 0) {
			setScore(1000000000/((double)(System.nanoTime() - lastRenderTime))); //set the displayValue to the frequency this object is being painted
		}
		loops++;
		lastRenderTime = System.nanoTime();
		super.paint(page);
		
	}

	
	
}
