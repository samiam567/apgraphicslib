package LegendOfJava;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector3D;

public class MainCharacter extends Character implements  MouseMotionListener, KeyListener {
	protected static double playerSpeed = 20;
	protected static double playerTurningSpeed = 0.7;
	
	public MainCharacter(Object_draw drawer) {
		super(drawer,Settings.width/2, Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50, 250);
		
		load();
		add();
		setName("Ryan");
		addListeners();
	}
	
	
	@Override
	public void Update(double frames) {
		super.Update(frames);	
	}
	
	public void hit(double attackPower) {
		super.hit(attackPower);
		LegendOfJavaRunner.console.setMessage("You have been hit! Health: " + HP);
	}
	public void die() {
		System.out.println("I cAnnOT bE kIllED mWaHaHaHa");
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {	
	
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	
			getDrawer().pause();
	
			((Vector3D) Room.roomAngV).setIJK(-prevPoint.getY()+e.getY(),e.getX()-prevPoint.getX(),0);
			Room.roomAngV.multiply(-playerTurningSpeed);
		
			prevPoint.setPos(e.getX(), e.getY());
			
			getDrawer().resume();
			
			
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {

		if (e.getKeyChar() == '\n') { //enter key
			
		}else if (e.getKeyChar() == 'm') {
			
		}
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyChar() == 'w') {
			Room.roomSpeed.setIJK(0,0,-playerSpeed);
		}else if (e.getKeyChar() == 's') {
			Room.roomSpeed.setIJK(0,0,playerSpeed);
		}
		
		if (e.getKeyChar() == 'd') {
			((Vector3D) Room.roomAngV).setIJK(0,-1,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}if (e.getKeyChar() == 'a') {;
			((Vector3D) Room.roomAngV).setIJK(0,1,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}else if (e.getExtendedKeyCode() == 38) { //UP arrow key
			((Vector3D) Room.roomAngV).setIJK(-1,0,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}else if (e.getExtendedKeyCode() == 40) { //DOWN arrow key
			((Vector3D) Room.roomAngV).setIJK(1,0,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}
		
		if (e.getKeyChar() == ' ') {
			System.out.println("attacking!");
			swordArm.Update(0.000000000000000001);
			attack();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyChar() ==  'w') || (e.getKeyChar() == 's')) {
			((Vector3D) Room.roomSpeed).setIJK(0, 0, 0);
		}else if ( (e.getKeyChar() == 'a') || (e.getKeyChar() == 'd') || (e.getExtendedKeyCode() == 40) || (e.getExtendedKeyCode() == 38)  ) {	
			((Vector3D) Room.roomAngV).setIJK(0,0,0);
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
	

}
