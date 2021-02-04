package calculator_parser_solver;

public class Addition extends Two_subNode_node {
	
	public Addition() {
		orderOfOpsLevel = 1;
	}
	
	@Override
	protected double operation(double a, double b) {
		return Addition.operationStat(a,b);
	}
	
	protected static double operationStat(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "+" + b);
		return a+b;
	}
	
	public String toString() {
		return "+";
	}
	
	
	// class-specific advanced operations
	
	
	protected ValueNode operation(ComplexValueNode n1, ComplexValueNode n2) {
		return new ComplexValueNode(n1.getReal() + n2.getReal(), n1.getComplex() + n2.getComplex());
	}
	protected ValueNode operation(ComplexValueNode n1, ValueNode n2) {
		return new ComplexValueNode(n1.getReal() + n2.getValue(), n1.getComplex());
	}
	protected ValueNode operation(ValueNode n2, ComplexValueNode n1) {
		return new ComplexValueNode(n1.getReal() + n2.getValue(), n1.getComplex());
	}
	
	
	
}
