package apgraphicslib;

import apgraphicslib.Physics_2DPolygon.PolyPoint;

public abstract class Physics_3DTexturedEquationedPolygon extends Physics_3DTexturedPolygon {

	protected class EquationNotImplementedException extends Exception {
		private static final long serialVersionUID = -8542176729601256586L;
		public EquationNotImplementedException() {
			super("The calculatePointValues(theta,phi) method for Physics_3DTexturedEquationedPolygon was not implemented in the child class! \n Please implement it.");
		}
	}
	public Physics_3DTexturedEquationedPolygon(Object_draw drawer, double x, double y, double z, double size, double ppSize) {
		super(drawer, x, y, z, ppSize);
		
		//trick the parent into thinking we have points 
		// (the base points are only used in calculatePointValues() so their values don't matter to us anyway)
		// we will just generate all of our points with the equation
		addPoint(size,size,-size);
		addPoint(size,-size,-size);
		addPoint(-size,-size,-size);
		addPoint(-size,size,-size);
		addPoint(size,size,size);
		addPoint(size,-size,size);
		addPoint(-size,-size,size);
		addPoint(-size,size,size);
		
	}
	
	/**
	 * 
	 * @param coords
	 * @return the radius of the point with the given coords (must be 3D)
	 */
	private double calculateR(double[] coords) {
		return Math.sqrt( coords[0]*coords[0] + coords[1]*coords[1] + coords[2]*coords[2]);
	}
	
	/**
	 * {@summary calculated point-values at the given theta and phi}
	 * {@code this is the bread and butter of making this child class work and MUST be overridden according to the equation you want}
	 * @param theta
	 * @param phi
	 * @return {x, y, z, r, dR / dTheta , dR / dPhi}
	 */
	@Override
	protected double[] calculatePointValues(double theta, double phi) {
		double[] p1 = equation(theta,phi);
		double[] pt2 = equation(theta + dTheta/2, phi);
		double[] pp2 = equation(theta, phi + dPhi/2);
		double r = calculateR(p1);
		return new double[] {p1[0],p1[1],p1[2],r, (calculateR(pt2) - r) / (dTheta/2),(calculateR(pp2) - r) / (dPhi/2)};
	}
	
	
	/**
	 * {@code this is the bread and butter of making this child class work and MUST be overridden according to the equation you want}
	 * @param theta
	 * @param phi
	 * @return {x, y, z}
	 */
	protected double[] equation(double theta, double phi) {
		EquationNotImplementedException e = new EquationNotImplementedException();
		e.printStackTrace();
		System.exit(1);
		return null;		
	}
	
	
	
	
	/**
	 * {@code resets the size based on the coordinates of the points}
	 * this is needed here because an equationed polygon has no frame points to calculate the size with so we must use the platepoints
	 */
	@Override
	public void reCalculateSize() {
		super.reCalculateSize();
		for (PolyPoint cPoint : getPlatePoints()) {
			//if this points coords are greater than the max coords the size permits, increase the size to accommodate this point
			if (Math.abs(cPoint.getX()) > getXSize()/2) setSize(Math.abs(cPoint.getX()*2),getYSize());
			if (Math.abs(cPoint.getY()) > getYSize()/2) setSize(getXSize(),Math.abs(cPoint.getY()*2));
			if (Math.abs(((Three_dimensional) cPoint).getZ()) > getZSize()/2) setSize(getXSize(),getYSize(),Math.abs(((Three_dimensional) cPoint).getZ())*2);
		}
	}

}
