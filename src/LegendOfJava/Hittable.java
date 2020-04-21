package LegendOfJava;

import apgraphicslib.Drawable;
import apgraphicslib.Three_dimensional;

public interface Hittable extends Three_dimensional, Drawable {
	public void hit(double attackPower);
}
