package calculator_parser_solver;

public class IsEqualTo extends Two_subNode_node {
	public IsEqualTo() {
		orderOfOpsLevel = 0;
	}
	
	@Override
	protected double operation(double a, double b) {
		return a == b ? 1 : 0;
	}
	
	@Override
	protected ValueNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
		if (! (outputNode instanceof Comparation)) outputNode = new Comparation();
		
		if ( (n1 instanceof AdvancedValueNode && ( (AdvancedValueNode) n1).needsSpecialOperationConditions) || (n2 instanceof AdvancedValueNode && ( (AdvancedValueNode) n2).needsSpecialOperationConditions) ) {
			Equation.warn("class " + IsEqualTo.class + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());

			if (operation(n1.getValue(),n2.getValue()) == 1) {
				((Comparation) outputNode).setValue(Comparation.ComparationValues.True);
			}else {
				((Comparation) outputNode).setValue(Comparation.ComparationValues.False);
			}
		}else {
			if (operation(n1.getValue(),n2.getValue()) == 1) {
				((Comparation) outputNode).setValue(Comparation.ComparationValues.True);
			}else {
				((Comparation) outputNode).setValue(Comparation.ComparationValues.False);
			}
		}
		
		return outputNode;
	}
	
	@Override
	public String toString() {
		return "==";
	}
}