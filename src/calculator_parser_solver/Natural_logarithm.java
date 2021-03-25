package calculator_parser_solver;

public class Natural_logarithm extends One_subNode_node {
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println(toString() + a);

		return Math.log(a);
	}
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode outputNode) {
		
		if ( (n1 instanceof AdvancedValueNode && ( (AdvancedValueNode) n1).needsSpecialOperationConditions)) {
			if (false) { 
				// put class-specific cases here
			}else {
				Equation.warn("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass());
				outputNode.setValue(operation(n1.getValue()));
			}
		}else {
			outputNode.setValue(operation(n1.getValue()));
		}
		
		return outputNode;
		
	}	
	
	public String toString() {
		return "ln";
	}
}
