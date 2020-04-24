package LegendOfJava;

import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Settings;
import apgraphicslib.Vector;
import apgraphicslib.Vector3D;
import apgraphicslib.Vector3DDynamicPointVector;


public class EnemyCharacter extends Character implements RoomObjectable {
	
	private MainCharacter mainCharacter;
	private Coordinate3D targetPoint;
	private double targetTheta = Math.PI/2;
	private Room parentRoom;
	private Vector3DDynamicPointVector targSpeed;
	private HealthBar hpBar;
	
	private double initX, initY, initZ; 
	public static int numEnemys = 0;
	
	/**
	 * 
	 * @param Char the mainCharacter of the game (Ryan)
	 * @param x the x position of the enemy
	 * @param z the z position of the enemy
	 */
	public EnemyCharacter(MainCharacter Char, double x, double z) {
		super(Char.getDrawer(),x, Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50,z);
		initX = x;
		initY = Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50;
		initZ = z;
		
		mainCharacter = Char;
		targetPoint = new Coordinate3D(0,  Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50, 0);
		targSpeed = new Vector3DDynamicPointVector(getCoordinates(),targetPoint);
		setName("enemy");
		
	}
	
	@Override
	protected void setBodyParts() {
		super.setBodyParts();
		getHead().textureSrc = "src/LegendOfJava/assets/enemyHead.jpg";
		rotatePoints(new Vector3D(0,Math.PI,0));
		
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		targetTheta += 0.001;
		double x,y,z,r = Physics_engine_toolbox.distance3D(getCoordinates(), mainCharacter.getCoordinates());
		
		x = mainCharacter.getX() + r*Math.cos(targetTheta + ((Vector3D)rotation).getJ());
		y = targetPoint.getY();
		z = mainCharacter.getZ() + r*Math.sin(targetTheta + ((Vector3D)rotation).getJ());		
		
		targetPoint.setPos(getX(), getY(), getZ());	
		
		if (Math.random() * 10 < 0.1) {
			attack();
		}
		
		double hpX, hpY;
		hpX = getHead().getX() - Settings.width/2;
		hpY = getHead().getY() - PlayerHead.headYSize - 10 - Settings.height/2;
		
		//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
		double parallaxValue = 1;
		if (getHead().getZ() != 0) {
			parallaxValue = (Settings.distanceFromScreen) / ((getHead().getZ()) + Settings.distanceFromScreen);
		}

		hpX *= parallaxValue;
		hpY *= parallaxValue;
		
		hpX += Settings.width/2;
		hpY += Settings.height/2;
		
		hpBar.setPos(hpX,hpY);

	}
	
	@Override
	public void die() { 
		remove();
		numEnemys--;
	}
	
	@Override
	public void hit(double attackPower) {
		super.hit(attackPower);
		AudioManager.playHitAudio(); //SMACK!
	}
	
	@Override
	public void load() {
		super.load();
		getHead().rotatePoints(new Vector3D(0,Math.PI,0));
		hpBar = new HealthBar(getDrawer(),getHead().getX(), getHead().getY() - PlayerHead.headYSize/3,this,10);
	}
	
	@Override
	public void add(Room room) {
		parentRoom = room;
		super.add();
		numEnemys++;
		getDrawer().add(hpBar);
		setPos(initX, initY, initZ);
	}
	
	@Override
	public void remove() {
		super.remove();
		getDrawer().remove(hpBar);
		parentRoom.roomObs.remove(this);
	}
	
	@Override
	public void setSpeed(Vector speed) {
		super.setSpeed(speed);   //.statAdd(targSpeed));
	}
	
	
	

}
