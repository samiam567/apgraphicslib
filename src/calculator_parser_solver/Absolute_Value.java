package calculator_parser_solver;

public class Absolute_Value extends One_subNode_node {
	
	public Absolute_Value() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("|" + a + "|");
		return Math.abs(a);
	}
	
	public String toString() {
		return "abs";
	}
	
	// class-specific advanced operations
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode outputNode) {
		
		if (n1 instanceof AdvancedValueNode) {
			if (n1 instanceof ComplexValueNode) { 
				// complex number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(operation(((ComplexValueNode) n1).getReal()), operation(((ComplexValueNode) n1).getComplex()));
			}else {
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass());
				outputNode.setValue(operation(n1.getValue()));
			}
		}else {
			outputNode.setValue(operation(n1.getValue()));
		}
		
		return outputNode;
		
	}
}