package calculator_parser_solver;


/**
 * {@summary a node with two children (an operation like +,-,*,/,etc) }
 */
public abstract class Two_subNode_node extends EquationNode {
	private EquationNode leftSubNode;
	private EquationNode rightSubNode;
	
	public EquationNode getLeftSubNode() {
		return leftSubNode;
	}
	
	public EquationNode getRightSubNode() {
		return rightSubNode;
	}
	
	/**
	 * {@summary sets the node and makes this the passed node's parent}
	 * @param lft
	 */
	public void setLeftSubNode(EquationNode lft) {
		leftSubNode = lft;
		leftSubNode.setParent(this);
	}
	
	/**
	 * {@summary sets the node and makes this the passed node's parent}
	 * @param rgt
	 */
	public void setRightSubNode(EquationNode rgt) {
		rightSubNode = rgt;
		rightSubNode.setParent(this);
	}
	
	
	/**
	 * {@summary gets the value}
	 * {@code will not recalculate this and subNode tree if this node is already calculated}
	 */
	@Override
	public double getValue() {
		if (! isCalculated()) {
			value = operation(leftSubNode.getValue(), rightSubNode.getValue()); 
			calculated();
		}
		
		return value;
	}
	
	protected double operation(double a, double b) {
		Exception e = new Exception("Operation method was not overriden for child of Two_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
}
