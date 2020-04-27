package LegendOfJava;

import apgraphicslib.Physics_3DTexturedPolygon;
import apgraphicslib.Vector3D;

public class Sword extends Physics_3DTexturedPolygon implements PlayerBodyPartAble {
	protected String textureSrc = "./src/LegendofJava/assets/sword.png";
	protected Character parentPlayer;
	
	public Sword(SwordArm parentArm) {
		super(parentArm.getDrawer(), parentArm.getX() + PlayerArm.armZSize/2, parentArm.getY() + PlayerArm.armXSize, parentArm.getZ() + PlayerArm.armYSize, 1);
		
		this.parentPlayer = parentArm.parentPlayer;
		
		double xSize = 300;
		double ySize = 30;
		setSize(xSize,ySize);
		addPoint(-xSize/2,-ySize/2);
		addPoint(xSize/2,-ySize/2);
		addPoint(xSize/2,ySize/2);
		addPoint(-xSize/2,ySize/2);
	
		setTexture(textureSrc);
		
		rotatePoints(new Vector3D(0,0,-Math.PI/2));
		setSize(ySize,xSize);

	
		if (parentPlayer.isMain) {
			setPos(parentArm.getX() + PlayerArm.armZSize/2, parentArm.getY() + PlayerArm.armXSize - getYSize()/2 + 50,parentArm.getZ() + 4*parentArm.getZSize());
		}else {
			setPos(parentArm.getX() + PlayerArm.armZSize/2, parentArm.getY() + PlayerArm.armXSize - getYSize()/2 + 50,parentArm.getZ() - 4*parentArm.getZSize());
		}
		
		setName(parentArm.parentPlayer.getName() + " sword");
		
		
	}
	
	@Override
	public double getPaintOrderValue() { 
		
		if (parentPlayer.isMain) {
			return parentPlayer.getHead().getPaintOrderValue() + 1;
		}else {
			return parentPlayer.getHead().getPaintOrderValue() - 1;
		}
	}

}
