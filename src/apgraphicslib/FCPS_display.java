package apgraphicslib;

/**
 * 
 * @author samiam567
 * {@summary displays the frameStep of its Object_draw}
 *
 */
public class FCPS_display extends ScoreBoard {  //frames calculated per second
	 
	private int loops = 0;
	public FCPS_display(Object_draw drawer1, double x, double y) {
		super(drawer1, x, y, "FStep:", drawer1.getFrameStep());
		decimalsToRound = 8;
		setName("Unnamed FCPS_display");
	}

	
	@Override
	public void Update(double frames) {
	
		loops++;
		super.Update(frames);
		try {
			if (loops % 50/getDrawer().getFrameStep() == 0) {
				setScore(getDrawer().getFrameStep());	
			}
		}catch(ArithmeticException a) {}

	}
}
