package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Physics_3DTexturedPolygon extends Physics_3DPolygon implements Textured3D, Updatable {
	
	protected class P3DPTexture {
		private int width;
		private int height;
		BufferedImage image;
		
		
		private P3DPTexture(String fileName) {
			File img_file = new File(fileName);
			try {
				image = ImageIO.read(img_file);
			} catch (IOException e) {
				getDrawer().out.println("could not read img file:" + fileName);
				if (Settings.JOptionPaneErrorMessages) JOptionPane.showMessageDialog(getDrawer(), "could not read img file:" + fileName);
				e.printStackTrace();
			}
			
	        width = image.getWidth();
	        height = image.getHeight();
	        

		}
		/** 
		 * @param x
		 * @param y
		 * @return the rgb value of the passed coords in the image
		 * {@summary returns the rgb value at a position in an image}
		 */
		 public RGBPoint3D getRGBPoint(double x, double y, double z,double theta, double phi, boolean threeD) {
			 RGBPoint3D point = new RGBPoint3D(x,y,z,0,0,0);
			 double divisor = 1;
			
			
			 int pixel;
			 
			 try { 
				 if (threeD) {
					 pixel = image.getRGB( (int) Math.round((theta) * width/2/Math.PI/divisor ),(int) Math.round(phi * height/Math.PI/divisor) );
				 }else {
					 pixel = image.getRGB((int) Math.round(x * width/getXSize()/divisor + width/2 ) ,(int) Math.round(y *height/getYSize() /divisor + height/2));
				 }
			 }catch(ArrayIndexOutOfBoundsException a) {
				 pixel = image.getRGB(0,0);
				 System.out.println(x + "," + y + "," + z);
				 getDrawer().out.println("texture does not fit on object");
				 getDrawer().out.println(a);
			 }
		 	 
			 int alpha = (pixel >> 24) & 0xff;
			 int red = (pixel >> 16) & 0xff;
			 int green = (pixel >> 8) & 0xff;
			 int blue = (pixel) & 0xff;
			 point.setRGB(red, green, blue, alpha);
			 
			 return point;
		  }
	}		 

	protected class FramePoint extends Point3D {
		private double theta, phi, r;
		private Color color = Color.black;
		public boolean isFound = false;
		public FramePoint(double x, double y, double z) {
			super(x, y, z);
			Vector3D initPosVec = new Vector3D(x,y,z);
			theta = initPosVec.getTheta();
			phi = initPosVec.getPhi();
			r = Math.sqrt(x*x + y*y + z*z);
		}
	}
	
	protected class RGBPoint3D extends Point3D {
		int R,G,B,alpha;
		
		protected RGBPoint3D(double x, double y, double z) {
			super(x,y,z);
		}
		
		protected RGBPoint3D(double x, double y, double z, int R, int G, int B) {
			super(x, y, z);
			this.R = R;
			this.G = G;
			this.B = B;
		}
		
		private void setRGB(int r, int g, int b, int alpha) {
			R = r;
			G = g;
			B = b;
			this.alpha = alpha;
		}
		
	}
	
	private P3DPTexture Texture;
	private double platePointSize;
	private LinkedList<RGBPoint3D> platePoints;
	protected double dPhi= 0.001;
	protected double dTheta = 0.001;
	public boolean paintInProgress = false;
	protected int collisionCheckGain = 5;
	private double minZToPaintPoints = -Settings.distanceFromScreen;
	
	public Physics_3DTexturedPolygon(Object_draw drawer, double x, double y, double z, double ppSize) {
		super(drawer, x, y, z);
		this.platePointSize = ppSize;
		setPlatePoints(new LinkedList<RGBPoint3D>());
	}
	
	private void updatePlatePoints() {
		for (PolyPoint cPoint : getPlatePoints()) {
			cPoint.rotate(rotationMatrix);
		}
		
		
	}
	
	@Override
	public void prePaintUpdate() {
		super.prePaintUpdate();
		getPlatePoints().sort(new Comparator<Point3D>() {
			@Override
			public int compare(Point3D o1, Point3D o2) {
				return (Double.compare(o2.getZ(), o1.getZ()));
			}
		});
	}
		
	@Override
	public void updatePoints() {
		super.updatePoints();
		updatePlatePoints();
	}
	
	/**
	 * 
	 * @param r the radius we are at
	 * @param dRdTheta
	 * @param dRdPhi
	 * {@summary calculates how much the texture loader should increment theta and phi in the next loop}
	 */
	protected double[] calculateAngleSteps(double r,double dRdTheta, double dRdPhi) {
		double rNewTheta = dTheta * dRdTheta + r;
		double newDTheta = Math.acos( (r*r+rNewTheta*rNewTheta - platePointSize*platePointSize) / (2*r*rNewTheta));
		
	
		if ((! Double.isNaN(newDTheta))) {
			if ( Math.abs(newDTheta-dTheta) > dTheta*0.6) {
				dTheta = newDTheta*2 + dTheta;
				dTheta /= 3;
			}else {
				dTheta = newDTheta;
			}
		}
		
		double rNewPhi = dPhi * dRdPhi + r;
		double newDPhi = Math.acos( (r*r+rNewPhi*rNewPhi - platePointSize*platePointSize) / (2*r*rNewPhi));
		
		if ((! Double.isNaN(newDPhi))) {
			if ( Math.abs(newDPhi-dPhi) > dPhi*0.6){
				dPhi = newDPhi * 2 + dPhi;
				dPhi /= 3;
			}else {
				dPhi = newDPhi;
			}
		}

		return new double[] {newDTheta, newDPhi};
		
	}
	
	/**
	 * {@summary calculated point-values at the given theta and phi}
	 * @param theta
	 * @param phi
	 * @return {x, y, r, dtheta / dr , dphi / dr}
	 */
	protected double[] calculatePointValues(double theta, double phi) {
		double r;
		
		//calculate the closest points
		FramePoint pt1 = (FramePoint) getPoints().get(0), pt2 = (FramePoint) getPoints().get(1), pp1 = (FramePoint) getPoints().get(2),pp2 = (FramePoint) getPoints().get(3);
		FramePoint cPoint;
		double angDist=0,angDt1=343000000, angDt2=343000000,angDp1=343000000, angDp2=343000000; 
		for (PolyPoint cP : getPoints()) {
			cPoint = (FramePoint) cP;
			angDist = Math.abs(cPoint.theta-theta) + Math.abs(phi-cPoint.phi);
			
			if ( (angDist < angDt1)  && (cPoint.theta < theta) ) {
				if ( (cPoint != pt2) && (cPoint != pp1) && (cPoint != pp2) ) { //make sure this point hasn't already been used
					pt1 = cPoint;
					angDt1 = angDist;
				}
			}else if ( (angDist < angDt2) && (cPoint.theta > theta) ) {
				if ( (cPoint != pt1) && (cPoint != pp1) && (cPoint != pp2) ) { //make sure this point hasn't already been used
					pt2 = cPoint;
					angDt2 = angDist;
				}
			}else if ( (angDist < angDp1) && (cPoint.phi < phi) ) {
				if ( (cPoint != pt1) && (cPoint != pt2) && (cPoint != pp2) ) { //make sure this point hasn't already been used
					pp1 = cPoint;
					angDp1 = angDist;
				}
			}else if ( (angDist < angDp2) && (cPoint.phi > phi) ) {
				if ( (cPoint != pt1) && (cPoint != pt2) && (cPoint != pp1) ) { //make sure this point hasn't already been used
					pp2 = cPoint;
					angDp2 = angDist;
				}
			}

		}
		
		//calculate change in theta, phi, and r for our two pairs of points
		double dthetaT = pt1.theta - pt2.theta;
		double dphiT = pt1.phi - pt2.phi;
		double drT = pt1.r - pt2.r;
		
		double dthetaP = pp1.theta - pp2.theta;
		double dphiP = pp1.phi - pp2.phi;
		double drP = pp1.r - pp2.r;
		

		//solve for dTheta/dr and dPhi/dr
		double[] DthetaAndDPhi = Physics_engine_toolbox.solveLinearSystem(dthetaT, dphiT, drT, dthetaP, dphiP, drP);
		
		
		r = pt1.r + (theta-pt1.theta)*DthetaAndDPhi[0] + (phi-pt1.phi)*DthetaAndDPhi[1];
	
		if (paintInProgress) {
			r = Math.abs(r); 
			pt1.color = Color.PINK;
			pt2.color = Color.PINK;
			pp1.color = Color.PINK;
			pp2.color = Color.PINK;
			getDrawer().repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			pt1.color = Color.BLACK;
			pt2.color = Color.BLACK;
			pp1.color = Color.BLACK;
			pp2.color = Color.BLACK;
		}
			
		theta += Math.PI;
		
		return new double[] {r * Math.cos(theta) * Math.sin(phi),  r * Math.sin(theta) * Math.sin(phi), r * Math.cos(phi), r,DthetaAndDPhi[0],DthetaAndDPhi[1]};
	}
	
	/**
	 * 
	 * @param theta
	 * @param phi
	 * @return an estimate of the radius of this object at the given theta and phi angles
	 */
	private double[] addNewPlatePoint(double theta, double phi) {
		
		double[] pointVals = calculatePointValues(theta,phi);
		
		double x = pointVals[0];
		double y = pointVals[1];
		double z = pointVals[2];
		double r = pointVals[3];
		double dthetadr = pointVals[4];
		double dphidr = pointVals[5];
		
		RGBPoint3D platePoint = null;
		
		
		platePoint = Texture.getRGBPoint(x, y, z,theta,phi,true);
		getPlatePoints().add(platePoint);
		
		
		
		//calculate how much to increment the angles
		return calculateAngleSteps(r,dthetadr,dphidr);
	}
	
	
	
	
	/**
	 * {@summary sets the texture of the object. Note that the texture cannot be a .jpeg file}
	 */
	@Override
	public void setTexture(String fileName) {
	
		getDrawer().pause(); //pause the drawer so we can not get ConcModExceptions
		
		getDrawer().out.println("setting texture");
		
		if (getPoints().size() == 0) {
			Exception e = new Exception("Physics_TexturePolygon must have points for a texture to be added");
			e.printStackTrace();
			return;
		}else if (getPoints().size() <= 4) {
			getDrawer().out.println("not enough points in Physics_3DTexturePolygon for it to be a 3D shape... reverting to 2D version");
			setTexture2D(fileName);
			return;
		}
		
		//set the rotation to zero so that we are applying the texture correctly
		setRotation(new Vector3D(0,0,0));
		super.Update(0.001);	
		
		Texture = new P3DPTexture(fileName);
		
		// remove old plate points
		getPlatePoints().clear();
		
	
		dTheta = 0.000001;
		dPhi = 0.000001;
		
		//load the texture
		for (double phi = Math.PI/2; phi <= Math.PI; phi += dPhi) {
			for (double theta = 0; theta < 2*Math.PI; theta += dTheta) {
				try {
					addNewPlatePoint(theta,phi);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("stage 1 out of 2 complete");
	 
		dTheta = 0.001;
		dPhi = 0.001;
		
		for (double phi = Math.PI/2; phi >= 0; phi -= dPhi) {
			for (double theta = 0; theta < 2*Math.PI; theta += dTheta) {
				try {
					addNewPlatePoint(theta,phi);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		

		
		
		getDrawer().out.println("Setting of texture complete.");
		getDrawer().out.println("Added " + getPlatePoints().size() + " platePoints.");
		getDrawer().resume(); //resume the drawer
		
	}
	
	@Override
	public void addPoint(double x, double y, double z) {
		addPoint(new FramePoint(x,y,z));
	}
	
	@Override
	public void addPoint(double x, double y) {
		addPoint(x,y,0);
	}
	
	public LinkedList<RGBPoint3D> getPlatePoints() {
		return platePoints;
	}

	public void setPlatePoints(LinkedList<RGBPoint3D> platePoints) {
		this.platePoints = platePoints;
	}
	
	/**
	 * @param newMinZ the minimum z value to show points on the screen 
	 */
	public void setMinZToPaintPoints(double newMinZ) {
		minZToPaintPoints = newMinZ;
	}

	@Override
	public void paint(Graphics page) {
		
 		if (paintInProgress) {
			FramePoint ccPoint;
			for (PolyPoint cP : getPoints()) {
				ccPoint = (FramePoint) cP;
				page.setColor(ccPoint.color);
				page.drawRect((int) ( getX() + ccPoint.getX()),(int) (getY() + ccPoint.getY()), 3,3);
			}
	 	}

 		
 		double parallaxValue, x, y;
		for (RGBPoint3D cPoint : getPlatePoints()) {
			
			if (cPoint.getZ() + getZ() >= minZToPaintPoints) {
				page.setColor(new Color(cPoint.R,cPoint.G,cPoint.B,cPoint.alpha));
				
				if (Settings.perspective) {
					
					
					x = getX() + cPoint.getX()-platePointSize/2 - Settings.width/2;
					y = getY() + cPoint.getY()-platePointSize/2 - Settings.height/2;
					
					//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
					if (cPoint.getZ() == 0) {
						parallaxValue = 1;
					}else {
						parallaxValue = (Settings.distanceFromScreen) / ((getZ() + cPoint.getZ()) + Settings.distanceFromScreen);
					}
					
					
					
					x *= parallaxValue;
					y *= parallaxValue;
					
					x += Settings.width/2;
					y += Settings.height/2;
					
					page.fillRect((int) Math.round(x - platePointSize*parallaxValue/2 - 1) ,(int) Math.round(y - platePointSize*parallaxValue/2 - 1) , (int) (platePointSize * parallaxValue + 2), (int) (platePointSize * parallaxValue + 2));
					
				}else {
					page.fillRect((int) Math.round(getX() + cPoint.getX()-platePointSize/2 - 1) ,(int) Math.round(getY() + cPoint.getY()-platePointSize/2 - 1) , (int) (platePointSize+2), (int) (platePointSize+2));
				}
			}
		}
		
	}
	

	
	/**
	 * {@summary sets the texture of the object, but this version makes the object a 2d sheet rather than a 3D wrapping. Note that the texture cannot be a .jpeg file}
	 */
	public void setTexture2D(String fileName) {
		
		if (getPoints().size() == 0) {
			Exception e = new Exception("Physics_TexturePolygon must have points for a texture to be added");
			e.printStackTrace();
		}
		
		Texture = new P3DPTexture(fileName);

		
		// remove old plate points
		getPlatePoints().clear();
		

		//un-find all frame points
		try {
			FramePoint cPoint;
			for (PolyPoint cP : getPoints()) {
				cPoint = (FramePoint) cP;
				cPoint.isFound = false;
			}
		}catch(ClassCastException c) {
			getDrawer().out.println("Non-framePoint in points list (Physics_2DTexturedPolygon) Name: " + getName());
			c.printStackTrace();
		}
		
		super.Update(0.001);		
		Polygon framePoly = new Polygon(pointXValues,pointYValues,numPoints);
		RGBPoint3D newPoint;
		int spiralNum = -2,spiralSize=1,direction = 0, pointsOut = 0;
		double x = 0, y = 0, xInc = 0, yInc = platePointSize;
		while (pointsOut <= (getXSize() * getYSize())/platePointSize ) {
			
			for (int pointNum = 0; pointNum < spiralSize; pointNum++) {
				x += xInc;
				y += yInc;
				
				
				//if our spiral guess was inside our polygon, add the plate point to the shape
				if (framePoly.contains(getX()+x,getY()+y)) {
					try {
						newPoint = Texture.getRGBPoint(x,y,0,0,0,false);
						getPlatePoints().add(newPoint);
					}catch(ArrayIndexOutOfBoundsException a) {
						System.out.println("array out of bounds");
					}
				}else {				
					pointsOut++;				
				}

			}
			spiralNum++;
			direction++;

			//size of spiral
			if (spiralNum % 2 == 1) spiralSize++;
			
			//direction
			if (direction % 4 == 0) {
				xInc = 0;
				yInc = platePointSize;
			}else if (direction % 4 == 1) {
				xInc = -platePointSize;
				yInc = 0;
			}else if (direction % 4 == 2) {
				xInc = 0;
				yInc = -platePointSize;
			}else if (direction % 4 == 3) {
				xInc = platePointSize;
				yInc = 0;
			}
		}
		
		getDrawer().out.println("Setting of texture complete.");
	}
	
	/** 
	 * @param gain the larger the gain the less accurate it is
	 **/
	public void setCollisionCheckingAccuracy(int gain) {
		if (gain > 0) {
			collisionCheckGain = 1;
		}else {
			Exception e = new Exception("collisionGain must be an integer greater than 0");
			e.printStackTrace();
		}
	}
	
	
	// collision methods (must implement Tangible in child class to use them)

	public void collision(CollisionEvent e) {
		// TODO Implement this
		System.out.println(e.objectHit.getName() + " has run into " + getName());
	}
	
	public CollisionEvent getCollisionEvent(Tangible o2, Coordinate2D pointOfCollision) {
		Vector relativeSpeed;
		try { 
			relativeSpeed = Vector3D.subtract((Vector3D) o2.getSpeed(),(Vector3D) getSpeed());
		}catch(ClassCastException DDD) {
			try {
				relativeSpeed = Vector2D.subtract((Vector2D) o2.getSpeed(),(Vector2D) getSpeed());
			}catch(ClassCastException DD) {
				relativeSpeed = Vector.subtract(o2.getSpeed(),getSpeed());
			}
		}
		
		Vector2D normalVec;
	
		try {
			
			normalVec = new Vector3D(getCoordinates(), (Coordinate3D) pointOfCollision);
		}catch(ClassCastException c) { //if the point was 2D, just set it at zPos 0 and carry on
			normalVec = new Vector2D(getCoordinates(),pointOfCollision);
		}
		
		
		
		
		return new CollisionEvent(pointOfCollision, normalVec,(Tangible) this, relativeSpeed);
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
		
		
		//checking if the point is within the collision area of any of these object's points
		int pointCounter = 10;
		for (Coordinate3D cPoint : getPlatePoints()) {
			if (pointCounter % collisionCheckGain == 0) { //only check every [collisionCheckGain] points
				if (Physics_engine_toolbox.objectRelativePointDistance3D(obX, obY, obZ, point3D, getX(), getY(), getZ(), cPoint) <= radius + platePointSize * collisionCheckGain/2) {
					return true;
				}
				
			}
			pointCounter++;
		}
		
	
		return false;
	}
	
	
	/**
	 * {@summary checks for a collision between this and an object}
	 * @param object the object to check if this one is collided with
	 * @return the point of contact or null if the objects are not collided
	 */
	public Coordinate3D checkForCollision(Tangible object) {
		int pointCounter = 10;
		for (Coordinate3D cPoint : getPlatePoints()) {
			if (pointCounter % collisionCheckGain == 0) { //only check every [collisionCheckGain] points
				if (object.checkForCollision(cPoint, (Tangible) this, platePointSize * collisionCheckGain/2)) {
					return cPoint;
				}
			
			}
			pointCounter++;
		}
		return null;
	}
	



}
