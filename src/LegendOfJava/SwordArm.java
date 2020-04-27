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
		
		
		setPositionRelativeToCharacter();
		
		sword = new Sword(this);
		sword.setPointOfRotation(getCoordinates(), true);
		parentPlayer.getBodyParts().add(sword);
		swinging = true;
		setPositionRelativeToCharacter();
	}

	private void setPositionRelativeToCharacter() {
		if (PlayerArm.swordArm == Side.left) {
			if (parentPlayer.isMain) {
				setPos(parentPlayer.leftArm.getX(),parentPlayer.leftArm.getY() + armYSize/2 + armZSize/2,parentPlayer.leftArm.getZ() + 2*armYSize);
			}else {
				setPos(parentPlayer.leftArm.getX(),parentPlayer.leftArm.getY() + armYSize/2 + armZSize/2,parentPlayer.leftArm.getZ() - 2*armYSize);
			}
		}else {
			if (parentPlayer.isMain) {
				setPos(parentPlayer.rightArm.getX(),parentPlayer.rightArm.getY() + armYSize/2 + armZSize/2,parentPlayer.rightArm.getZ() + 2*armYSize);
			}else {
				setPos(parentPlayer.rightArm.getX(),parentPlayer.rightArm.getY() + armYSize/2 + armZSize/2,parentPlayer.rightArm.getZ() - 2*armYSize);
			}
		}
	}

	@Override
	public void load() {
		setTexture(textureSrc);
		rotatePoints(new Vector3D(Math.PI/2, 0, 0));
		reCalculateSize();
		setPositionRelativeToCharacter();
	}
	
	@Override
	public void Update(double frames) {
		super.Update(frames);
		
		if (swinging) {
		
			if ( Math.abs(((Vector2D)rotation).getI()) < Math.PI/3) {
				
				if (parentPlayer.isMain) {
					((Vector2D) angularVelocity).setI(-5);
					sword.setPointOfRotation(getCoordinates(), true);
					sword.setOrbitalAngularVelocity(angularVelocity);
					angularVelocity.multiply(1);
					
				}else{
					((Vector2D) angularVelocity).setI(3);
					sword.setOrbitalAngularVelocity(angularVelocity);
				}
			}else {
				((Vector2D) angularVelocity).setI(0);
				setRotation(0,((Vector2D) rotation).getJ(),((Vector3D) rotation).getK());
				((Vector2D) angularVelocity).setI(0);		
				sword.setOrbitalRotation(new Vector3D(0,0,0));
				sword.setRotation(new Vector3D(0,0,0));
				swinging = false;
				
				Hittable cHittable = null;
				for (Physics_engine_compatible cObject : getDrawer().getObjects()) {
					try {
						cHittable = (Hittable) cObject; //if we can do this than the object is something we can hit
						
						if (! cHittable.equals(parentPlayer)) { //we cant hit ourself
							if (Physics_engine_toolbox.distance3D(parentPlayer.getCoordinates(), cHittable.getCoordinates()) < sword.getXSize() + 2*sword.getYSize()) {
								cHittable.hit(parentPlayer.getAttackPower());
							}
						}
					}catch(ClassCastException c) {}
				}
			}
		}
		
	}


}
