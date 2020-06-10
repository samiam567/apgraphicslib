package apgraphicslib;

import java.awt.Graphics;

public interface CameraMovable extends Drawable {
	
	
	/**
	 * {@code paints the object using the passed camera's paint data, or in the fov of the camera}
	 * @param page
	 * @param cam
	 */
	public void paint(Camera cam, Graphics page);
	
	
	/**
	 * {@summary creates camera paint data. This is called by the camera after this object is added to it}
	 * @param cam
	 */
	public void createCameraPaintData(Camera cam);
	
	/**
	 * {@summary deletes camera paint data. This is called by the camera after this object is removed from it}
	 * @param cam
	 */
	public void deleteCameraPaintData(Camera cam);
	
	
	/**
	 * 
	 * @param cam the camera for which to update the data
	 */
	public void updateCameraPaintData(Camera cam);
	
	
	/**
	 * {@summary this controls the order at which the objects are painted. the higher the number, the further back the object will be and it will be painted earlier}
	 * @param cam the camera to get the paint order for. passing null will return the p.o.v. for the normal field of view 
	 * @return the paint order value
	 */
	public double getPaintOrderValue(Camera cam);
}
