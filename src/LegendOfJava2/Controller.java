package LegendOfJava2;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import LegendOfJava.Room;
import apgraphicslib.Vector3D;



public class Controller implements MouseMotionListener, MouseListener, KeyListener{
	private Robot mouseController;
	private LegendOfJava2 runner;
	private boolean mouseMovementActive = true;

	public Controller(LegendOfJava2 runner) {
		this.runner = runner;
		
		addControllerToComponents();
		
		try {
			mouseController = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ( (e.getKeyChar() == 'w' ) || (e.getKeyCode() == 87)) {
			runner.Ryan.movementDirection = 1;
			runner.Ryan.Update(0.00001);
		}else if ((e.getKeyChar() == 's') || (e.getKeyCode() == 83)) {
			runner.Ryan.movementDirection = 2;
			runner.Ryan.Update(0.00001);
		}else if ((e.getKeyChar() == 'a') || (e.getKeyCode() == 65)) {
			System.out.println("a");
			runner.Ryan.movementDirection = 3;
			runner.Ryan.Update(0.00001);
		}else if ( (e.getKeyChar() == 'd') || (e.getKeyCode() == 68)) {
			System.out.println("d");
			runner.Ryan.movementDirection = 4;
			runner.Ryan.Update(0.00001);
		}
		
		if (e.getKeyCode() == 16) { //SHIFT key
	
		}
		
		if (e.getKeyCode() == 27) { // Esc key
			mouseMovementActive = false;
		}
		
		

		if (e.getExtendedKeyCode() == 38) { //UP arrow key
		
		}else if (e.getExtendedKeyCode() == 40) { //DOWN arrow key
	
		}
		
		if (e.getKeyChar() == ' ') { //space
			runner.Ryan.getSpeed().setJ(runner.Ryan.getSpeed().getJ() + runner.Ryan.jumpSpeed);
			runner.Ryan.getAcceleration().setJ(runner.Ryan.getAcceleration().getJ() + runner.gravity);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ( (e.getKeyChar() == 'w' ) || (e.getKeyCode() == 87) || (e.getKeyChar() == 's') || (e.getKeyCode() == 83) || (e.getKeyChar() == 'd') || (e.getKeyCode() == 68) || (e.getKeyChar() == 'a') || (e.getKeyCode() == 65) ) {
			runner.Ryan.getSpeed().setR(0);
			runner.Ryan.movementDirection = 0;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseMovementActive = true;
		//TODO make Ryan attack
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseMovementActive = true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {		
		mouseController.mouseMove(runner.drawer.getFrameWidth()/2,runner.drawer.getFrameHeight()/2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mouseMovementActive) {
			Vector3D moveVec = new Vector3D(e.getYOnScreen()-runner.drawer.getFrameHeight()/2,e.getXOnScreen()-runner.drawer.getFrameWidth()/2,0);
	
			moveVec.multiply(0.01);
			((Vector3D) runner.camera.getRotation()).add(moveVec);
			
			
			mouseController.mouseMove(runner.drawer.getFrameWidth()/2,runner.drawer.getFrameHeight()/2);
		}
	}
	
	private void addControllerToComponents() {
		runner.drawer.addMouseListener(this);
		runner.drawer.getFrame().addMouseListener(this);
		runner.drawer.getFrame().getContentPane().addMouseListener(this);
		
		runner.drawer.addMouseMotionListener(this);
		runner.drawer.getFrame().addMouseMotionListener(this);
		runner.drawer.getFrame().getContentPane().addMouseMotionListener(this);
		
		runner.drawer.addKeyListener(this);
		runner.drawer.getFrame().addKeyListener(this);
		runner.drawer.getFrame().getContentPane().addKeyListener(this);
	}
}
