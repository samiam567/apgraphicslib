package calculator_parser_solver;

public class ComplexValueNode extends AdvancedValueNode {
	private double imaginaryComponent;
	
	public ComplexValueNode() {
		super('k');
		notCalculated();
	}
	
	public ComplexValueNode(double real, double imaginary) { 
		super(real,'k');
		imaginaryComponent = imaginary;
	}
	
	public void congjugate() {
		imaginaryComponent = -imaginaryComponent;
	}
	
	public ComplexValueNode getConjugate() {
		return new ComplexValueNode(value,-imaginaryComponent);
	}
	
	public double getImaginaryComponent() {
		return imaginaryComponent;
	}
	
	/*
	@Override
	public ComplexValueNode copy() {
		ComplexValueNode cV = new ComplexValueNode(value,imaginaryComponent);
		cV.setUnsetVal(unsetVal(),'k');
		cV.setName(getName());
		System.out.println(cV.unsetVal());
		return cV;
	}*/
	
	
	@Override
	public String toString() {
		return "" + getValue() + " + " + imaginaryComponent + "i ";
	}

	public double getReal() {
		return value;
	}
	
	public double getComplex() {
		return imaginaryComponent;
	}
	

}
