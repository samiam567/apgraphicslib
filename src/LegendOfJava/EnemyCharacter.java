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
	
	public static int numEnemys = 0;
	
	/**
	 * 
	 * @param Char the mainCharacter of the game (Ryan)
	 * @param x the x position of the enemy
	 * @param z the z position of the enemy
	 */
	public EnemyCharacter(MainCharacter Char, double x, double z) {
		super(Char.getDrawer(),x, Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50,z);
		mainCharacter = Char;
		targetPoint = new Coordinate3D(0,  Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50, 0);
		targSpeed = new Vector3DDynamicPointVector(getCoordinates(),targetPoint);
		setName("enemy");
	}
	
	@Override
	protected void setBodyParts() {
		super.setBodyParts();
		head.textureSrc = "src/LegendOfJava/assets/enemyHead.jpg";
		
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

	}
	
	@Override
	public void die() { 
		remove();
		numEnemys--;
		LegendOfJavaRunner.console.setMessage("Enemy Killed!");
	}
	
	@Override
	public void hit(double attackPower) {
		LegendOfJavaRunner.console.setMessage("Enemy Hit! Enemy health: " + HP);
		super.hit(attackPower);
	}
	
	@Override
	public void load() {
		super.load();
		head.rotate(new Vector3D(0,Math.PI,0));
		head.rotate(new Vector3D(0,0,Math.PI/2));
		head.rotate(new Vector3D(Math.PI/4,0,0));
	}
	
	public void add(Room room) {
		parentRoom = room;
		super.add();
		numEnemys++;
	}
	
	@Override
	public void setSpeed(Vector speed) {
		super.setSpeed(speed);   //.statAdd(targSpeed));
	}
	
	
	

}
