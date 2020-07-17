package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Physics_2DPolygon extends Physics_2DDrawMovable implements Updatable, Rotatable, Two_dimensional, CameraMovable {
	
	/** 
	 * @author samiam567
	 * {@summary a point that can be used in a Physics_[]Dpolygon}
	 * {@code defined in Physics_2DPolygon}
	 */
	protected interface PolyPoint {
		public void rotate(AffineRotation rotation);
		public double getX();
		public double getY();
		public void rotate(Vector rotation);
		public void add(Vector tempStatMultiply);
	}
	
	protected static class AffineRotation {
		protected double a,b,c,d;
		protected Vector rotation;
		
		/**
		 * {@summary will create a AffineRotation that will rotate the points [rotation] radians MORE than they already are rotated}
		 */
		protected void calculateRotation(Vector rotation) {
			this.rotation = rotation;
			double theta = rotation.getR();
			a = Math.cos(theta);
			b = -Math.sin(theta);
			c = -b;
			d = a;
		}
	}
	
	protected static class Point2D extends Coordinate2D implements PolyPoint, Two_dimensional {
		protected double prevAngle = 0;

		public Point2D(double x, double y) {
			super(x, y);
			
		}	
		
		public void rotate(AffineRotation rotation) {
			setPos(rotation.a * getX() + rotation.b * getY(), rotation.c * getX() + rotation.d * getY());
		}
	
		// this is my rotation alg
		public void rotate(Vector rotation) {
			double[] rotComps = Vector2D.rectangularToPolar(x, y);
			rotComps = Vector2D.polarToRectangular(rotComps[0], rotComps[1] + (rotation.getR()-prevAngle));	
			setPos(rotComps[0], rotComps[1]);
			prevAngle  = rotation.getR();
		} 
	}
	
	
	
	protected AffineRotation rotationMatrix = new AffineRotation();
	
	protected Vector rotation = new Vector();
	protected Vector angularVelocity = new Vector();
	protected Vector angularAcceleration = new Vector();
	private Vector angVFrames; //used by the Update() method
	
	protected boolean rotateWithOrbit = false;
	protected Coordinate2D pointOfRotation = coordinates;
	
	protected Vector orbitalRotation = new Vector();
	protected Vector orbitalAngularVelocity = new Vector();
	protected Vector orbitalAngularAcceleration = new Vector();
	private Vector orbitalAngVFrames; //used by the Update() method
	protected Coordinate2D pORCoordsTemp = new Coordinate2D(0,0); //used by the Update() method
	
	//these are used for painting the object
	protected int[] pointXValues;
	protected int[] pointYValues;
	protected int numPoints = 0;
	
	private ArrayList<PolyPoint> points = new ArrayList<PolyPoint>();
	
	private boolean isFilled = false;

	public Physics_2DPolygon(Object_draw drawer, double x, double y) {
		super(drawer, x, y);
		coordinates = new Point2D(x,y); //Point2D so we can rotate the object about an outside point
		pointXValues = new int[0];
		pointYValues = new int[0];
		pointOfRotation = coordinates;
	}
	
	public void addPoint(double x, double y) {
		addPoint(new Point2D(x,y));
	}
	
	/**
	 * {@summary adds a point to the points array and changes xSize and ySize if they are not big enough to fit it}
	 * @param newPoint
	 */
	protected void addPoint(PolyPoint newPoint) {
		numPoints++;
	
		pointXValues = new int[numPoints];
		pointYValues = new int[numPoints];
		
		//resizing 
		if (Math.abs(newPoint.getX()) > getXSize()/2) setSize(Math.abs(newPoint.getX()*2),getYSize());
		if (Math.abs(newPoint.getY()) > getYSize()/2) setSize(getXSize(),Math.abs(newPoint.getY()*2));
		points.add(newPoint);
		
		updatePointValueLists();
		
		
	}
	
	/**
	 * @author samiam567
	 * {@summary rotates the points in the object}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	protected void updatePoints() {		
		for (PolyPoint cPoint : points) {
			cPoint.rotate(rotationMatrix);
		}
	}
	
	/**
	 * {@code resets the size based on the coordinates of the points}
	 */
	public void reCalculateSize() {
		setSize(0,0);
		for (PolyPoint cPoint : getPoints()) {
			if (Math.abs(cPoint.getX()) > getXSize()/2) setSize(Math.abs(cPoint.getX()*2),getYSize());
			if (Math.abs(cPoint.getY()) > getYSize()/2) setSize(getXSize(),Math.abs(cPoint.getY()*2));
		}
	}

	
	protected void updatePointValueLists() {
		int pointIndx = 0;
		
		
		for (PolyPoint cPoint : points) {
			pointXValues[pointIndx] = (int) (cPoint.getX() + getX());
			pointYValues[pointIndx] = (int) (cPoint.getY() + getY());
			pointIndx++;
		}
		numPoints = pointIndx;
	}
	

	/**
	 * {@code add angV to rotation, add angAccel to angV, updatePoints}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		//orbital rotation
		if (orbitalAngularAcceleration.getR() != 0) orbitalAngularVelocity.add(orbitalAngularAcceleration.tempStatMultiply(frames));
			
		orbitalAngVFrames = orbitalAngularVelocity.tempStatMultiply(frames);
			
		orbitalRotation.add(orbitalAngVFrames);	
			
		rotationMatrix.calculateRotation(orbitalAngVFrames);
			
		pORCoordsTemp.setPos(pointOfRotation);
			
		getCoordinates().subtract(pORCoordsTemp);
		((PolyPoint) getCoordinates()).rotate(rotationMatrix);
		getCoordinates().add(pORCoordsTemp);
		
		if (rotateWithOrbit ) {
			updatePoints(); //this will rotate the points with the orbital motion
			rotation.add(orbitalAngVFrames);
		}
			
		
		//rotation about a point
		if (angularAcceleration.getR() != 0) angularVelocity.add(((Vector2D) angularAcceleration).tempStatMultiply(frames));
		if ( angularVelocity.getR() != 0) {
			angVFrames = angularVelocity.tempStatMultiply(frames);
			rotationMatrix.calculateRotation(angVFrames);
			rotation.add(angVFrames);	
			updatePoints();
		}
		
	
		
	}
	
	/**
	 * {@summary a one-time rotation that rotates all the points the given Vector}
	 * @param rotation
	 */
	public void rotatePoints(Vector rotation) {
		getDrawer().pause();
		rotationMatrix.calculateRotation(rotation);
		updatePoints();
		getDrawer().resume();
	}
	
	/**
	 * {@summary rotates the object a certain angle}
	 */
	public void rotate(Vector rotation) {
		getDrawer().pause();
		this.rotation.add(rotation);
		rotationMatrix.calculateRotation(rotation);
		updatePoints();
		getDrawer().resume();
	}
	
	/**
	 * {@summary rotates the object about a point}
	 * @param rotation the rotation to perform
	 * @param pointOfRotation the point to rotate about
	 */
	public void rotateAbout(Vector rotation, Coordinate2D pointOfRotation) {
		getDrawer().pause(); //pause the Object_draw so that it doesn't mess with this operation
		
		//calculate the rotation
		rotationMatrix.calculateRotation(rotation);
		this.rotation.add(rotation);
		this.orbitalRotation.add(rotation);
		
		//rotate the center point
		setPos(getX() - pointOfRotation.getX(), getY() - pointOfRotation.getY()); //shift so that the pointOfRotation is the origin	
		((Point2D) getCoordinates()).rotate(rotationMatrix); //rotate the center point
		setPos(getX() + pointOfRotation.getX(), getY() + pointOfRotation.getY()); //shift the ob back to its previous position
		
		//rotate the rest of the points
		updatePoints();

		getDrawer().resume(); //resume the Object_draw
	}
	
	protected ArrayList<PolyPoint> getPoints() {
		return points;
	}

	public void setRotation(double i, double j) {
		getDrawer().pause();
		rotate(((Vector2D) rotation).statMultiply(-1));
		
		((Vector2D) rotation).setI(i);
		((Vector2D) rotation).setJ(j);
		
		rotate(rotation);
		
		getDrawer().resume();
	}

	@Override
	public void setRotation(Vector rotVec) {
		getDrawer().pause();
		rotate(rotation.multiply(-1));
		rotation = rotVec;
		rotate(rotation);
		getDrawer().resume();
	}
	
	@Override
	public void setAngularVelocity(Vector newAngV) {
	
		angularVelocity = newAngV;
	}
	
	@Override
	public void setAngularAcceleration(Vector newAngAccel) {
		angularAcceleration = newAngAccel;
	}
	
	@Override
	public void setOrbitalRotation(Vector rotVec) {
		getDrawer().pause();
		rotateAbout(orbitalRotation.multiply(-1),pointOfRotation);
		orbitalRotation = rotVec;
		rotateAbout(orbitalRotation,pointOfRotation);
		getDrawer().resume();
	}
	
	@Override
	public void setOrbitalAngularVelocity(Vector newAngV) {
		orbitalAngularVelocity = newAngV;
	}
	
	@Override
	public void setOrbitalAngularAcceleration(Vector newAngAccel) {
		orbitalAngularAcceleration = newAngAccel;
	}
	
	@Override
	public Vector getRotation() {
		return rotation;
	}
	
	@Override
	public Vector getAngularVelocity() {
		return angularVelocity;
	}
	
	@Override
	public Vector getAngularAcceleration() {
		return angularAcceleration;
	}
	
	@Override
	public Vector getOrbitalRotation() {
		return orbitalRotation;
	}
	
	@Override
	public Vector getOrbitalAngularVelocity() {
		return orbitalAngularVelocity;
	}
	
	@Override
	public Vector getOrbitalAngularAcceleration() {
		return orbitalAngularAcceleration;
	}
	
	/**
	 * {@summary sets the point that the orbital rotation will rotate about}
	 * @param newPointOfRotation the point that the 
	 * @param rotateWithOrbit whether or not to rotate the physical object around the pointOfRotation and not just the position of the object around the pointOfRotation
	 */
	@Override
	public void setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit) {
		this.rotateWithOrbit = rotateWithOrbit;
		pointOfRotation = newPointOfRotation;
	}
	
	/// Camera stuff ////////////////////////////////////////////////////////////////////////////////
	private ArrayList<CameraPaintData> cameraDataSets = new ArrayList<CameraPaintData>();
	private class CameraPaintData {
		private Camera cam;
		private int[] camPointXValues;
		private int[] camPointYValues;	
		
		public CameraPaintData(Camera cam) {
			this.cam = cam;
			camPointXValues = new int[points.size()];
			camPointYValues = new int[points.size()];
		}
	}	
	
	private CameraPaintData getCameraPaintData(Camera cam) {
		for (CameraPaintData data : cameraDataSets) {
			if (data.cam.equals(cam)) return data;
		}
		
		Exception e = new Exception("data for Camera " + cam + " ID: " + cam.getId() + " cannot be found for " + getName() + this);
		e.printStackTrace(getDrawer().getOutputStream());
		return null;
	}
	
	public void createCameraPaintData(Camera cam) {
		cameraDataSets.add(new CameraPaintData(cam));
	}
	
	@Override
	public void deleteCameraPaintData(Camera cam) {
		cameraDataSets.remove(getCameraPaintData(cam));
	}
	
	public void updateCameraPaintData(Camera cam) {
		
		CameraPaintData data = getCameraPaintData(cam);
		
		
		
		double camX = cam.getCoordinates().getX(), camY = cam.getCoordinates().getY();
		
		double offSetX = camX - cam.getFrameWidth()/2;
		double offSetY = camY - cam.getFrameHeight()/2;
		
		AffineRotation affRot = new AffineRotation();
		affRot.calculateRotation(cam.getRotation());
		
		
		double obX = affRot.a * (getX() - offSetX - camX) + affRot.b * (getY() - offSetY - camY);
		double obY = affRot.c * (getX() - offSetX - camX) + affRot.d * (getY() - offSetY - camY);
		
		obX += camX;
		obY += camY;

		if (points.size() != data.camPointXValues.length) {
			data.camPointXValues = new int[points.size()];
			data.camPointYValues = new int[points.size()];
		}
		
		int pointIndx = 0;
		for (PolyPoint cPoint : points) {
			data.camPointXValues[pointIndx] = (int) (obX + affRot.a * (cPoint.getX()) + affRot.b * (cPoint.getY()));
			data.camPointYValues[pointIndx] = (int) (obY + affRot.c * (cPoint.getX()) + affRot.d * (cPoint.getY()));
			pointIndx++;
		}
		
		
	}
	
	@Override
	public void paint(Camera cam, Graphics page) {
		
		CameraPaintData data = getCameraPaintData(cam);
		
		if (getIsFilled()) {
			page.fillPolygon(data.camPointXValues, data.camPointYValues, numPoints);
		}else {
			page.drawPolygon(data.camPointXValues, data.camPointYValues, numPoints);
		}
	}
	
	/// Camera stuff end ////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	@Override
	public void paint(Graphics page) {
		
		updatePointValueLists();
		
		if (getIsFilled()) {
			page.fillPolygon(pointXValues, pointYValues, numPoints);
		}else {
			page.drawPolygon(pointXValues, pointYValues, numPoints);
		}
	}

	

	public void setIsFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

	@Override
	public double getPaintOrderValue(Camera cam) {
		return Physics_engine_toolbox.distance2D(getCoordinates(), cam.getCoordinates());
	}

	public boolean getIsFilled() {
		return isFilled;
	}



	
}
