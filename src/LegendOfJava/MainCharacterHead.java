package LegendOfJava;

import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Tangible;
import apgraphicslib.Three_dimensional;

//this is exactly the same as normal head but it can hit walls
public class MainCharacterHead extends PlayerHead implements Tangible {

	public MainCharacterHead(Character parentPlayer, int ppSize) {
		super(parentPlayer, ppSize);
	}
	
	public boolean checkForCollision(Coordinate2D point, Tangible ob, double radius) {
		
		//getting the three-dimensional coordinates of point
		Coordinate3D point3D;
		try {
			//try to make the point a 3D point
			point3D = (Coordinate3D) point;
		}catch(ClassCastException c) { //if the point was 2D, just set it at zPos 0 and carry on
			point3D = new Coordinate3D(point.getX(),point.getY(),0);
		}
		
		
		
		//getting the Three-dimensional equivalent position of the object the point is in
		double obX = ob.getX();
		double obY = ob.getY();
		double obZ;
		try {
			obZ = ((Three_dimensional) ob).getZ();
		}catch(ClassCastException c) {
			obZ = 0;
		}
		
		
		//do a cylindrical boundary box covering the character
		if ((point.getY() + obY >= getY() - PlayerHead.headYSize/2 - radius/2) && (point.getY() + obY <= getY() + PlayerTorso.torsoYSize + radius/2) ) {
			return (Physics_engine_toolbox.distance2D(getX(), getZ(), obX + point3D.getX(), obZ + point3D.getZ()) < PlayerHead.headXSize + radius);
		}
		
		return false;
	}
	
	
	public Coordinate3D checkForCollision(Tangible object) {
		//just worry about other obs hitting this as this method would take too long to be worth it. 
		//Note that this means characters cannot hit each other
		return null;
	}	
}
