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
			Vector3D speedVec = new Vector3D(runner.camera.getDirectionFacing().getI(),0,runner.camera.getDirectionFacing().getK());
			speedVec.multiply(1/runner.camera.getDirectionFacing().getR());
			speedVec.multiply(100);
			((Vector3D) runner.Ryan.getSpeed()).setIJK(speedVec.getI(),runner.Ryan.getSpeed().getI(),speedVec.getK());
			
			
		}else if ((e.getKeyChar() == 's') || (e.getKeyCode() == 83)) {

		}
		
		if (e.getKeyCode() == 16) { //SHIFT key
	
		}
		
		if ((e.getKeyChar() == 'd') || (e.getKeyCode() == 68)) {

		}if ( (e.getKeyChar() == 'a') || (e.getKeyCode() == 65)) {

		}else if (e.getExtendedKeyCode() == 38) { //UP arrow key

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
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//TODO make Ryan attack
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
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
		Vector3D moveVec = new Vector3D(e.getYOnScreen()-runner.drawer.getFrameHeight()/2,e.getXOnScreen()-runner.drawer.getFrameWidth()/2,0);

		moveVec.multiply(0.01);
		((Vector3D) runner.camera.getRotation()).add(moveVec);
		runner.camera.getDirectionFacing().rotate(moveVec);
		
		mouseController.mouseMove(runner.drawer.getFrameWidth()/2,runner.drawer.getFrameHeight()/2);
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
