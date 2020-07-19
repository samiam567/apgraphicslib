package apgraphicslib;

public class AffineRotation3D extends AffineRotation {
	
	//for the rot matrices, a 5 will be replaced by a trig function in the calculateRotation() method
	private double[][] xRot = {
			{1,0,0,0}, //0
			{0,5,5,0}, //1
			{0,5,5,0}, //2
			{0,0,0,1}, //3
	};
	private double[][] yRot = {
			{5,0,5,0}, //0
			{0,1,0,0}, //1
			{5,0,5,0}, //2
			{0,0,0,1}, //3
	}; 
	private double[][] zRot = {
			{5,5,0,0}, //0
			{5,5,0,0}, //1
			{0,0,1,0}, //2
			{0,0,0,1}, //3
	};
	
	public double[][] affRotMatrix;
	public AffineRotation planeRotTheta = new AffineRotation();
	public AffineRotation planeRotPhi = new AffineRotation();
	public AffineRotation negativePlaneRotTheta = new AffineRotation();
	public AffineRotation negativePlaneRotPhi = new AffineRotation();
	public AffineRotation planeRotation = new AffineRotation();
	public boolean advancedRotation = Settings.advancedRotation;
	
	public AffineRotation3D() {}
	
	public AffineRotation3D(Vector3D rotation) {
		calculateRotation(rotation);
	}

	public void calculateRotation(double xTheta, double yTheta, double zTheta) {	
		//xRotation
		xRot[1][1] = Math.cos(xTheta);
		xRot[2][1] = -Math.sin(xTheta);
		xRot[1][2] = Math.sin(xTheta);
		xRot[2][2] = Math.cos(xTheta);
		
		//yRotation
		yRot[0][0] = Math.cos(yTheta);
		yRot[2][0] = Math.sin(yTheta);
		yRot[0][2] = -Math.sin(yTheta);
		yRot[2][2] = Math.cos(yTheta);
		
		//zRotation
		zRot[0][0] = Math.cos(zTheta);
		zRot[1][0] = -Math.sin(zTheta);
		zRot[0][1] = Math.sin(zTheta);
		zRot[1][1] = Math.cos(zTheta);
	
		affRotMatrix = Physics_engine_toolbox.matrixMultiply(xRot, yRot);
		affRotMatrix = Physics_engine_toolbox.matrixMultiply(affRotMatrix, zRot);
	}
	
	/**
	 * {@summary will create a AffineRotation that will rotate the points rotation radians MORE than they already are rotated AROUND the passed Vector with the manitude of the passed vector}
	 */
	@Override
	public void calculateRotation(Vector rotation) {
		Vector3D rotTemp = ((Vector3D) rotation);
		
		if (advancedRotation) {
			planeRotTheta.calculateRotation(new Vector(rotTemp.getTheta()));
			planeRotPhi.calculateRotation(new Vector(rotTemp.getPhi()));
			planeRotation.calculateRotation(new Vector(rotTemp.getR()));
			negativePlaneRotPhi.calculateRotation(new Vector(-rotTemp.getPhi()));
			negativePlaneRotTheta.calculateRotation(new Vector(-rotTemp.getTheta()));
		}else {
			calculateRotation(rotTemp.getI(),rotTemp.getJ(),rotTemp.getK());
		}
	}

}