package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;

import apgraphicslib.Physics_2DPolygon.AffineRotation;



public class Physics_2DTexturedPolygon extends Physics_2DPolygon implements Textured, Updatable {
	
	private class P2DPTexture {
		private int width;
		private int height;
		BufferedImage image;
		
		private P2DPTexture(String fileName) {
			File img_file = new File(fileName);
			try {
				image = ImageIO.read(img_file);
			} catch (IOException e) {
				getDrawer().out.println("could not read img file:" + fileName);
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
		 public RGBPoint2D getRGBPoint(double x, double y) {
			 RGBPoint2D point = new RGBPoint2D(x,y,0,0,0);
			 double divisor = 1;
			 int pixel = image.getRGB((int) Math.round(x * width/getXSize()/divisor + width/2 ) ,(int) Math.round(y *height/getYSize() /divisor + height/2));
			 int alpha = (pixel >> 24) & 0xff;
			 int red = (pixel >> 16) & 0xff;
			 int green = (pixel >> 8) & 0xff;
			 int blue = (pixel) & 0xff;
			 point.setRGB(red, green, blue, alpha);
			 
			 return point;
		  }
	}		 

	protected class FramePoint extends Point2D {
		private boolean isFound = false;
		public FramePoint(double x, double y) {
			super(x, y);
		}
	}
	protected class RGBPoint2D extends Point2D {
		int R,G,B,alpha;
		
		protected RGBPoint2D(double x, double y) {
			super(x,y);
		}
		
		protected RGBPoint2D(double x, double y, int R, int G, int B) {
			super(x, y);
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
	
	private P2DPTexture Texture;
	private double platePointSize;
	private LinkedList<RGBPoint2D> platePoints;
	private boolean showBorder = false;
	
	public Physics_2DTexturedPolygon(Object_draw drawer, double x, double y, double ppSize) {
		super(drawer, x, y);
		this.platePointSize = ppSize;
		platePoints = new LinkedList<RGBPoint2D>();
	}
	

	private void updatePlatePoints() {
		for (PolyPoint cPoint : platePoints) {
			cPoint.rotate(rotationMatrix);
		}
	}
	
	@Override
	public void updatePoints() {
		super.updatePoints();
		updatePlatePoints();
	}
	

	private boolean allFramePointsFound() {
		FramePoint cPoint;
		try {
			for (PolyPoint cP : getPoints()) {
				cPoint = (FramePoint) cP;
				if (! cPoint.isFound) {
					return false;
				}
			}
		}catch(ClassCastException c) {
			getDrawer().out.println("Non-framePoint in points list (Physics_2DTexturedPolygon) Name: " + getName());
			c.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void checkForFound(RGBPoint2D point) {
		try {
			FramePoint cPoint;
			for (PolyPoint cP : getPoints()) {
				cPoint = (FramePoint) cP;
				if (Physics_engine_toolbox.distance2D(cPoint, point) < 10*platePointSize) cPoint.isFound = true;
			}
		}catch(ClassCastException c) {
			getDrawer().out.println("Non-framePoint in points list (Physics_2DTexturedPolygon) Name: " + getName());
			c.printStackTrace();
		}
	}
	
	
	/**
	 * {@summary sets the texture of the object. Note that the texture cannot be a .jpeg file}
	 */
	@Override
	public void setTexture(String fileName) {
		
		if (getPoints().size() == 0) {
			Exception e = new Exception("Physics_TexturePolygon must have points for a texture to be added");
			e.printStackTrace();
		}
		
		setRotation(new Vector(0));
		
		Texture = new P2DPTexture(fileName);
		
		// remove old plate points
		platePoints.clear();
		
		
		
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
		RGBPoint2D newPoint;
		int spiralNum = -2,spiralSize=1,direction = 0, pointsOut = 0;
		double x = 0, y = 0, xInc = 0, yInc = platePointSize;
		while (! allFramePointsFound()) {
			
			for (int pointNum = 0; pointNum < spiralSize; pointNum++) {
				x += xInc;
				y += yInc;
				
				
				//if our spiral guess was inside our polygon, add the plate point to the shape
				if (framePoly.contains(getX()+x,getY()+  y)) {
					try {
						newPoint = Texture.getRGBPoint(x,y);
						platePoints.add(newPoint);
						checkForFound(newPoint);
					}catch(ArrayIndexOutOfBoundsException a) {
						System.out.println("array out of bounds");
					}
				}else {
					pointsOut++;
					if (pointsOut > (getXSize() * getYSize())/platePointSize ) {
						System.out.println("terminating");
						return;
					}
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
	
	@Override
	public void addPoint(double x, double y) {
		addPoint(new FramePoint(x,y));
	}
	
	/// Camera stuff ////////////////////////////////////////////////////////////////////////////////
		private ArrayList<CameraPaintData> cameraDataSets = new ArrayList<CameraPaintData>();
		private class CameraPaintData {
			private Camera cam;
			private double x, y;	
			private RGBPoint2D[] platePoints;
			
			public CameraPaintData(Camera cam, Physics_2DTexturedPolygon parent) {
				this.cam = cam;
				platePoints = new RGBPoint2D[0];
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
			cameraDataSets.add(new CameraPaintData(cam,this));
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
			
			
			data.x = affRot.a * (getX() - offSetX - camX) + affRot.b * (getY() - offSetY - camY);
			data.y = affRot.c * (getX() - offSetX - camX) + affRot.d * (getY() - offSetY - camY);
			
			data.x += camX;
			data.y += camY;
			
		
			
			 //resize data.platepoints if it isn't the same size as the object list
			if (platePoints.size() != data.platePoints.length) {
				data.platePoints = new RGBPoint2D[platePoints.size()];
				
				//loop through and create a point at each index of the data.platePoints list
				int i = 0;
				for (RGBPoint2D cPoint : platePoints) {
					data.platePoints[i] = new RGBPoint2D(cPoint.getX(), cPoint.getY(), cPoint.R, cPoint.G, cPoint.B);
					i++;
				}
				
			}
			
			int i = 0;
			for (RGBPoint2D cPoint : platePoints) {			
					data.platePoints[i].setPos((affRot.a * (cPoint.getX()) + affRot.b * (cPoint.getY())), (affRot.c * (cPoint.getX()) + affRot.d * (cPoint.getY())));
					data.platePoints[i].setRGB(cPoint.R, cPoint.G, cPoint.B, cPoint.alpha);
					i++;
			}
			
			
		}
		
		@Override
		public void paint(Camera cam, Graphics page) {
			if (showBorder) super.paint(page);
			
			CameraPaintData data = getCameraPaintData(cam);
			
		
			for (RGBPoint2D cPoint : data.platePoints) {
		   		page.setColor(new Color(cPoint.R,cPoint.G,cPoint.B,cPoint.alpha));
				page.fillRect((int) Math.round(data.x + cPoint.getX()-platePointSize/2),(int) Math.round(data.y + cPoint.getY()-platePointSize/2), (int) (platePointSize+1), (int) (platePointSize+1));
			}	
		}
		
		/// Camera stuff end ////////////////////////////////////////////////////////////////////////////////

	
	@Override
	public void paint(Graphics page) {
		if (showBorder) super.paint(page);
		
		for (RGBPoint2D cPoint : platePoints) {
	   		page.setColor(new Color(cPoint.R,cPoint.G,cPoint.B,cPoint.alpha));
			page.fillRect((int) Math.round(getX() + cPoint.getX()-platePointSize/2),(int) Math.round(getY() + cPoint.getY()-platePointSize/2), (int) (platePointSize+1), (int) (platePointSize+1));
		}	
	}

	/**
	 * {@summary warning: this can be pretty expensive if cameras are being used}
	 * {@code this must be called BEFORE the object is added to any cameras if it is true}
	 * @param showBorder
	 */
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

}
