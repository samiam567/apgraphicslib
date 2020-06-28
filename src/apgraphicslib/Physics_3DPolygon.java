package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import apgraphicslib.Physics_2DPolygon.AffineRotation;
import apgraphicslib.Physics_2DPolygon.PolyPoint;
import apgraphicslib.Physics_2DTexturedPolygon.RGBPoint2D;

public class Physics_3DPolygon extends Physics_2DPolygon implements Three_dimensional, Rotatable {
	
	private double zSize;
	
	
	public class AffineRotation3D extends AffineRotation {
		
		//for the rot matrices, a 5 will be replaced by a trig function in the calculateRotation() method
		private double[][] xRot = {
				{1,0,0,0}, //0
				{0,5,5,0}, //1
				{0,5,5,0}, //2
				{0,0,0,1}, //3
		};
		private double[][] yRot = {
				{5,0,5,0}, //0
				{0,1,0,0}, //1
				{5,0,5,0}, //2
				{0,0,0,1}, //3
		}; 
		private double[][] zRot = {
				{5,5,0,0}, //0
				{5,5,0,0}, //1
				{0,0,1,0}, //2
				{0,0,0,1}, //3
		};
		
		private double[][] affRotMatrix;
		private AffineRotation planeRotTheta = new AffineRotation(), planeRotPhi = new AffineRotation(), negativePlaneRotTheta = new AffineRotation(), negativePlaneRotPhi = new AffineRotation(), planeRotation = new AffineRotation();
		private boolean advancedRotation = false;
		public AffineRotation3D() {
		
		}
		
		public void calculateRotation(double xTheta, double yTheta, double zTheta) {	
			//xRotation
			xRot[1][1] = Math.cos(xTheta);
			xRot[2][1] = -Math.sin(xTheta);
			xRot[1][2] = Math.sin(xTheta);
			xRot[2][2] = Math.cos(xTheta);
			
			//yRotation
			yRot[0][0] = Math.cos(yTheta);
			yRot[2][0] = Math.sin(yTheta);
			yRot[0][2] = -Math.sin(yTheta);
			yRot[2][2] = Math.cos(yTheta);
			
			//zRotation
			zRot[0][0] = Math.cos(zTheta);
			zRot[1][0] = -Math.sin(zTheta);
			zRot[0][1] = Math.sin(zTheta);
			zRot[1][1] = Math.cos(zTheta);
		
			affRotMatrix = Physics_engine_toolbox.matrixMultiply(xRot, yRot);
			affRotMatrix = Physics_engine_toolbox.matrixMultiply(affRotMatrix, zRot);
		}
		
		/**
		 * {@summary will create a AffineRotation that will rotate the points rotation radians MORE than they already are rotated AROUND the passed Vector with the manitude of the passed vector}
		 */
		@Override
		public void calculateRotation(Vector rotation) {
			Vector3D rotTemp = ((Vector3D) rotation);
			
			if (Settings.advancedRotation) {
				advancedRotation = true;
				planeRotTheta.calculateRotation(new Vector(rotTemp.getTheta()));
				planeRotPhi.calculateRotation(new Vector(rotTemp.getPhi()));
				planeRotation.calculateRotation(new Vector(rotTemp.getR()));
				negativePlaneRotPhi.calculateRotation(new Vector(-rotTemp.getPhi()));
				negativePlaneRotTheta.calculateRotation(new Vector(-rotTemp.getTheta()));
			}else {
				advancedRotation = false;
				calculateRotation(rotTemp.getI(),rotTemp.getJ(),rotTemp.getK());
			}
		}
	}
	
	/**
	 * {@code a 3D point designed for a Physics_3DPolygon. Can rotate itself}
	 * @author samiam567
	 *
	 */
	protected class Point3D extends Coordinate3D implements PolyPoint {
		//multi-purpose lists for rotation
		private double[] rotMagsStat = new double[3];
		private double[] rotComps = new double[2];
		
		double prevAngle = 0;
		public Point3D(double x, double y, double z) {
			super(x, y,z);
			
		}	
	
	
		/**
		 * {@code rotates this point in accordance to the passed AffineRotation}
		 */
		public void rotate(AffineRotation rot) {
			AffineRotation3D AffineRot = (AffineRotation3D) rot;
			if (AffineRot.advancedRotation) {
				
				
				//rotate to the plane of the Vector
				setPos(AffineRot.planeRotTheta.a * getX() + AffineRot.planeRotTheta.b * getY(), AffineRot.planeRotTheta.c * getX() + AffineRot.planeRotTheta.d * getY()); //rotate to match phi
				setPos(AffineRot.planeRotPhi.a * getX() + AffineRot.planeRotPhi.b * getZ(), getY(), AffineRot.planeRotPhi.c * getX() + AffineRot.planeRotPhi.d * getZ()); //rotate to match theta
				
				//rotate in the plane of the Vector
				setPos(AffineRot.planeRotation.a * getX() + AffineRot.planeRotation.b * getY(), AffineRot.planeRotation.c * getX() + AffineRot.planeRotation.d * getY(), getZ()); //rotate to match theta
				
				//rotate back to original position
				setPos(AffineRot.negativePlaneRotPhi.a * getX() + AffineRot.negativePlaneRotPhi.b * getZ(), getY(), AffineRot.negativePlaneRotPhi.c * getX() + AffineRot.negativePlaneRotPhi.d * getZ()); //rotate to match theta
				setPos(AffineRot.negativePlaneRotTheta.a * getX() + AffineRot.negativePlaneRotTheta.b * getY(), AffineRot.negativePlaneRotTheta.c * getX() + AffineRot.negativePlaneRotTheta.d * getY()); //rotate to match phi
				
				
			}else {
				double[][] coords = {
						{getX()}, 
						{getY()},
						{getZ()},
						{0},
				};
				
				double[][] newCoords = Physics_engine_toolbox.matrixMultiply(AffineRot.affRotMatrix, coords,false);
				setPos(newCoords[0][0],newCoords[1][0],newCoords[2][0]);
			}
			
		}
		
		
		@Deprecated
		private double[] calculateRotation(double x, double y, double angle) {
			double[] polar = Vector2D.rectangularToPolar(x, y);
			return Vector2D.polarToRectangular(polar[0], polar[1] + angle);	
		}
		
		//My method using Vectors rather than AffineRotation matrices (not used because it's slow)
		@Deprecated
		public void rotate(Vector rotMagn) { //rotates about the given vector, <the length of the vector> radians
			
			Vector3D rotVector = ((Vector3D) rotMagn);
			
			//rotate to the plane of the vector ~~~~~
			rotMagsStat[0] = -rotVector.getTheta();
			rotMagsStat[1] = -rotVector.getPhi();
			
			//zRotation (theta)
			rotComps = calculateRotation(x,y,rotMagsStat[0]);
			x = rotComps[0];
			y = rotComps[1];
			
			//yRotation (phi)
			rotComps = calculateRotation(x,z,rotMagsStat[1]);
			x = rotComps[0];
			z = rotComps[1];
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			
			
			//rotate in that vector's plane
			rotComps = calculateRotation(x,y,rotVector.getR()-prevAngle);
			x = rotComps[0];
			y = rotComps[1];
			
			
			
			//rotating back to the original plane ~~~~
			rotMagsStat[0] = rotVector.getTheta();
			rotMagsStat[1] = rotVector.getPhi();
			
			//yRotation (phi)
			rotComps = calculateRotation(x,z,rotMagsStat[1]);
			x = rotComps[0];
			z = rotComps[1];
			
			//zRotation (theta)
			rotComps = calculateRotation(x,y,rotMagsStat[0]);
			x = rotComps[0];
			y = rotComps[1];
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         	prevAngle = rotVector.getR();
		}
	}
	
	public Physics_3DPolygon(Object_draw drawer, double x, double y) {
		super(drawer, x, y);
		coordinates = new Point3D(x,y,0); //Point3D so we can rotate the object about an outside point
		rotation = new Vector3D(0,0,0);
		angularVelocity = new Vector3D(0,0,0);
		angularAcceleration = new Vector3D(0,0,0);
		orbitalRotation = new Vector3D(0,0,0);
		orbitalAngularAcceleration = new Vector3D(0,0,0);
		orbitalAngularVelocity = new Vector3D(0,0,0);
		speed = new Vector3D(0,0,0);
		rotationMatrix = new AffineRotation3D();
		pointOfRotation = coordinates;
		pORCoordsTemp = new Coordinate3D(0,0,0);
	}
		
	public Physics_3DPolygon(Object_draw drawer, double x, double y, double z) {
		super(drawer, x, y);
		coordinates = new Point3D(x,y,z); //Point3D so we can rotate the object about an outside point
		rotation = new Vector3D(0,0,0);
		angularVelocity = new Vector3D(0,0,0);
		angularAcceleration = new Vector3D(0,0,0);
		orbitalRotation = new Vector3D(0,0,0);
		orbitalAngularAcceleration = new Vector3D(0,0,0);
		orbitalAngularVelocity = new Vector3D(0,0,0);
		speed = new Vector3D(0,0,0);
		rotationMatrix = new AffineRotation3D();
		setPos(x,y,z);
		pointOfRotation = coordinates;
		pORCoordsTemp = new Coordinate3D(0,0,0);
	}
	
	/**
	 * {@summary updates the object. All overridden versions of this should call super.Update()}
	 */
	@Override
	public void Update(double frames) {
		super.Update(frames);	
	}
	
	/**
	 * {@summary rotates the object a certain angle}
	 */
	@Override
	public void rotate(Vector rotation) {
		getDrawer().pause();
		((Vector3D)this.rotation).add(rotation);
		rotationMatrix.calculateRotation(rotation);
		updatePoints();
		getDrawer().resume();
	}
	
	/**
	 * {@summary a one-time rotation that will rotate all the points in the object. The angular velocity should be relative to this new rotation}
	 */
	@Override
	public void rotatePoints(Vector rotation) {
		getDrawer().pause();
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
		
		double xPofRot = pointOfRotation.getX();
		double yPofRot = pointOfRotation.getY();
		double zPofRot = 0;
		try {
			zPofRot = ((Coordinate3D) pointOfRotation).getZ();
		}catch(ClassCastException c) {c.printStackTrace();}
		
		//calculate the rotation
		try {
			rotationMatrix.calculateRotation((Vector3D) rotation);
			this.rotation.add(rotation);
		}catch(ClassCastException c) {
			Vector3D rot = new Vector3D(0,0,rotation.getR());
			rotationMatrix.calculateRotation(rot);
			this.rotation.add(rot);
		}
		
		//rotate the center point
		setPos(getX() - xPofRot, getY() - yPofRot, getZ() - zPofRot); //shift so that the pointOfRotation is the origin	
		((Point3D) getCoordinates()).rotate(rotationMatrix); //rotate the center point
		setPos(getX() + xPofRot, getY() + yPofRot, getZ() + zPofRot); //shift the ob back to its previous position
		
		//rotate the rest of the points
		updatePoints();

		getDrawer().resume(); //resume the Object_draw
	}

	/**
	 * sets the speed of the object. This must be overridden by higher dimensional objects
	 */
	@Override
	public void setSpeed(Vector newSpeed) {
		try { //vec is 3d
			speed = ((Vector3D) newSpeed);
		}catch(ClassCastException c) { //if the speed vector has too few dimensions, just use the ones we are given
			try{ //vec is 2d
				((Vector3D) getSpeed()).setSize(((Vector2D) newSpeed).getI(), ((Vector2D) newSpeed).getJ());
			}catch(ClassCastException e) {
				super.setSpeed(newSpeed); //let the super class deal with this one-dimensional vector
			}
		}
		
	}

	@Override
	public void setPos(double x, double y, double z) {
		((Coordinate3D) coordinates).setPos(x, y,z);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * {@summary add point to the points list and changes size to fit it if needed}
	 */
	public void addPoint(double x, double y, double z) {		
		addPoint(new Point3D(x,y,z));
	}
	
	@Override
	public void addPoint(PolyPoint newPoint) {
		super.addPoint(newPoint);
		if (Math.abs(((Three_dimensional) newPoint).getZ()) > getZSize()/2) setSize(getXSize(),getYSize(),Math.abs(((Three_dimensional) newPoint).getZ())*2);
	}
	
	/**
	 * {@summary add point to the points list}
	 */
	@Override
	public void addPoint(double x, double y) {
		addPoint(new Point3D(x,y,0));
	}
	
	/**
	 * {@code resets the size based on the coordinates of the points}
	 */
	@Override
	public void reCalculateSize() {
		setSize(0,0,0);
		for (PolyPoint cPoint : getPoints()) {
			//if this points coords are greater than the max coords the size permits, increase the size to accommodate this point
			if (Math.abs(cPoint.getX()) > getXSize()/2) setSize(Math.abs(cPoint.getX()*2),getYSize());
			if (Math.abs(cPoint.getY()) > getYSize()/2) setSize(getXSize(),Math.abs(cPoint.getY()*2));
			if (Math.abs(((Three_dimensional) cPoint).getZ()) > getZSize()/2) setSize(getXSize(),getYSize(),Math.abs(((Three_dimensional) cPoint).getZ())*2);
		}
	}


	/**
	 * sets the acceleration of the object. This must be overridden buy higher dimensional objects
	 */
	@Override
	public void setAcceleration(Vector newAcceleration) {
		try { //vec is 3d
			acceleration = ((Vector3D) newAcceleration);
		}catch(ClassCastException c) { //if the acceleration vector has too few dimensions, just use the ones we are given
			try{ //vec is 2d
				((Vector3D) getAcceleration()).setSize(((Vector2D) newAcceleration).getI(), ((Vector2D) newAcceleration).getJ());
			}catch(ClassCastException e) {
				super.setAcceleration(newAcceleration); //let the super class deal with this one-dimensional vector
			}
		}
	}
	
	public void setRotation(double i, double j, double k) {
		getDrawer().pause();
		rotate(rotation.statMultiply(-1));
		
		((Vector3D) rotation).setIJK(i,j,k);
		
		rotate(rotation);
		
		getDrawer().resume();
	}

	@Override
	public void setRotation(Vector newRot) {
		getDrawer().pause();
		rotate(rotation.statMultiply(-1));
		try {
			rotation = (Vector3D) newRot;
		}catch(ClassCastException c) {
			try {
				((Vector3D) rotation).setSize(((Vector2D) newRot).getI(),((Vector2D) newRot).getJ(), ((Vector3D)this.rotation).getK());
			}catch(ClassCastException e) {
				((Vector2D) rotation).setR(newRot.getR());
			}
		}
		
		rotate(rotation);
		
		getDrawer().resume();
	}
	
	
	@Override
	public void setAngularVelocity(Vector newAngV) {
		try {
			angularVelocity = (Vector3D) newAngV;
		}catch(ClassCastException c) {
			try {
				((Vector3D)angularVelocity).setIJK(((Vector2D) newAngV).getI(),((Vector2D) newAngV).getJ(),((Vector3D)angularVelocity).getK());
			}catch(ClassCastException d) {
				((Vector3D) angularVelocity).setR(newAngV.getR());
			}
		}
	}

	/**
	 * {@code add angV to rotation, add angAccel to angV, updatePoints}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	@Override
	public void setAngularAcceleration(Vector newAngAccel) {
		try {
			orbitalAngularAcceleration = (Vector3D) newAngAccel;
		}catch(ClassCastException c) {
			try {
				((Vector3D) orbitalAngularAcceleration).setIJK(((Vector2D) newAngAccel).getI(),((Vector2D) newAngAccel).getJ(),((Vector3D)orbitalAngularAcceleration).getK());
			}catch(ClassCastException d) {
				((Vector3D) orbitalAngularAcceleration).setR(newAngAccel.getR());
			}
		}
	}
	
	@Override
	public void setOrbitalRotation(Vector newRot) {
		getDrawer().pause();
		rotateAbout(orbitalRotation.statMultiply(-1),pointOfRotation);
		try {
			orbitalRotation = (Vector3D) newRot;
		}catch(ClassCastException c) {
			try {
				((Vector3D) orbitalRotation).setSize(((Vector2D) newRot).getI(),((Vector2D)newRot).getJ(), ((Vector3D)this.orbitalRotation).getK());
			}catch(ClassCastException e) {
				((Vector2D) orbitalRotation).setR(newRot.getR());
			}
		}
		
		rotateAbout(orbitalRotation,pointOfRotation);
		
		getDrawer().resume();
	}
	
	
	@Override
	public void setOrbitalAngularVelocity(Vector newAngV) {
		try {
			orbitalAngularVelocity = (Vector3D) newAngV;
		}catch(ClassCastException c) {
			try {
				((Vector3D)orbitalAngularVelocity).setIJK(((Vector2D) newAngV).getI(),((Vector2D) newAngV).getJ(),((Vector3D)orbitalAngularVelocity).getK());
			}catch(ClassCastException d) {
				((Vector3D) orbitalAngularVelocity).setR(newAngV.getR());
			}
		}
	}

	/**
	 * {@code add angV to rotation, add angAccel to angV, updatePoints}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	@Override
	public void setOrbitalAngularAcceleration(Vector newAngAccel) {
		try {
			orbitalAngularAcceleration = (Vector3D) newAngAccel;
		}catch(ClassCastException c) {
			try {
				((Vector3D) orbitalAngularAcceleration).setIJK(((Vector2D) newAngAccel).getI(),((Vector2D) newAngAccel).getJ(),((Vector3D) orbitalAngularAcceleration).getK());
			}catch(ClassCastException d) {
				((Vector3D) orbitalAngularAcceleration).setR(newAngAccel.getR());
			}
		}
	}

	@Override
	public void setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit) {
		this.rotateWithOrbit = rotateWithOrbit;
		try {
			pointOfRotation = (Coordinate3D) newPointOfRotation;
		}catch(ClassCastException c ) {
			pointOfRotation.setPos(newPointOfRotation.getX(), newPointOfRotation.getY());
		}
	}
	
	@Override
	public Coordinate3D getCoordinates() {
		return ((Coordinate3D) coordinates);
	}

	@Override
	public double getZ() {
		return ((Coordinate3D) coordinates).getZ();
	}
	
	/**
	 * {@summary sets the size of the object. This may be interpreted differently by different objects}
	 * {@code also updates maxSize}
	 */
	public void setSize(double xSize, double ySize) {
		super.setSize(xSize, ySize);
		maxSize = Math.sqrt(xSize*xSize+ySize*ySize+zSize*zSize);
	}
	
	/**
	 * {@summary sets the size of the object. This may be interpreted differently by different objects}
	 * {@code also updates maxSize}
	 */
	public void setSize(double xSize, double ySize, double zSize) {
		super.setSize(xSize,ySize);
		this.zSize = zSize;
		maxSize = Math.sqrt(xSize*xSize+ySize*ySize+zSize*zSize);
	}

	@Override
	public double getZSize() {
		return zSize;
	}
	
	
	/// Camera stuff ////////////////////////////////////////////////////////////////////////////////
	private ArrayList<CameraPaintData> cameraDataSets = new ArrayList<CameraPaintData>();
	private class CameraPaintData {
		private Camera cam;
		private Point3D center;
		private Point3D[] points;
		private int[] camPointXValues;
		private int[] camPointYValues;			
			
		public CameraPaintData(Camera cam) {
			this.cam = cam;
			center = new Point3D(0,0,0);
			points = new Point3D[0];
			camPointXValues = new int[getPoints().size()];
			camPointYValues = new int[getPoints().size()];
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
		
		
		
		double camX = cam.getCoordinates().getX(), camY = cam.getCoordinates().getY(), camZ = 0;
		
		try {
			camZ = ((Coordinate3D) cam.getCoordinates()).getZ();
		}catch(ClassCastException c) {}
		
		double offSetX = camX - cam.getFrameWidth()/2;
		double offSetY = camY - cam.getFrameHeight()/2;
		double offSetZ = camZ;
		
		Vector3D camRotation;
		
		try {
			camRotation = (Vector3D) cam.getRotation();
		}catch(ClassCastException c) {
			camRotation = new Vector3D(0,0,cam.getRotation().getR());
		}
		
	//	System.out.println(camRotation);
		
		AffineRotation3D affRot = new AffineRotation3D();
	
		affRot.calculateRotation(camRotation);
		
		data.center.setPos(getX() - offSetX - camX, getY() - offSetY - camY, getZ() - offSetZ - camZ);
		
		data.center.rotate(affRot);
		
		data.center.add(cam.getCoordinates());

		if (getPoints().size() != data.camPointXValues.length) {
			data.camPointXValues = new int[getPoints().size()];
			data.camPointYValues = new int[getPoints().size()];
		}
		
		int i;
		
		//resize data.points if it isn't the same size as the object list
		if (getPoints().size() != data.points.length) {
			data.points = new Point3D[getPoints().size()];
			
			//loop through and create a point at each index of the data.points list
			i = 0;
			Coordinate3D cPoint;
			for (PolyPoint cP : getPoints()) {
				cPoint = (Coordinate3D) cP;
				data.points[i] = new Point3D(0,0,0);
				i++;
			}
			
		}
		
		
	
		//update the data.points list with the right values and more importantly data.pointXValues and data.pointYValues
		i = 0;
		Coordinate3D cPoint;
		for (PolyPoint cP : getPoints()) {
			cPoint = (Coordinate3D) cP;
			data.points[i].setPos(cPoint.getX(), cPoint.getY(), cPoint.getZ());
			data.points[i].rotate(affRot);
			data.camPointXValues[i] = (int) (data.points[i].getX() + data.center.getX());
			data.camPointYValues[i] = (int) (data.points[i].getY() + data.center.getY());
			i++;
		}
			
	}
	
	@Override
	public void paint(Camera cam, Graphics page) {
		
		CameraPaintData data = getCameraPaintData(cam);

	
		if (getIsFilled()) {
			page.fillPolygon(data.camPointXValues, data.camPointYValues, data.camPointXValues.length);
		}else {
			page.drawPolygon(data.camPointXValues, data.camPointYValues, data.camPointXValues.length);
		}
	}	
	
	////////////////////////// end camera stuff

}
