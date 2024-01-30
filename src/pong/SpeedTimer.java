package pong;

import apgraphicslib.Movable;
import apgraphicslib.Object_draw;
import apgraphicslib.Timer;
import apgraphicslib.Vector3D;

public class SpeedTimer extends Timer { //waits for a certain magnitude of time, then applies a speed
	double xComponent,yComponent,zComponent; 
	Movable object;
	
	public SpeedTimer(Object_draw drawer1, double magnitude1, TimerUnits type1, double xComponent1,double yComponent1,double zComponent1,Movable object1) {
		super(drawer1,magnitude1, type1);
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;
		object = object1;
	}
	
	protected void endTimer() {
		object.setSpeed(new Vector3D(xComponent, yComponent, zComponent));
	}
	

}