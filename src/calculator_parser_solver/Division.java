package calculator_parser_solver;

public class Division extends Two_subNode_node {
	
	public Division() {
		orderOfOpsLevel = 2;
	}
	
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "/" + b);
		return a/b;
	}
	
	public String toString() {
		return "/";
	}
	
	
	
	protected ValueNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
	
		if ( (n1 instanceof AdvancedValueNode && ( (AdvancedValueNode) n1).needsSpecialOperationConditions) || (n2 instanceof AdvancedValueNode && ( (AdvancedValueNode) n2).needsSpecialOperationConditions) )	{		
			if (n1 instanceof ComplexValueNode && n2 instanceof ComplexValueNode) { 
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues((((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n1).getImaginaryComponent()*((ComplexValueNode) n2).getImaginaryComponent())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n2).getImaginaryComponent()*((ComplexValueNode) n2).getImaginaryComponent()), (((ComplexValueNode) n1).getImaginaryComponent()*((ComplexValueNode) n2).getReal()-((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getImaginaryComponent())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n2).getImaginaryComponent()*((ComplexValueNode) n2).getImaginaryComponent()));
			}else if (n1 instanceof ComplexValueNode) { 
				// only n1 Complex Number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues((((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n1).getImaginaryComponent())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()), (((ComplexValueNode) n1).getImaginaryComponent()*((ComplexValueNode) n2).getReal()-((ComplexValueNode) n1).getReal())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()));
				
			}else if (n2 instanceof ComplexValueNode) { 
				// only n2 complex number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode(); //cast outputNode to the correct type 
				((ComplexValueNode) outputNode).setValues((((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n2).getImaginaryComponent())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n2).getImaginaryComponent()*((ComplexValueNode) n2).getImaginaryComponent()), (((ComplexValueNode) n2).getReal()-((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getImaginaryComponent())/(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n2).getReal()+((ComplexValueNode) n2).getImaginaryComponent()*((ComplexValueNode) n2).getImaginaryComponent()));
			}else {
				Equation.warn("class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
				outputNode.setValue(operation(n1.getValue(),n2.getValue()));
			}
			
		
		}else { //they are just normal values
			outputNode.setValue(operation(n1.getValue(),n2.getValue()));
		}
		
		return outputNode;
	}
	
}