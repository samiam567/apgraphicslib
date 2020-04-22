package LegendOfJava;

import java.awt.Graphics;
import java.util.LinkedList;
import apgraphicslib.Coordinate2D;
import apgraphicslib.Coordinate3D;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_3DPolygon;



public class Character extends Physics_3DPolygon implements Hittable {
	public enum Side {left,right};
	
	//combat numbers
	protected double HP = 10;

	private double attack = 5;

	protected double defense = 2;
	
	public boolean isMain = false;
	protected PlayerHead head;
	PlayerBodyPart torso;
	protected PlayerArm leftArm, rightArm;
	protected SwordArm swordArm;
	protected PlayerBodyPart leftLeg, rightLeg; //not currently used

	protected LinkedList<PlayerBodyPartAble> bodyParts = new LinkedList<PlayerBodyPartAble>();
	
	protected Coordinate2D prevPoint = new Coordinate2D(0,0);
	
	
	protected Coordinate3D prevPos = new Coordinate3D(0,0,0);
	
	public Point3D normalPoint = new Point3D(1,0,0);
	protected static int ppSize = 5; // controls the resolution of the textures. Stands for platePointSize in Physics_3DTexturedPolygon, is the size of each pixel
	
	private boolean loaded = false;
	
	
	public Character(Object_draw drawer, double x, double y, double z) {
		super(drawer, x, y, z);
		
		addPoint(normalPoint);
			
		setSize(PlayerHead.headXSize, PlayerHead.headYSize + PlayerTorso.torsoYSize + 180);
		setName("Character");
	}
	
	/**
	 * {@code called when the character was hit by an attack}
	 * @param attackPower the power of the attack this character was hit with
	 */
	public void hit(double attackPower) {
		getDrawer().out.println(getName() + " has been hit");
		HP -= attackPower/defense;
		if (HP <= 0) die();
	}
	
	/**
	 * {@summary this method is called if the characters HP <= 0}
	 */
	public void die() {
		
	}
	
	/**
	 * {@code removes this character from the environment}
	 */
	public void remove() {
		getDrawer().pause();
		getDrawer().remove(this);
		for (PlayerBodyPartAble bP : bodyParts) {
			getDrawer().remove(bP);
		}
		getDrawer().resume();
	}
	
	/**
	 * {@code makes the character swing their sword}
	 */
	public void attack() {
		swordArm.swinging = true;
	}
	
	public double getAttackPower() {
		return attack;
	}
	
	public void load() {
		setBodyParts();
		head.load();
		leftArm.load();
		rightArm.load();
		swordArm.load();
		loaded = true;
		
	}
	
	public void add() {
		if (! loaded) load();
		
		getDrawer().add(this);
		for (PlayerBodyPartAble bP : bodyParts) {
			getDrawer().add(bP);
		}
		
	}
	
	protected void setBodyParts() {
		for (PlayerBodyPartAble bP : bodyParts) {
			getDrawer().remove(bP);
		}
		bodyParts.clear();
		
		head = new PlayerHead(this,ppSize);
		head.setName(getName() + " head"); 
		bodyParts.add(head);
		
		
		torso = new PlayerTorso(this,head.getX(), head.getY() + PlayerTorso.torsoYSize + PlayerHead.headYSize,ppSize);
		torso.setName(getName() + " torso");
		bodyParts.add(torso);
		
		leftArm = new PlayerArm(this,head.getX() - PlayerTorso.torsoXSize - PlayerArm.armXSize*2, head.getY() + PlayerHead.headYSize/2 + PlayerArm.armYSize/2, Side.left,ppSize);
		leftArm.setName(getName() + " leftArm");
		
		rightArm = new PlayerArm(this,head.getX() + PlayerTorso.torsoXSize + PlayerArm.armXSize*2,head.getY() + PlayerHead.headYSize/2 + PlayerArm.armYSize/2, Side.right,ppSize);
		rightArm.setName(getName() + " rightArm");
		
		swordArm = new SwordArm(this, ppSize);
		
		
		bodyParts.add(leftArm);
		bodyParts.add(rightArm);
		bodyParts.add(swordArm);
		
		
		/*
		leftLeg = new PlayerLeg(this,Side.left);
		rightLeg = new PlayerLeg(this,Side.right);
		*/	
		
		setPos(head.getX(),head.getY(),head.getZ());
		
		prevPos.setPos(head.getX(),head.getY(),head.getZ());
	}
	
	
	@Override
	public void Update(double frames) {
		super.Update(frames);		
		
		//move the body parts along with this Character object probably the most difficult way possible
		for (PlayerBodyPartAble bP : bodyParts) {
			bP.setPos(bP.getX() - prevPos.getX(), bP.getY() - prevPos.getY(),bP.getZ() - prevPos.getZ());
			bP.setPos(bP.getX() + getX(), bP.getY() + getY(),bP.getZ() + getZ());
		}
		
		prevPos.setPos(getX(),getY(),getZ());
	}
	
	@Override
	public void paint(Graphics page) {
		
	}
	
	

	public void setPPSize(int playerPPSize) {
		ppSize = playerPPSize;
		loaded = false;
	}

}
