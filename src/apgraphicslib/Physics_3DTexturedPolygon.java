package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import apgraphicslib.Physics_2DPolygon.PolyPoint;
import apgraphicslib.Physics_3DPolygon.Point3D;
import calculator_parser_solver.Equation;

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
	private boolean isTangible = true, showBorder = false;
	
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
		

		//solve for dr/dTheta and dr/dPhi
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
	 * {@summary uniformly translates all the points in the object so that the engine center of the object is roughly in line with the Polygon's estimated center of mass}
	 * {@code This must be called AFTER setTexture() if you want platePoints to be centered (which you probably do)}
	 */
	@Override
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
		
		for (PolyPoint cP : platePoints ) {
			cPoint = (Point3D) cP;
			cPoint.setPos(cPoint.getX() - com.getX(),cPoint.getY() - com.getY(), cPoint.getZ() - com.getZ());
		}
		

	}
	
	/**
	 * {@summary sets the texture of the object. Note that the texture cannot be a .jpeg file}
	 */
	@Override
	public void setTexture(String fileName) {
	
		getDrawer().pause(); //pause the drawer so we don't get ConcModExceptions
		
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

	
	/// Camera stuff ////////////////////////////////////////////////////////////////////////////////
		private ArrayList<CameraPaintData> cameraDataSets = new ArrayList<CameraPaintData>();
		private class CameraPaintData {
			private Camera cam;
			private Point3D center;
			private ArrayList<RGBPoint3D> platePoints;
			private ArrayList<RGBPoint3D> secondaryPlatePoints;
			private boolean usingPrimaryData = true;
			private boolean sorted = true;

			public CameraPaintData(Camera cam) {
				this.cam = cam;
				center = new Point3D(0,0,0);
				platePoints = new ArrayList<RGBPoint3D>(0);
				secondaryPlatePoints = new ArrayList<RGBPoint3D>(0);				
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
			if (showBorder) super.createCameraPaintData(cam);
			
			cameraDataSets.add(new CameraPaintData(cam));
		}
		
		@Override
		public void deleteCameraPaintData(Camera cam) {
			if (showBorder) super.deleteCameraPaintData(cam);
			
			cameraDataSets.remove(getCameraPaintData(cam));
		}
		
		
		public void updateCameraPaintData(Camera cam) {	
			
			if (showBorder) super.updateCameraPaintData(cam);
			
			CameraPaintData data = getCameraPaintData(cam);

			ArrayList<RGBPoint3D> dataPointSet;
			
			//alternate which data to use
			data.usingPrimaryData = ! data.usingPrimaryData;
			
			
			if (data.usingPrimaryData) {
				dataPointSet = data.platePoints;
			}else {
				dataPointSet = data.secondaryPlatePoints;
			}
			
		
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
			
			//we are about to change the data so it is not currently sorted
			data.sorted  = false;
			
			data.center.setPos(getX() - camX, getY() - camY, getZ() - camZ);
			
			data.center.rotate(affRot);
			
			data.center.setPos(data.center.getX() + cam.getFrameWidth()/2,data.center.getY() + cam.getFrameHeight()/2,data.center.getZ());
			
			int i;
			
			//resize data.points if it isn't the same size as the object list
			if (platePoints.size() != data.platePoints.size()) {
				data.platePoints.clear();
				
				//loop through and create a point at each index of the data.points list
				for (i = 0; i < getPlatePoints().size(); i++) {
					data.platePoints.add(new RGBPoint3D(0,0,0,0,0,0));
				}
				
			}
			
			//resize data.points if it isn't the same size as the object list
			if (platePoints.size() != data.secondaryPlatePoints.size()) {
				data.secondaryPlatePoints.clear();
				
				//loop through and create a point at each index of the data.points list
				for (i = 0; i < getPlatePoints().size(); i++) {
					data.secondaryPlatePoints.add(new RGBPoint3D(0,0,0,0,0,0));
				}
				
			}
			
			
		
			//update the data.points list with the right values and more importantly data.pointXValues and data.pointYValues		
			double parallaxValue, x, y;
			RGBPoint3D cPoint;
			i = 0;
			for (RGBPoint3D obPoint : platePoints) {
				cPoint = dataPointSet.get(i);
				cPoint.setRGB(obPoint.R, obPoint.G, obPoint.B, obPoint.alpha);
				cPoint.setPos(obPoint.getX(), obPoint.getY(), obPoint.getZ());
				cPoint.rotate(affRot);
				
				
				
				if (cam.perspective) {
					
				
					x = data.center.getX() + cPoint.getX()-platePointSize/2 - cam.getFrame().getWidth()/2;
					y = data.center.getY() + cPoint.getY()-platePointSize/2 - cam.getFrame().getHeight()/2;
					
					//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
					if (cPoint.getZ() + data.center.getZ() == 0) {
						parallaxValue = 1;
					}else {
						parallaxValue = (Settings.distanceFromScreen) / ((data.center.getZ() + cPoint.getZ()) + Settings.distanceFromScreen);
					}
					
					
					
					x *= parallaxValue;
					y *= parallaxValue;
					
					x += cam.getFrame().getWidth()/2;
					y += cam.getFrame().getHeight()/2;
					
					cPoint.setPos(Math.round(x - platePointSize*parallaxValue/2 ) , Math.round(y - platePointSize*parallaxValue/2 ),cPoint.getZ());
					
					
				}else {
					cPoint.add(data.center);
				}
				
				i++;
			}
			
			
		
			//sort the dataPointSet by z pos
			dataPointSet.sort(new Comparator<Point3D>() {
				@Override
				public int compare(Point3D o2, Point3D o1) {
					return (Double.compare(o1.getZ(), o2.getZ()));
				}
			});
			
		
			data.sorted = true;
			
		}
		
		@Override
		public void paint(Camera cam, Graphics page) {
			
			if (showBorder) {
				super.paint(cam,page);
			}
			
			
			CameraPaintData data = getCameraPaintData(cam);
			
			ArrayList<RGBPoint3D> dataPointSet;
			
			
			//if the data is sorted, use the data most recently updated, if not use the other dataSet (which is sorted)
			if (data.sorted) {
				if (data.usingPrimaryData) {
					dataPointSet = data.platePoints;
				}else {
					dataPointSet = data.secondaryPlatePoints;
				}
			}else {
				if (data.usingPrimaryData) {
					dataPointSet = data.secondaryPlatePoints;
				}else {
					dataPointSet = data.platePoints;
				}
			}
			
			
			
			double parallaxValue;

	 		for (RGBPoint3D cPoint : dataPointSet) {
	 			
	 			if (cPoint.getZ() + getZ()  >= minZToPaintPoints + ((Three_dimensional) cam.getCoordinates()).getZ()) {
		 			if (cam.perspective) {
						//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
						if (((data.center.getZ() + cPoint.getZ()) + -minZToPaintPoints) == 0) {
							parallaxValue = 1;
						}else {
							parallaxValue = (-minZToPaintPoints) / ((data.center.getZ() + cPoint.getZ()) + -minZToPaintPoints);
						}
					}else {
						parallaxValue = 1;
					}
				
				
					page.setColor(new Color(cPoint.R,cPoint.G,cPoint.B,cPoint.alpha));
					
					page.fillRect((int) cPoint.getX() - 1, (int) cPoint.getY() - 1,(int) (platePointSize*parallaxValue) + 2,(int) (platePointSize*parallaxValue) + 2);
				}
			}
	 			 		
		}	
		
		////////////////////////// end camera stuff
		
	
	@Override
	public void paint(Graphics page) {
		
		if (showBorder) {
			super.paint(page);
		}
		
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
					
					
					x = getX() + cPoint.getX()-platePointSize/2 - getDrawer().getFrameWidth()/2;
					y = getY() + cPoint.getY()-platePointSize/2 - getDrawer().getFrameHeight()/2;
					
					//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
					if (cPoint.getZ() + getZ() == 0) {
						parallaxValue = 1;
					}else {
						parallaxValue = (Settings.distanceFromScreen) / ((getZ() + cPoint.getZ()) + Settings.distanceFromScreen);
					}
					
					
					
					x *= parallaxValue;
					y *= parallaxValue;
					
					x += getDrawer().getFrameWidth()/2;
					y += getDrawer().getFrameHeight()/2;
					
					page.fillRect((int) Math.round(x - platePointSize*parallaxValue/2 - 1) ,(int) Math.round(y - platePointSize*parallaxValue/2 - 1) , (int) (platePointSize * parallaxValue + 2), (int) (platePointSize * parallaxValue + 2));
					
				}else {
					page.fillRect((int) Math.round(getX() + cPoint.getX()-platePointSize/2 - 1) ,(int) Math.round(getY() + cPoint.getY()-platePointSize/2 - 1) , (int) (platePointSize+2), (int) (platePointSize+2));
				}
			}
		}
		
	}
	

	
	/**
	 * {@summary sets the texture of the object, but this version makes the object a 2d sheet rather than a 3D wrapping. Note that the texture cannot be a .jpeg file}
	 * @param fileName the filePath to the texture to use
	 * @param zEq the Equation in terms of up to x and y to use for the z values of the points
 	 */
	public void setTexture2D(String fileName, Equation zEq) {
		
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
						zEq.setVariableValue("x", x);
						zEq.setVariableValue("y", y);
						newPoint = Texture.getRGBPoint(x,y,zEq.solve(),0,0,false);
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
	
	public void setTexture2D(String fileName) {
		setTexture2D(fileName, new Equation("0"));
	}
	
	/**
	 * {@summary finds the closest point to the passed one with the distance being calculated in accordance to the passed equation}
	 * {@code *throws away negative distances*}
	 * @param point
	 * @param equation
	 */
	private Point3D findClosestPoint(Coordinate3D point, String equation) {
		Equation distance = new Equation(equation);
		
		Point3D cPoint, closestPoint = (Point3D) getPoints().get(0);
		double closestDistance = Double.MAX_VALUE;
		for (PolyPoint cP : getPoints()) {
			cPoint = (Point3D) cP;
			
			//set the values into the distance equation
			
			distance.setVariableValue("x", cPoint.getX()-point.getX());
			distance.setVariableValue("y", cPoint.getY()-point.getY());
			distance.setVariableValue("z", cPoint.getZ()-point.getZ());
			
			
			if ((distance.solve() < closestDistance) && (distance.solve() >= 0) ) {
				closestPoint = cPoint;
				closestDistance = distance.solve();
			}
			
		}
		
		return closestPoint;
		
	}
	
	
	private double getZAtPoint_Surface(double x, double y) {
		Point3D pXY, pX, pY, p;

		//the second part of these equations is to make the whole thing negative if the x and y aren't the right signs
		pXY = findClosestPoint(new Coordinate3D(0,0,0), "sqrt(x^2 + y^2) * ( (abs(x)/x * abs(y)/y) * (abs(x)/x + abs(y)/y) - 1)"); //x and y must both be positive
		p = findClosestPoint(new Coordinate3D(0,0,0), "sqrt(x^2 + y^2) * ( (abs(x)/x * abs(y)/y) * (_abs(x)/x + _abs(y)/y) - 1)"); //x and y must both be negative
		pX = findClosestPoint(new Coordinate3D(0,0,0), "sqrt(x^2 + y^2) * ( (abs(x)/x - abs(y)/y) - 1)"); //x must be positive and y must be negative
		pY = findClosestPoint(new Coordinate3D(0,0,0), "sqrt(x^2 + y^2) * ( (abs(y)/y - abs(x)/x) - 1)"); //y must be positive and z must be negative
		
		
		//try to make sure we have at least three unique points (this is not 100% accurate but it should give us an hint if it's not working)
		if (pXY.equals(p)) {
			Exception e = new Exception("pXY == p");
			e.printStackTrace();
		}
		if (pXY.equals(pX)) {
			Exception e = new Exception("pXY == pX");
			e.printStackTrace();
		}
		if (pX.equals(pY)) {
			Exception e = new Exception("pX == pY");
			e.printStackTrace();
		}
		
		/*
		To get dr/dx and dr/dy we must solve the equation  dx (dz/dx) + dy (dz/dy) = dz
		Where dx, dy, and dr are knowns. Since there are two unknowns we must use 4 points (or 2 unique combinations of 2 points) to get 2 equations with 2 sets of dx, dy, and dr.
		*/
		double dX1 = pXY.getX() - p.getX();
		double dY1 = pXY.getY() - p.getY();
		double dZ1 = pXY.getZ() - p.getZ();
		
		double dX2 = pXY.getX() - pY.getX();
		double dY2 = pX.getY() - pY.getY();
		double dZ2 = pX.getZ() - pY.getZ();
		
		double[] drdx_drdy = Physics_engine_toolbox.solveLinearSystem(dX1, dY1, dZ1, dX2, dY2, dZ2);
		
		return pXY.getZ() + (x - pXY.getX()) * drdx_drdy[0] + (y - pXY.getY()) * drdx_drdy[1];	
		
	}
	
	
	/**
	 * {@summary sets the texture of the object but treats the object as a thin sheet (or a 2D plane) in 3D rather than as a 3D shape with an inside. }
	 *  {@code could theoretically work but deprecated because there needs to be points throughout the surface and that will mess up the polygon border of the surface }
	 */
	@Deprecated
	public void setTexture_Surface(String fileName) {
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
						
						newPoint = Texture.getRGBPoint(x,y,getZAtPoint_Surface(x,y),0,0,false);
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
			collisionCheckGain = gain;
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
		
		if (Vector3D.class.isAssignableFrom(o2.getSpeed().getClass()) && Vector3D.class.isAssignableFrom(getSpeed().getClass())) {
			relativeSpeed = Vector3D.subtract((Vector3D) o2.getSpeed(),(Vector3D) getSpeed());
		}else if (Vector2D.class.isAssignableFrom(o2.getSpeed().getClass()) && Vector2D.class.isAssignableFrom(getSpeed().getClass())) {
			relativeSpeed = Vector2D.subtract((Vector2D) o2.getSpeed(),(Vector2D) getSpeed());
		}else{
			relativeSpeed = Vector.subtract(o2.getSpeed(),getSpeed());
			
		}
		
		Vector2D normalVec;
	
		if (Coordinate3D.class.isAssignableFrom(pointOfCollision.getClass())) {
			normalVec = new Vector3D(getCoordinates(), (Coordinate3D) pointOfCollision);
		}else{ //if the point was 2D, just set it at zPos 0 and carry on
			normalVec = new Vector2D(getCoordinates(),pointOfCollision);
		}
		
		return new CollisionEvent(pointOfCollision, normalVec,(Tangible) this, relativeSpeed);
	}
	
	public boolean checkForCollision(Coordinate2D point, Tangible ob, double radius) {
		
		//getting the three-dimensional coordinates of point
		Coordinate3D point3D;
		if (Coordinate3D.class.isAssignableFrom(point.getClass())) {
			//try to make the point a 3D point
			point3D = (Coordinate3D) point;
		}else{ //if the point was 2D, just set it at zPos 0 and carry on
			point3D = new Coordinate3D(point.getX(),point.getY(),0);
		}
		
		
		
		//getting the Three-dimensional equivalent position of the object the point is in
		double obX = ob.getX();
		double obY = ob.getY();
		double obZ;
		if (Three_dimensional.class.isAssignableFrom(ob.getClass())) {
			obZ = ((Three_dimensional) ob).getZ();
		}else{
			obZ = 0;
		}
		
		
		//checking if the point is within the collision area of any of these object's points
		int pointCounter = 10;
		for (Coordinate3D cPoint : getPlatePoints()) {
			if (pointCounter % collisionCheckGain == 0) { //only check every [collisionCheckGain] points
				if (Physics_engine_toolbox.objectRelativePointDistance3D(obX, obY, obZ, point3D, getX(), getY(), getZ(), cPoint) <= radius + platePointSize * collisionCheckGain) {
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
		int pointCounter = 100;
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

	public boolean getIsTangible() {
		return isTangible;
	}

	public void setIsTangible(boolean isTangible) {
		this.isTangible = isTangible;
	}

	/**
	 * {@summary warning: this can be pretty expensive if cameras are being used}
	 * @param showBorder
	 */
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}
	



}
