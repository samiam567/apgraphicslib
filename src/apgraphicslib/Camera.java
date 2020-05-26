package apgraphicslib;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;


/**
 * {@code will paint objects that are added to it according to the perspective of the camera }
 */
public abstract class Camera extends JPanel implements Updatable {
	
	private Coordinate cameraPosition;
	private Vector cameraPanVelocity;
	private Vector cameraRotation;
	private Vector cameraAngularVelocity;
	
	private LinkedList<Drawable> cameraObs = new LinkedList<Drawable>(); //the objects to draw on the camera view frame
	
	private Physics_frame frame;
	private String name = "unNamed Camera";
	private Object_draw drawer;
	
	/**
	 * {@code CAMERA WILL NOT WORK UNLESS A DRAWER IS SET -- this can be done by either using this camera as the constructor parameter in the desired Object_draw or by using setDrawer()}
	 * @param cameraPosition
	 */
	public Camera(Coordinate cameraPosition) {
		this.cameraPosition = cameraPosition;
	}
	
	/**
	 * {@code sets the objects drawer. Note: does not add the object to the passed drawer}
	 * @param drawer
	 */
	public void setDrawer(Object_draw drawer) {
		this.drawer = drawer;
	}
	
	
	@Override
	public void prePaintUpdate() {
		
		
				
		cameraPosition.add(cameraPanVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		cameraRotation.add(cameraAngularVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		repaint();
	}
	
	@Override
	public void paint(Graphics page) {
		//TODO Implement this
	}
	
	
	@Override
	public void Update(double frames) {
		//do nothing
	}

	/**
	 * Gets the name of the object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the object
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Gets the Object_draw that this object was constructed with 
	 */
	public Object_draw getDrawer() {
		return drawer;
	}
}
