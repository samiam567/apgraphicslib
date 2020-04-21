package LegendOfJava;

import LegendOfJava.Character.Side;
import apgraphicslib.Physics_engine_compatible;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Settings;
import apgraphicslib.Vector2D;
import apgraphicslib.Vector3D;

public class SwordArm extends PlayerArm {

	public boolean swinging = false;
	private Sword sword;
	public SwordArm(Character parent, int ppSize) {
		super(parent, Settings.width/2, Settings.height/2, PlayerArm.swordArm, ppSize);
		
		side = PlayerArm.swordArm;
		
		setSize(armXSize,armYSize,armZSize);
		
		
		if (PlayerArm.swordArm == Side.left) {
			setPos(parentPlayer.leftArm.getX(),parentPlayer.leftArm.getY() + armYSize/2 + armZSize/2,parentPlayer.leftArm.getZ() + armYSize/2);
		}else {
			setPos(parentPlayer.rightArm.getX(),parentPlayer.rightArm.getY() + armYSize/2 + armZSize/2,parentPlayer.rightArm.getZ() + armYSize/2);
		}
		
		sword = new Sword(this,ppSize);
		sword.setPointOfRotation(getCoordinates(), true);
		parentPlayer.bodyParts.add(sword);
	}
	
	

	@Override
	public void load() {
		setTexture(textureSrc);
		rotatePoints(new Vector3D(Math.PI/2, 0, 0));
		
		if (PlayerArm.swordArm == Side.left) {
			setPos(parentPlayer.leftArm.getX(),parentPlayer.leftArm.getY() + armYSize/2 + armZSize/2,parentPlayer.leftArm.getZ() + armYSize/2);
		}else {
			setPos(parentPlayer.rightArm.getX(),parentPlayer.rightArm.getY() + armYSize/2 + armZSize/2,parentPlayer.rightArm.getZ() + armYSize/2);
		}
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		if (swinging) {
			if (((Vector2D)rotation).getI() > -Math.PI/3) {
				((Vector2D) angularVelocity).setI(-3);
				sword.setOrbitalAngularVelocity(angularVelocity);
			}else {
				((Vector2D) angularVelocity).setI(0);
				setRotation(0,((Vector2D) rotation).getJ(),((Vector3D) rotation).getK());
				((Vector2D) angularVelocity).setI(0);		
				sword.setOrbitalRotation(new Vector3D(0,0,0));
				
				swinging = false;
				
				Hittable cHittable = null;
				for (Physics_engine_compatible cObject : getDrawer().getObjects()) {
					try {
						cHittable = (Hittable) cObject; //if we can do this than the object is something we can hit
						
						if (! cHittable.equals(parentPlayer)) { //we cant hit ourself
							if (Physics_engine_toolbox.distance3D(parentPlayer.getCoordinates(), cHittable.getCoordinates()) < sword.getXSize() + sword.getYSize()) {
								cHittable.hit(parentPlayer.getAttackPower());
							}
						}
					}catch(ClassCastException c) {}
				}
			}
		}
		
	}


}
