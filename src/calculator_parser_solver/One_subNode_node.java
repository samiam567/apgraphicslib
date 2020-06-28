package calculator_parser_solver;

public class One_subNode_node extends EquationNode {
	
	private EquationNode subNode;
	
	public EquationNode getSubNode() {
		return subNode;
	}
	
	public void setSubNode(EquationNode sub) {
		subNode = sub;
		notCalculated();
	}

	@Override
	public double getValue() {
		if (! isCalculated()) {
			value = operation(subNode.getValue()); 
			calculated();
		}
		return value;
	}
	
	protected double operation(double a) {
		Exception e = new Exception("Operation method was not overriden for child of One_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
}
