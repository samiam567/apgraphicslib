package LegendOfJava;

import apgraphicslib.Physics_3DTexturedEquationedPolygon;


/**
 * @author apun1
 *{@summary a body part of a player. As per the parent this will have a texture and will work in 3D}
 */
public abstract class PlayerBodyPart extends Physics_3DTexturedEquationedPolygon implements PlayerBodyPartAble {
	protected String textureSrc = "";
	protected Character parentPlayer;
	
	

	public PlayerBodyPart(Character  parent, double x, double y, double z, int ppSize) {
		super(parent.getDrawer(), x, y, z,100, ppSize);
		parentPlayer = parent;
	
	}	
}
