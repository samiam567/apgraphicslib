package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import apgraphicslib.Physics_2DPolygon.PolyPoint;
import calculator_parser_solver.Equation;



//TODO add ability to save objects

/**
 * 
 * @author apun1
 * {@summary tools to draw objects using the mouse.}
 * {@code you must call startCapture() before you can add points }
 */
public class Polygon_drawer_tools extends Physics_drawable implements MouseListener, MouseMotionListener, KeyListener, Drawable {
	
	private Object_draw drawer;
	private Camera3D cam;
	
	private Physics_3DTexturedPolygon object;
	private String name;
	private boolean capturing = false;
	private Coordinate2D prevPoint = new Coordinate2D(0,0);
	private double contPointDist = 10;
	private boolean constCapture = false;
	private boolean mirror;
	private boolean rotating;
	private double rotationSpeed, newPointZ = 0, arrowKeyZOffset = 0;
	
	private ScoreBoard zDisplay, zOffsetDisplay;
	
	private Equation zAxis = new Equation("(x/10)^2"); 

	
	public static void main(String[] args) {
		Camera3D cam = new Camera3D(new Coordinate3D(Settings.width/2, Settings.height/2, 0));
		new Polygon_drawer_tools(new Object_draw(), cam);
	}
	
	public Polygon_drawer_tools(Object_draw drawer, Camera3D cam) {
		super(drawer, drawer.getFrameWidth()/2, drawer.getFrameHeight()/2);
		this.cam = cam;
		this.drawer = drawer;
		drawer.addCamera(cam);
		
		zDisplay = new ScoreBoard(drawer,100, 25, "Current zPosition: ", newPointZ);
		zDisplay.setColor(Color.cyan);
		drawer.add(zDisplay);
		
		zOffsetDisplay = new ScoreBoard(drawer,120, 50, "Current arrowKeyZOffset: ", arrowKeyZOffset);
		zOffsetDisplay.setColor(Color.cyan);
		drawer.add(zOffsetDisplay);
		
		zAxis.setPrintStream(drawer.getOutputStream());
		
		object = new Physics_3DTexturedPolygon(drawer, drawer.getFrameWidth()/2, drawer.getFrameHeight()/2,0,2);
		
		
		
		drawer.add(object);
		
		object.setShowBorder(true);
		cam.add(object);
		setName("Polygon_drawer_tools for " + object.getName());
		addListeners();
		
		startCapture();
		
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
		
		drawer.out.println("Listeners added");
	}
	

	
	/**
	 * {@summary tells the drawer_tools to start capturing points}
	 */
	public void startCapture() {
		object.getDrawer().out.println("capture started");
		object.getDrawer().out.println("Type ENTER when done");
		object.getDrawer().out.println("type m to activate mirror mode");
		object.getDrawer().out.println("use the UP and DOWN arrow keys to change the z position of new points");
		object.getDrawer().out.println("right click and drag to rotate the drawing");
		
		object.getDrawer().remove(object); //this keeps many errors from happening that stem from live building
		capturing = true;
		drawer.add(this);
		
		drawer.start();
		
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
		drawer.pause();
		object.setTexture2D("./src/LegendOfJava/assets/texture.jpg",zAxis);
		object.getDrawer().add(object);
		((Vector3D) object.angularVelocity).setIJK(1,1,1);
		
		object.centerPoints();
		
	//	object.setShowBorder(false);
		
		drawer.remove(zDisplay);
		drawer.remove(zOffsetDisplay);
		drawer.resume();
	}
	
	private void addPoint(int x, int y) {	
		
		object.addPoint(x - object.getX(),y - object.getY(),newPointZ);

		if (mirror) {
			object.addPoint(-(x - object.getX()),(y - object.getY()),newPointZ);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		object.getDrawer().out.println("mouse clicked");
		
		if (capturing) {
			addPoint(e.getX(),e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

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
		constCapture = false;
		rotating = false;
		
		//reset camera
		cam.getCameraAngularVelocity().setR(0);
		cam.cameraRotation.setR(0);
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
		zAxis.setVariableValue("x", e.getX() - object.getX());
		zAxis.setVariableValue("y", e.getY() - object.getY());
			
		newPointZ = zAxis.solve() + arrowKeyZOffset;	
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		zAxis.setVariableValue("x", e.getX() - object.getX());
		zAxis.setVariableValue("y", e.getY() - object.getY());
			
		newPointZ = zAxis.solve() + arrowKeyZOffset;	
		
		
		if (constCapture) {
			Coordinate2D newPoint = new Coordinate2D(e.getX(),e.getY());
			if (Physics_engine_toolbox.distance2D(newPoint, prevPoint) > contPointDist) {
				addPoint((int)prevPoint.getX(),(int)prevPoint.getY());
				prevPoint = newPoint;
			}
		}else if (rotating) {
			((Vector3D) cam.getCameraAngularVelocity()).setIJK(prevPoint.getY()-e.getY(),e.getX()-prevPoint.getX(),0);
			((Vector3D) cam.getCameraAngularVelocity()).multiply(0.01);
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
		}else if (e.getExtendedKeyCode() == 38) { //UP arrow key
			arrowKeyZOffset++;
			newPointZ++; //this will be updated later when the mouse moves but we change manualy here so the displayed number immediately changes
			zOffsetDisplay.setScore(arrowKeyZOffset);
		}else if (e.getExtendedKeyCode() == 40) { //DOWN arrow key
			arrowKeyZOffset--;
			newPointZ--; //this will be updated later when the mouse moves but we change manualy here so the displayed number immediately changes
			zOffsetDisplay.setScore(arrowKeyZOffset);
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
		zDisplay.setScore(newPointZ);
		
		//draw crosshairs for mirroring
		page.drawLine((int) object.getX(), 0,(int) object.getX(),(int) getDrawer().getFrameHeight());
		page.drawLine(0,(int) object.getY(), getDrawer().getFrameWidth(),(int) object.getY());
		
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
