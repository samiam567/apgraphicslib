package apgraphicslib;

public class AffineRotation {
	public double a,b,c,d;
	private Vector rotation;
	
	public AffineRotation() {}
	
	public AffineRotation(Vector vec) {
		calculateRotation(vec);
	}
	
	/**
	 * {@summary will create a AffineRotation that will rotate the points [rotation] radians MORE than they already are rotated}
	 */
	public void calculateRotation(Vector rotation) {
		this.rotation = rotation;
		double theta = rotation.getR();
		a = Math.cos(theta);
		b = -Math.sin(theta);
		c = -b;
		d = a;
	}
}