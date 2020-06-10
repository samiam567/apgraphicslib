package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import javax.swing.JPanel;


/**
 * {@code will paint objects that are added to it according to the perspective of the camera }
 */
public abstract class Camera extends JPanel implements Updatable, Physics_engine_compatible {
	
	private static int numCameras = 0;
	
	private int id;
	
	protected Coordinate2D cameraPosition;
	protected Vector cameraPanVelocity;
	protected Vector cameraRotation;
	protected Vector cameraAngularVelocity;
	
	protected LinkedList<CameraMovable> cameraObs = new LinkedList<CameraMovable>(); //the objects to draw on the camera view frame
	protected LinkedList<Drawable> paintOnlyObs = new LinkedList<Drawable>(); //the objects to only draw on the camera frame
	
	protected Physics_frame frame;
	protected String name = "unNamed Camera";
	protected Object_draw drawer;
	
	/**
	 * {@code CAMERA MUST BE added to an Object_draw by either using it as the constructor parameter or using addCamera()}
	 * @param cameraPosition
	 */
	public Camera(Coordinate2D cameraPosition) {
		setName("Unnamed Camera");
		numCameras++;
		id = numCameras;
		this.cameraPosition = cameraPosition;
		cameraPanVelocity = new Vector(0);
		cameraRotation = new Vector(0);
		cameraAngularVelocity = new Vector(0);
		frame = new Physics_frame(this);
		setDoubleBuffered(false);
		
	}
	
	/**
	 * {@code sets the objects drawer. Note: does not add the object to the passed drawer}
	 * @param drawer
	 */
	public void setDrawer(Object_draw drawer) {
		this.drawer = drawer;
		
		drawer.add((Updatable) this);
	}
	
	
	@Override
	public void prePaintUpdate() {
		cameraPosition.add(cameraPanVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		cameraRotation.add(cameraAngularVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		
		for (CameraMovable cOb : cameraObs) {
			cOb.updateCameraPaintData(this);
		}
	}
	
	private class CamObsSorter implements Comparator<CameraMovable> {
		private Camera cam; //the camera reference to get the paintRenderOrders from 
		
		public CamObsSorter(Camera camera) {
			cam = camera;
		}
		@Override
		public int compare(CameraMovable o1, CameraMovable o2) {
			return Double.compare(o2.getPaintOrderValue(cam),o1.getPaintOrderValue(cam));	
		}	
	}
	
	@Override
	public void paint(Graphics page) {	
		try {
			page.setColor(frame.getBackground());
			page.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
			page.setColor(Color.black);
			
			
			cameraObs.sort(new CamObsSorter(this));
			
			for (Drawable cOb : paintOnlyObs) {
				page.setColor(cOb.getColor());
				cOb.paint(page);
			}
			
			for (CameraMovable cOb : cameraObs) {
				page.setColor(cOb.getColor());
				cOb.paint(this,page);
			}
		
		}catch(ConcurrentModificationException c) {
			getDrawer().out.println(c + " in paint(Graphics) in Camera: " + getName());
		}
	}
	
	
	public Physics_frame getFrame() {
		return frame;
	}
	
	
	public void add(CameraMovable newOb) {
		cameraObs.add(newOb);
		newOb.createCameraPaintData(this);
	}
	
	public void remove(CameraMovable remOb) {
		cameraObs.remove(remOb);
		remOb.deleteCameraPaintData(this);
	}
	
	public void addPaintOnly(Drawable newOb) {
		paintOnlyObs.add(newOb);
	}
	
	public void removePaintOnly(Drawable remOb) {
		paintOnlyObs.remove(remOb);
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

	public Coordinate2D getCoordinates() {
		return cameraPosition;
	}
	
	public int getId() {
		return id;
	}

	public Vector getRotation() {
		return cameraRotation;
	}
	
	public int getFrameWidth() {
		return frame.getWidth();
	}
	
	public int getFrameHeight() {
		return frame.getHeight();
	}
}
