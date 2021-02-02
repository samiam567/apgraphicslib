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
	
	@Override
	public ComplexValueNode copy() {
		return new ComplexValueNode(value,imaginaryComponent);
	}
	

}
