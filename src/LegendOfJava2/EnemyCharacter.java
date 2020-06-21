package LegendOfJava2;

import apgraphicslib.Settings;
import apgraphicslib.Timer;
import apgraphicslib.Vector;
import apgraphicslib.Vector3D;


public class EnemyCharacter extends Character implements RoomObjectable {
	
	private class EnemyAttackTimer extends Timer {
		private long attackTime = 0;
		private EnemyCharacter character;
		public EnemyAttackTimer(EnemyCharacter character, double time) {
			super(character.getDrawer(), time, TimerUnits.seconds);
			this.character = character;
		}
		
		public EnemyAttackTimer(EnemyCharacter character, double time, long attackTime) {
			super(character.getDrawer(), time, TimerUnits.seconds);
			this.character = character;
			this.attackTime = attackTime;
		}
		
		@Override
		public void triggerEvent() {
		
			if (getDrawer().getDrawables().contains(character)) { //if the enemy is still alive
				character.attack();
				if (attackTime % 2 == 0) { //switch between waiting 1 and 3 seconds between attacks
					new EnemyAttackTimer(character, 3, attackTime + 1);
				}else {
					new EnemyAttackTimer(character, 1, attackTime + 1);
				}
			}
		}
		
	}
	
	private Room parentRoom;
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
		
	
		//controlling the position of the HP bar above the enemy's head
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
		LOJAudioManager.playHitAudio(); //SMACK!
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
		new EnemyAttackTimer(this,numEnemys % 5);
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
