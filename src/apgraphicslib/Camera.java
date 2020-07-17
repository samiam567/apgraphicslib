package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import javax.swing.JPanel;

import apgraphicslib.Physics_2DPolygon.AffineRotation;
import apgraphicslib.Physics_2DPolygon.PolyPoint;
import apgraphicslib.Physics_2DPolygon.Point2D;

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
	
	protected AffineRotation rotationMatrix;
	
	protected boolean rotateWithOrbit = false;
	protected Coordinate2D pointOfRotation;
	
	protected Vector orbitalRotation = new Vector();
	protected Vector orbitalAngularVelocity = new Vector();
	protected Vector orbitalAngularAcceleration = new Vector();
	
	protected Coordinate2D pORCoordsTemp = new Coordinate2D(0,0); //used by the Update() method
	
	protected LinkedList<CameraMovable> cameraObs = new LinkedList<CameraMovable>(); //the objects to draw on the camera view frame
	protected LinkedList<Drawable> paintOnlyObs = new LinkedList<Drawable>(); //the objects to only draw on the camera frame
	
	protected Physics_frame frame;
	protected String name = "unNamed Camera";
	protected Object_draw drawer;
	
	public boolean perspective = Settings.perspective;
	
	/**
	 * {@code CAMERA MUST BE added to an Object_draw by either using it as the constructor parameter or using addCamera()}
	 * @param cameraPosition
	 */
	public Camera(Coordinate2D cameraPosition) {
		setName("Unnamed Camera");
		numCameras++;
		id = numCameras;
		this.cameraPosition = new Point2D(cameraPosition.getX(), cameraPosition.getY());
		cameraPanVelocity = new Vector(0);
		cameraRotation = new Vector(0);
		cameraAngularVelocity = new Vector(0);
		frame = new Physics_frame(this);
		setDoubleBuffered(false);
		rotationMatrix = new AffineRotation();
		pointOfRotation = cameraPosition;
	}
	
	/**
	 * {@code sets the objects drawer. Note: does not add the object to the passed drawer}
	 * @param drawer
	 */
	public void setDrawer(Object_draw drawer) {
		if ( ! drawer.equals(this.drawer)) {
			this.drawer = drawer;
			
			drawer.add((Updatable) this);
		}else {
			Exception e = new Exception("drawer already set to that drawer");
			e.printStackTrace(drawer.out);
		}
	}
	
	
	@Override
	public void prePaintUpdate() {
		
		//orbital rotation
		if (orbitalAngularAcceleration.getR() != 0) orbitalAngularVelocity.add(orbitalAngularAcceleration);
							
		orbitalRotation.add(orbitalAngularVelocity);	
					
		rotationMatrix.calculateRotation(orbitalAngularVelocity);
				
		pORCoordsTemp.setPos(pointOfRotation);
					
		getCoordinates().subtract(pORCoordsTemp);
		((PolyPoint) getCoordinates()).rotate(rotationMatrix);
		getCoordinates().add(pORCoordsTemp);
			
		if (rotateWithOrbit) {
			cameraRotation.add(orbitalAngularVelocity);
		}
			
		//end orbital rotation 
		
		
		cameraRotation.add(cameraAngularVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		
		cameraPosition.add(cameraPanVelocity.tempStatMultiply(Settings.timeSpeed / getDrawer().getActualFPS()));
		
		
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
			c.printStackTrace();
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
		//do nothing because we do updating in prePaintUpdate since this object is never used between frames
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
	
	public void setOrbitalRotation(Vector rotVec) {
		orbitalRotation = rotVec;
	}
	

	public void setOrbitalAngularVelocity(Vector newAngV) {
		orbitalAngularVelocity = newAngV;
	}

	public void setOrbitalAngularAcceleration(Vector newAngAccel) {
		orbitalAngularAcceleration = newAngAccel;
	}
	
	public void setPointOfRotation(Coordinate3D newPOR) {
		pointOfRotation = newPOR;
	}
	
	public void setRotateWithOrbit(boolean rot) {
		rotateWithOrbit = rot;
	}
	
	public Vector getOrbitalRotation() {
		return orbitalRotation;
	}
	
	public Vector getOrbitalAngularVelocity() {
		return orbitalAngularVelocity;
	}
	
	public Vector getOrbitalAngularAcceleration() {
		return orbitalAngularAcceleration;
	}
	
	public int getFrameWidth() {
		return frame.getWidth();
	}
	
	public int getFrameHeight() {
		return frame.getHeight();
	}
}
