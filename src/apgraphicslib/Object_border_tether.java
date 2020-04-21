package apgraphicslib;

/**
 * 
 * @author samiam567
 * This class will keep track of whether objects are outside the frame and will notify the user if they are
 *
 */
public class Object_border_tether extends Physics_object implements Updatable {

	public Object_border_tether(Object_draw drawer) {
		super(drawer);
		setName("Unnamed border_tether");
	}

	@Override
	public void Update(double frames) {
		for (Drawable cObject : getDrawer().getDrawables()) {
			if ((cObject.getX() > Settings.width) || (cObject.getX() < 0) || (cObject.getY() > Settings.height) || (cObject.getY() < 0) ) { 
				System.out.println(cObject.getName() + " is outside the screen. x: " + cObject.getX() + " y: " + cObject.getY());
				 
			}
		}
		
	}
	
	@Override 
	public void prePaintUpdate() {
		
	}

}
