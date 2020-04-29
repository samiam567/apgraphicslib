package apgraphicslib;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import apgraphicslib.Physics_2DPolygon.PolyPoint;
import apgraphicslib.Physics_3DPolygon.Point3D;

/**
 * 
 * @author apun1
 * {@summary tools to draw objects using the mouse. Needs update to really be used well}
 * {@code you must call startCapture() before you can add points }
 */
@Deprecated
public class Polygon_drawer_tools extends Physics_drawable implements MouseListener, MouseMotionListener, KeyListener, Drawable {
	private Physics_3DPolygon object;
	private String name;
	private boolean capturing = false;
	private Coordinate2D prevPoint = new Coordinate2D(0,0);
	private double contPointDist = 4;
	private boolean constCapture = false;
	private boolean mirror;
	private boolean rotating;
	private double rotationSpeed;
	
	public Polygon_drawer_tools(Physics_3DPolygon ob) {
		super(ob.getDrawer(),ob.getX(),ob.getY());
		object = ob;
		addListeners();
		name = "Polygon_drawer_tools for " + object.getName();
	}
	
	
	private void addListeners() {
		object.getDrawer().getFrame().getContentPane().addMouseListener(this);
		object.getDrawer().getFrame().addMouseListener(this);
		object.getDrawer().addMouseListener(this);
		
		object.getDrawer().getFrame().getContentPane().addMouseMotionListener(this);
		object.getDrawer().getFrame().addMouseMotionListener(this);
		object.getDrawer().addMouseMotionListener(this);
		
		object.getDrawer().getFrame().getContentPane().addKeyListener(this);
		object.getDrawer().getFrame().addKeyListener(this);
		object.getDrawer().addKeyListener(this);
	}
	

	
	/**
	 * {@summary tells the drawer_tools to start capturing points}
	 */
	public void startCapture() {
		object.getDrawer().out.println("capture started");
		object.getDrawer().out.println("Type ENTER when done");
		object.getDrawer().out.println("type m to activate mirror mode");
		object.getDrawer().out.println("type 3 to extrude the drawing"); //TODO Make this
		object.getDrawer().out.println("right click and drag to rotate the drawing");
		
		object.getDrawer().remove(object); //this keeps many errors from happening that stem from live building
		capturing = true;
		object.getDrawer().add(this);
		while (capturing) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void endCapture() {
		object.getDrawer().out.println("capture finished");
		capturing = false;
		object.getDrawer().remove(this);
		object.getDrawer().add(object);
	}
	
	private void addPoint(int x, int y) {
		object.addPoint(x - object.getX(),y - object.getY(),0);
		if (mirror) {
			object.addPoint(-(x - object.getX()),(y - object.getY()),0);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
//		object.getDrawer().out.println("mouse clicked");
		if (capturing) {
			addPoint(e.getX(),e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
//		object.getDrawer().out.println("mouse pressed");
		prevPoint = new Coordinate2D(e.getX(),e.getY());
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			if (capturing) {
				constCapture  = true;
			}
		}else if (e.getButton() == MouseEvent.BUTTON3) { //rotate the drawing
			rotating = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
//		object.getDrawer().out.println("mouse released");
		constCapture = false;
		rotating = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		object.getDrawer().out.println("mouse entered");
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
//		object.getDrawer().out.println("mouse exited");
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
//		object.getDrawer().out.println("mouse moved");
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		
		if (constCapture) {
			Coordinate2D newPoint = new Coordinate2D(e.getX(),e.getY());
			if (Physics_engine_toolbox.distance2D(newPoint, prevPoint) > contPointDist) {
				addPoint((int)prevPoint.getX(),(int)prevPoint.getY());
				prevPoint = newPoint;
			}
		}else if (rotating) {
			Vector3D rotation = new Vector3D(prevPoint.getY()-e.getY(),e.getX()-prevPoint.getX(),0);
			rotation.multiply(0.0001);
			for (PolyPoint cPoint : object.getPoints()) {
				((Point3D)cPoint).rotate(rotation);
				((Point3D)cPoint).prevAngle = 0;
			}
		}
		
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		object.getDrawer().out.println("Key typed");
		if (e.getKeyChar() == '\n') { //enter key
			endCapture();
		}else if (e.getKeyChar() == 'm') {
			mirror = true;
		}
		
		System.out.println(e.getKeyChar());
	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		object.getDrawer().out.println("Key Pressed");
		System.out.println(rotationSpeed);
		double rotAdd = 0.01;
		
		if (e.getKeyChar() == 'a') {;
			rotationSpeed+=rotAdd;
			object.rotate(new Vector3D(0,rotationSpeed,0));
		}else if (e.getKeyChar() == 'd') {
			rotationSpeed+=rotAdd;
			object.rotate(new Vector3D(0,-rotationSpeed,0));
		}else if (e.getKeyChar() == 'w') {
			rotationSpeed+=rotAdd;
			object.rotate(new Vector3D(rotationSpeed,0,0));
		}else if (e.getKeyChar() == 's') {
			rotationSpeed+=rotAdd;
			object.rotate(new Vector3D(-rotationSpeed,0,0));
		}
		
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		object.getDrawer().out.println("Key Released");
		rotationSpeed = 0;
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
		
	}

	@Override
	public Object_draw getDrawer() {
		return object.getDrawer();
	}

	@Override
	public void paint(Graphics page) {
		//draw crosshairs for mirroring
		page.drawLine((int) object.getX(), 0,(int) object.getX(),(int) Settings.height);
		page.drawLine(0,(int) object.getY(), Settings.width,(int) object.getY());
		
		for (PolyPoint cPoint : object.getPoints()) {
			page.fillRect((int) (object.getX() + cPoint.getX()-2),(int) (object.getY() + cPoint.getY()-2), 4, 4);
		}
		
	}

	@Override
	public double getX() {
		return object.getX();
	}

	@Override
	public double getY() {
		return object.getY();
	}

	@Override
	public Coordinate2D getCoordinates() {
		return object.getCoordinates();
	}



	

	
	

}
