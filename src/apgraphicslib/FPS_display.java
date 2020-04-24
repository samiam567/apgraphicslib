package apgraphicslib;

import java.awt.Graphics;

public class FPS_display extends ScoreBoard {
	private int loops = 0;
	private long lastRenderTime = System.nanoTime();

	public FPS_display(Object_draw drawer1, double x, double y) {
		super(drawer1, x, y, "FPS:", 0);
		decimalsToRound = 0;
		setName("Unnamed FPSDisplay");
	}

	public void paint(Graphics page) {
		setScore(getDrawer().getActualFPS());
		super.paint(page);
		
	}

	
	
}
