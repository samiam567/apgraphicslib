package calculator_parser_solver;

public class functionNode extends One_subNode_node {
	
	public void setSubNode(EquationNode sub) {
		super.setSubNode(sub);
		
		
	}
	
	private EquationNode[] getParams() {
		EquationNode[] params;
		if (getSubNode().getValueData() instanceof Matrixable) {
			params = ((Matrixable) getSubNode().getValueData()).getValues();
		}else {
			params = new EquationNode[] {getSubNode().getValueData()};
		}
		return params;
	}
	
	protected double operation(double a) {
		return function(getParams(), new ValueNode(0)).getValue();
	}
	
	
	public ValueNode function(EquationNode[] params, ValueNode outputNode) {
		Equation.warn("Function method was not overriden for child of functionNode " + getClass() );
		return null;
	}
	
	
	/**
	 * 
	 * @param nodeA the value to calculate with
	 * @param outputNode will output to this node and return it if new node creation is not necessary
	 * @return
	 */
	protected ValueNode operation(ValueNode nodeA, ValueNode outputNode) {
		return function(getParams(),outputNode);
	}
}
