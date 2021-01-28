package apgraphicslib;

import java.awt.Graphics;
import java.util.ArrayList;

public class Physics_3DPolygon extends Physics_2DPolygon implements Three_dimensional, Rotatable {
	
	private double zSize;
	
	
	/**
	 * {@code a 3D point designed for a Physics_3DPolygon. Can rotate itself}
	 * @author samiam567
	 *
	 */
	protected static class Point3D extends Coordinate3D implements PolyPoint {
		//multi-purpose lists for rotation
		private double[] rotMagsStat = new double[3];
		private double[] rotComps = new double[2];
		
		private double prevAngle = 0;
		public Point3D(double x, double y, double z) {
			super(x, y,z);
			
		}	
	
	
		/**
		 * {@summary rotates this point in accordance to the passed AffineRotation}
		 * {@code Note: Passed AffineRotation must be a AffineRotation3D}
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
		additonalAngularVelocitiesVector = new Vector3D();
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
		additonalAngularVelocitiesVector = new Vector3D();
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
		if (Coordinate3D.class.isAssignableFrom(pointOfRotation.getClass())) {
			zPofRot = ((Coordinate3D) pointOfRotation).getZ();
		}else{
			ClassCastException c = new ClassCastException("point of rotation is not a coordinate3D");
			c.printStackTrace(getDrawer().out);
		}
		
		//calculate the rotation
		if (Vector3D.class.isAssignableFrom(rotation.getClass())) {
			rotationMatrix.calculateRotation((Vector3D) rotation);
			this.rotation.add(rotation);
		}else{
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
		if (Vector3D.class.isAssignableFrom(newSpeed.getClass())) { //vec is 3d
			speed = ((Vector3D) newSpeed);
		}else if (Vector2D.class.isAssignableFrom(newSpeed.getClass())) {//vec is 2d   
			//if the speed vector has too few dimensions, just use the ones we are given
			((Vector3D) getSpeed()).setSize(((Vector2D) newSpeed).getI(), ((Vector2D) newSpeed).getJ());
		}else{
			super.setSpeed(newSpeed); //let the super class deal with this one-dimensional vector
		}
		
	}

	@Override
	public void setPos(double x, double y, double z) {
		((Coordinate3D) coordinates).setPos(x, y,z);
	}
	
	/**
	 * {@summary uniformly translates all the points in the object so that the engine center of the object is roughly in line with the Polygon's estimated center of mass}
	 */
	public void centerPoints() {
		Coordinate3D com = new Coordinate3D(0,0,0); 
		Coordinate3D cPoint;
		for (PolyPoint cP : getPoints() ) {
			cPoint = (Coordinate3D) cP;
			com.add(cPoint);
		}
		
		com.setPos(com.getX() / getPoints().size(),com.getY() / getPoints().size(),com.getZ() / getPoints().size());
		
		for (PolyPoint cP : getPoints() ) {
			cPoint = (Point3D) cP;
			cPoint.setPos(cPoint.getX() - com.getX(),cPoint.getY() - com.getY(), cPoint.getZ() - com.getZ());
		}

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
		if (Vector3D.class.isAssignableFrom(newAcceleration.getClass())) { //vec is 3d
			acceleration = ((Vector3D) newAcceleration);
		}else if (Vector2D.class.isAssignableFrom(newAcceleration.getClass())) { //vec is 2d	
			 //if the acceleration vector has too few dimensions, just use the ones we are given
			((Vector3D) getAcceleration()).setSize(((Vector2D) newAcceleration).getI(), ((Vector2D) newAcceleration).getJ());
		}else{
			super.setAcceleration(newAcceleration); //let the super class deal with this one-dimensional vector
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
		if (Vector3D.class.isAssignableFrom(newRot.getClass())) {
			rotation = (Vector3D) newRot;
		}else if (Vector2D.class.isAssignableFrom(newRot.getClass())) {
			((Vector3D) rotation).setSize(((Vector2D) newRot).getI(),((Vector2D) newRot).getJ(), ((Vector3D)rotation).getK());
		}else{
			((Vector2D) rotation).setR(newRot.getR());
		}
		
		rotate(rotation);
		
		getDrawer().resume();
	}
	
	
	@Override
	public void setAngularVelocity(Vector newAngV) {
		if (Vector3D.class.isAssignableFrom(newAngV.getClass())) {
			angularVelocity = (Vector3D) newAngV;
		}else if (Vector2D.class.isAssignableFrom(newAngV.getClass())) {
			((Vector3D)angularVelocity).setIJK(((Vector2D) newAngV).getI(),((Vector2D) newAngV).getJ(),((Vector3D)angularVelocity).getK());
		}else{
			((Vector3D) angularVelocity).setR(newAngV.getR());
		}
	}

	/**
	 * {@code add angV to rotation, add angAccel to angV, updatePoints}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	@Override
	public void setAngularAcceleration(Vector newAngAccel) {
		if (Vector3D.class.isAssignableFrom(newAngAccel.getClass())) {
			angularAcceleration = (Vector3D) newAngAccel;
		}else if (Vector2D.class.isAssignableFrom(newAngAccel.getClass())){
			((Vector3D) angularAcceleration).setIJK(((Vector2D) newAngAccel).getI(),((Vector2D) newAngAccel).getJ(),((Vector3D)orbitalAngularAcceleration).getK());
		}else{
			((Vector3D) angularAcceleration).setR(newAngAccel.getR());
		}
	}
	
	@Override
	public void setOrbitalRotation(Vector newRot) {
		getDrawer().pause();
		rotateAbout(orbitalRotation.statMultiply(-1),pointOfRotation);
		
		if (Vector3D.class.isAssignableFrom(newRot.getClass())) {
			orbitalRotation = (Vector3D) newRot;
		}else if (Vector2D.class.isAssignableFrom(newRot.getClass())) {
			((Vector3D) orbitalRotation).setSize(((Vector2D) newRot).getI(),((Vector2D)newRot).getJ(), ((Vector3D)this.orbitalRotation).getK());
		}else{
			((Vector2D) orbitalRotation).setR(newRot.getR());
		}
		
		rotateAbout(orbitalRotation,pointOfRotation);
		
		getDrawer().resume();
	}
	
	
	@Override
	public void setOrbitalAngularVelocity(Vector newAngV) {
		if (Vector3D.class.isAssignableFrom(newAngV.getClass())) {
			orbitalAngularVelocity = (Vector3D) newAngV;
		}else if (Vector2D.class.isAssignableFrom(newAngV.getClass())) {
			((Vector3D)orbitalAngularVelocity).setIJK(((Vector2D) newAngV).getI(),((Vector2D) newAngV).getJ(),((Vector3D)orbitalAngularVelocity).getK());
		}else{
			((Vector3D) orbitalAngularVelocity).setR(newAngV.getR());
		}
	}

	/**
	 * {@code add angV to rotation, add angAccel to angV, updatePoints}
	 * {@code this should never have to be overridden by higher dimensional polygons}
	 */
	@Override
	public void setOrbitalAngularAcceleration(Vector newAngAccel) {
		if (Vector3D.class.isAssignableFrom(newAngAccel.getClass())) {
			orbitalAngularAcceleration = (Vector3D) newAngAccel;
		}else if (Vector2D.class.isAssignableFrom(newAngAccel.getClass())) {
			((Vector3D) orbitalAngularAcceleration).setIJK(((Vector2D) newAngAccel).getI(),((Vector2D) newAngAccel).getJ(),((Vector3D) orbitalAngularAcceleration).getK());
		}else{
			((Vector3D) orbitalAngularAcceleration).setR(newAngAccel.getR());
		}
	}

	@Override
	public void setPointOfRotation(Coordinate2D newPointOfRotation, boolean rotateWithOrbit) {
		this.rotateWithOrbit = rotateWithOrbit;
		if (Coordinate3D.class.isAssignableFrom(newPointOfRotation.getClass())) {
			pointOfRotation = (Coordinate3D) newPointOfRotation;
		}else{
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
		
		if (Coordinate3D.class.isAssignableFrom(cam.getCoordinates().getClass())) {
			camZ = ((Coordinate3D) cam.getCoordinates()).getZ();
		}
		
		
		Vector3D camRotation;
		
		if (Vector3D.class.isAssignableFrom(cam.getRotation().getClass())) {
			camRotation = (Vector3D) cam.getRotation();
		}else{
			camRotation = new Vector3D(0,0,cam.getRotation().getR());
		}
		
		
		AffineRotation3D affRot = new AffineRotation3D();
	
		affRot.calculateRotation(camRotation);
		
		data.center.setPos(getX() - camX, getY() - camY, getZ() - camZ);
		
		data.center.rotate(affRot);
		
		data.center.setPos(data.center.getX() + cam.getFrameWidth()/2,data.center.getY() + cam.getFrameHeight()/2,data.center.getZ());
		

		if (getPoints().size() != data.camPointXValues.length) {
			data.camPointXValues = new int[getPoints().size()];
			data.camPointYValues = new int[getPoints().size()];
		}
		
		int i;
		
		//resize data.points if it isn't the same size as the object list
		if (getPoints().size() != data.points.length) {
			data.points = new Point3D[getPoints().size()];
			
			//loop through and create a point at each index of the data.points list
		
			for (i = 0; i < getPoints().size(); i++) {
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
