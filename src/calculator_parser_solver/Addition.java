package calculator_parser_solver;

public class Addition extends Two_subNode_node {
	
	public Addition() {
		orderOfOpsLevel = 1;
	}
	
	@Override
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "+" + b);
		return a+b;
	}
	
	public String toString() {
		return "+";
	}
	
	
	// class-specific advanced operations
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
			
		if (n1 instanceof AdvancedValueNode || n2 instanceof AdvancedValueNode) {
			
			if (n1 instanceof ComplexValueNode && n2 instanceof ComplexValueNode) { 
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(n1.getValue() + n2.getValue(), ((ComplexValueNode) n1).getComplex() + ((ComplexValueNode) n2).getComplex());
			}else if (n1 instanceof ComplexValueNode) { 
				// only n1 Complex Number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(n1.getValue() + n2.getValue(), ((ComplexValueNode) n1).getComplex());
			}else if (n2 instanceof ComplexValueNode) { 
				// only n2 complex number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(n1.getValue() + n2.getValue(), ((ComplexValueNode) n2).getComplex());
			}else {
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
			}
			
		
		}else { //they are just normal values
			outputNode.setValue(operation(n1.getValue(),n2.getValue()));
		}
		
		return outputNode;
	}
	
	
	
}
