package LegendOfJava2;

import apgraphicslib.Coordinate2D;
import apgraphicslib.Drawable;
import apgraphicslib.Three_dimensional;
import apgraphicslib.Vector;

public interface PlayerBodyPartAble extends Drawable, Three_dimensional {

	public void setPointOfRotation(Coordinate2D coordinates, boolean b);

	public void setOrbitalAngularVelocity(Vector rotVec);

	public void setOrbitalRotation(Vector rotVec);

}
