package apgraphicslib;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


/**
 * {@code replaced by Camera - will rotate and pan objects to simulate camera behavior, but isn't a true camera. }
 */
@Deprecated 
public class Camera_Simulated extends Physics_object implements Updatable, MouseMotionListener, KeyListener, Resizable {
	private double cameraPanSpeed = 10000, cameraRotateSpeed = 10;
	
	private Vector3D cameraPanVelocity = new Vector3D(0,0,0), prevCameraPanVelocity = new Vector3D(0,0,0);
	private Vector3D cameraRotateVelocity = new Vector3D(0,0,0), prevCameraRotateVelocity = new Vector3D(0,0,0);

	private Coordinate3D cameraCenter = new Coordinate3D(Settings.width/2,Settings.height/2, -Settings.distanceFromScreen);
	
	public Camera_Simulated(Object_draw drawer) {
		super(drawer);
		
		addListeners();
	}

	
	@Override
	public void Update(double frames) {
		
		Camera_Simulated_objectable cOb;
		
		
		for (Updatable cUp : getDrawer().getUpdatables()) {
			try {
				cOb = (Camera_Simulated_objectable) cUp;
				cOb.setOrbitalAngularVelocity(((Vector3D)cOb.getOrbitalAngularVelocity()).statAdd(cameraRotateVelocity).subtract(prevCameraRotateVelocity));
				cOb.setSpeed( ((Vector3D)cOb.getSpeed()).statAdd(cameraPanVelocity).subtract(prevCameraPanVelocity) );
				
			}catch(ClassCastException c) {}
		}
		prevCameraRotateVelocity.setIJK(cameraRotateVelocity);
		prevCameraPanVelocity.setIJK(cameraPanVelocity);
	}

	@Override
	public void prePaintUpdate() {
		
		Camera_Simulated_objectable cOb;
		for (Updatable cUp : getDrawer().getUpdatables()) {
			try {
				cOb = (Camera_Simulated_objectable) cUp;
				cOb.setPointOfRotation(cameraCenter, true);
			}catch(ClassCastException c) {}
		}
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {	
	
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {

			
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {

		if (e.getKeyChar() == '\n') { //enter key
			
		}else if (e.getKeyChar() == 'm') {
			
		}
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if ( (e.getKeyChar() == 'w' ) || (e.getKeyCode() == 87)) {
			cameraPanVelocity.setIJK(0,0,-cameraPanSpeed);
		}else if ((e.getKeyChar() == 's') || (e.getKeyCode() == 83)) {
			cameraPanVelocity.setIJK(0,0,cameraPanSpeed);
		}
		
		if ((e.getKeyChar() == 'd') || (e.getKeyCode() == 68)) {
			System.out.println("d");
			((Vector3D) cameraRotateVelocity).setIJK(0,1,0);
			cameraRotateVelocity.multiply(cameraRotateSpeed);
			
		}if ( (e.getKeyChar() == 'a') || (e.getKeyCode() == 65)) {
			System.out.println("a");
			((Vector3D) cameraRotateVelocity).setIJK(0,-1,0);
			cameraRotateVelocity.multiply(cameraRotateSpeed);
		}else if (e.getExtendedKeyCode() == 38) { //UP arrow key
			((Vector3D) cameraRotateVelocity).setIJK(1,0,0);
			cameraRotateVelocity.multiply(cameraRotateSpeed);
		}else if (e.getExtendedKeyCode() == 40) { //DOWN arrow key
			((Vector3D) cameraRotateVelocity).setIJK(-1,0,0);
			cameraRotateVelocity.multiply(cameraRotateSpeed);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyChar() ==  'w') || (e.getKeyChar() == 's')  || (e.getKeyCode() == 87)|| (e.getKeyCode() == 83)) {
			((Vector3D) cameraPanVelocity).setIJK(0, 0, 0);
		}else if ( (e.getKeyChar() == 'a') || (e.getKeyChar() == 'd')|| (e.getKeyCode() == 65) || (e.getKeyCode() == 68) || (e.getExtendedKeyCode() == 40) || (e.getExtendedKeyCode() == 38)  ) {	
			((Vector3D) cameraRotateVelocity).setIJK(0,0,0);
		}

	}
	
	public void addListeners() {		
		getDrawer().getFrame().getContentPane().addMouseMotionListener(this);
		getDrawer().getFrame().getGlassPane().addMouseMotionListener(this);
		getDrawer().getFrame().addMouseMotionListener(this);
		getDrawer().addMouseMotionListener(this);
		
		getDrawer().getFrame().getContentPane().addKeyListener(this);
		getDrawer().getFrame().getGlassPane().addKeyListener(this);
		getDrawer().getFrame().addKeyListener(this);
		getDrawer().addKeyListener(this);
		
	}


	@Override
	public void resize() {
		cameraCenter.setPos(getDrawer().getFrameWidth()/2, getDrawer().getFrameHeight()/2, -Settings.distanceFromScreen);
	}
	
}

