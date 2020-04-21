package LegendOfJava;

import apgraphicslib.Coordinate3D;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Vector;
import apgraphicslib.Vector3D;
import apgraphicslib.Vector3DDynamicPointVector;


public class EnemyCharacter extends Character implements RoomObjectable {
	
	private MainCharacter mainCharacter;
	private Coordinate3D targetPoint;
	private double targetTheta = Math.PI/2;
	private Room parentRoom;
	private Vector3DDynamicPointVector targSpeed;

	
	public EnemyCharacter(MainCharacter Char, double x, double y, double z) {
		super(Char.getDrawer(),x,y,z);
		mainCharacter = Char;
		targetPoint = new Coordinate3D(0, y, 0);
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
		
		targetPoint.setPos(x, y, z);	
		

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
	}
	
	@Override
	public void setSpeed(Vector speed) {
		super.setSpeed(speed.statAdd(targSpeed));
	}
	
	
	

}
