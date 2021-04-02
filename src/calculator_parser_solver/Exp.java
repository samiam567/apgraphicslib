package calculator_parser_solver;


public class Exp extends One_subNode_node {

	protected double operation(double a) {
		return Math.exp(a);
	}
	
	public String toString() {
		return "e^";
	}
	
	
	// class-specific advanced operations
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode outputNode) {
		
		if ( (n1 instanceof AdvancedValueNode && ( (AdvancedValueNode) n1).needsSpecialOperationConditions)) {
			if (n1 instanceof ComplexValueNode) { 
				
				ComplexValueNode N1 = (ComplexValueNode) n1;
				
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				
				
				ComplexValueNode eulerExpans = new ComplexValueNode(Math.cos(N1.getComplex()),Math.sin(N1.getComplex()));
				
				
				
				Multiplication multi = new Multiplication();
				
				outputNode = multi.operation(new ValueNode(Math.exp(N1.getReal())),eulerExpans,outputNode);			
			
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
