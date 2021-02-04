package calculator_parser_solver;

public class Multiplication extends Two_subNode_node {
	
	public Multiplication() {
		orderOfOpsLevel = 2;
	}
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "*" + b);
		return a*b;
	}
	
	public String toString() {
		return "*";
	}
	
	protected ValueNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
			
		if (n1 instanceof AdvancedValueNode || n2 instanceof AdvancedValueNode) {
			
			if (n1 instanceof ComplexValueNode && n2 instanceof ComplexValueNode) { 
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal()-((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getComplex(), ((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getComplex()+((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getReal());
			}else if (n1 instanceof ComplexValueNode) { 
				// only n1 Complex Number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal(), ((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getReal());
			}else if (n2 instanceof ComplexValueNode) { 
				// only n2 complex number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n1).getReal(), ((ComplexValueNode) n2).getComplex()*((ComplexValueNode) n1).getReal());
			}else {
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
			}
			
		
		}else { //they are just normal values
			outputNode.setValue(operation(n1.getValue(),n2.getValue()));
		}
		
		return outputNode;
	}
}
