package calculator_parser_solver;

public class Negative extends One_subNode_node {
	
	public Negative(String mode) {
		if (mode.equals("numberInput")) {
			orderOfOpsLevel = 4;
		}else {
			orderOfOpsLevel = 3;
		}
	}
	

	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("_" + a);
		return 0-a;
	}
	
	public String toString() {
		return "_";
	}
	
	
	// class-specific advanced operations
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode outputNode) {
		
		if (n1 instanceof AdvancedValueNode) {
			if (n1 instanceof ComplexValueNode) { 
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(operation(((ComplexValueNode) n1).getReal()), operation(((ComplexValueNode) n1).getComplex()));
			}else {
				Equation.warn("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass());
				outputNode.setValue(operation(n1.getValue()));
			}
		}else {
			outputNode.setValue(operation(n1.getValue()));
		}
		
		return outputNode;
		
	}	
	
}