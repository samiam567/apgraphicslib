package apgraphicslib;

import java.awt.Graphics;

public class FPS_display extends ScoreBoard {
	private int loops = 0;

	public FPS_display(Object_draw drawer1, double x, double y) {
		super(drawer1, x, y, "FPS:", 0);
		decimalsToRound = 0;
		setName("Unnamed FPSDisplay");
	}

	public void paint(Graphics page) {
		if (loops % Settings.targetFPS == 0) {
			setScore(getDrawer().getActualFPS());
			loops = 1;
		}
		loops++;
		super.paint(page);
		
	}

	
	
}
