package LegendOfJava;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Timer;
import apgraphicslib.Vector3D;

public class MainCharacter extends Character implements  MouseMotionListener, KeyListener {
	
	
	/**
	 * 
	 * {@code creating one of these will cause the mainCharacter to jump back the passed number of units}
	 *
	 */
	private class JumpBackTimer extends Timer {
		private static final double timeToJump = 0.4;

		public JumpBackTimer(MainCharacter player, double units) {
			super(player.getDrawer(), timeToJump, TimerUnits.seconds);

			Room.roomSpeed.setIJK(0,0,units / timeToJump);
			
			for (PlayerBodyPartAble bp : bodyParts) {
				bp.setPointOfRotation(head.getCoordinates(), true);
				bp.setOrbitalAngularVelocity(new Vector3D(-2*Math.PI/timeToJump,0,0));
			}
			
			hittable = false;
		}
		
		@Override
		protected void triggerEvent() {
			Room.roomSpeed.setIJK(0,0,0);
			for (PlayerBodyPartAble bp : bodyParts) {
				bp.setOrbitalRotation(new Vector3D(0,0,0));
				bp.setOrbitalAngularVelocity(new Vector3D(0,0,0));
			}
			hittable = true;
		}
		
	}
	
	private boolean hittable = true; //whether or not we can currently be hit
	protected static double playerSpeed = 1700;
	protected static double playerTurningSpeed = 2;

	public MainCharacter(Object_draw drawer) {
		super(drawer,Settings.width/2, Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50, 250);
		isMain = true;
		load();
		add();
		setName("Ryan");
		addListeners();
		defense = 5;
	}
	
	
	@Override
	public void Update(double frames) {
		super.Update(frames);	
	}
	
	@Override
	public void hit(double attackPower) {
		if (hittable) {
			super.hit(attackPower);
			AudioManager.playDamageAudio(); //OOF
		}
	}

	public void die() {
		getDrawer().repaint();
		System.out.println("oof");
		JOptionPane.showMessageDialog(getDrawer(), "GAME OVER");
		getDrawer().stop();
		System.exit(1);
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
			Room.roomSpeed.setIJK(0,0,-playerSpeed);
		}else if ((e.getKeyChar() == 's') || (e.getKeyCode() == 83)) {
			Room.roomSpeed.setIJK(0,0,playerSpeed);
		}
		
		if (e.getKeyCode() == 16) { //SHIFT key
			JumpBackTimer j = new JumpBackTimer(this,300);
		}
		
		if ((e.getKeyChar() == 'd') || (e.getKeyCode() == 68)) {
			System.out.println("d");
			((Vector3D) Room.roomAngV).setIJK(0,1,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}if ( (e.getKeyChar() == 'a') || (e.getKeyCode() == 65)) {
			System.out.println("a");
			((Vector3D) Room.roomAngV).setIJK(0,-1,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}else if (e.getExtendedKeyCode() == 38) { //UP arrow key
			((Vector3D) Room.roomAngV).setIJK(1,0,0);
			Room.roomAngV.multiply(playerTurningSpeed);
		}else if (e.getExtendedKeyCode() == 40) { //DOWN arrow key
			((Vector3D) Room.roomAngV).setIJK(-1,0,0);
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
		if ((e.getKeyChar() ==  'w') || (e.getKeyChar() == 's')  || (e.getKeyCode() == 87)|| (e.getKeyCode() == 83)) {
			((Vector3D) Room.roomSpeed).setIJK(0, 0, 0);
		}else if ( (e.getKeyChar() == 'a') || (e.getKeyChar() == 'd')|| (e.getKeyCode() == 65) || (e.getKeyCode() == 68) || (e.getExtendedKeyCode() == 40) || (e.getExtendedKeyCode() == 38)  ) {	
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
