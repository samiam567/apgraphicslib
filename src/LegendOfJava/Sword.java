package LegendOfJava;

import apgraphicslib.Physics_3DTexturedPolygon;
import apgraphicslib.Vector3D;

public class Sword extends Physics_3DTexturedPolygon implements PlayerBodyPartAble {
	protected String textureSrc = "src/LegendofJava/assets/sword.png";
	protected Character parentPlayer;
	
	public Sword(SwordArm parentArm, double ppSize) {
		super(parentArm.getDrawer(), parentArm.getX() + PlayerArm.armZSize/2, parentArm.getY() + PlayerArm.armXSize/2, parentArm.getZ() + PlayerArm.armYSize, 2);
		
		
		
		double xSize =130;
		double ySize = 30;
		setSize(xSize,ySize);
		addPoint(-xSize/2,-ySize/2);
		addPoint(xSize/2,-ySize/2);
		addPoint(xSize/2,ySize/2);
		addPoint(-xSize/2,ySize/2);
	
		setTexture(textureSrc);
		
		rotatePoints(new Vector3D(0,0,Math.PI/2));
		setSize(ySize,xSize);
		
		setPos(parentArm.getX() + PlayerArm.armZSize/2, parentArm.getY() + PlayerArm.armXSize/2 - getYSize()/2,parentArm.getZ());
		parentArm.swinging = true;
	}

}
