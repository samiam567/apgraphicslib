package apgraphicslib;

/**
 * Will call a method after the passed time. Should not be added to the drawer.
 * @author samiam567
 */
public abstract class Timer extends Physics_object implements Updatable {

	public enum TimerUnits {Frames, subCalculations, seconds};
	private double timePassed = 0, timeAmount;
	private long nanoSecondsTimeKeeper;
	private TimerUnits units;
	public Timer(Object_draw drawer, double time, TimerUnits units) {
		super(drawer);
		setName("Timer");
		timeAmount = time;
		this.units = units;
		drawer.add(this);
		nanoSecondsTimeKeeper = System.nanoTime();
	}
	
	
	/**
	 * this method is called when the time is up. Should be overwritten by children
	 */
	protected void triggerEvent() {
		Exception e = new Exception("Timer must override triggerEvent() ");
		e.printStackTrace();
	}
	
	private void checkTimerOver() {	
		if (timePassed >= timeAmount) {
			triggerEvent();
			getDrawer().remove(this);
		}
	}
	
	@Override
	public void prePaintUpdate() {
		if (units.equals(TimerUnits.Frames)) {
			timePassed++;
			checkTimerOver();
		}
	}
	
	@Override
	public void Update(double frames) {
		if (units.equals(TimerUnits.subCalculations)) {
			timePassed++;
			checkTimerOver();
		}else if (units.equals(TimerUnits.seconds)) {
			
			timePassed += ((double)(System.nanoTime()-nanoSecondsTimeKeeper)) / 1000000000;
			nanoSecondsTimeKeeper = System.nanoTime();
			checkTimerOver();
		}
	}

}
