package calculator_parser_solver;


/**
 * {@summary a node with two children (an operation like +,-,*,/,etc) }
 */
public abstract class Two_subNode_node extends EquationNode {
	private EquationNode leftSubNode;
	private EquationNode rightSubNode;
	
	public EquationNode getLeftSubNode() {
		leftSubNode.getValue();
		return leftSubNode;
	}
	
	public EquationNode getRightSubNode() {
		rightSubNode.getValue();
		return rightSubNode;
	}
	
	/**
	 * {@summary sets the node and makes this the passed node's parent}
	 * @param lft
	 */
	public void setLeftSubNode(EquationNode lft) {
		leftSubNode = lft;
		leftSubNode.setParent(this);
		notCalculated();
	}
	
	/**
	 * {@summary sets the node and makes this the passed node's parent}
	 * @param rgt
	 */
	public void setRightSubNode(EquationNode rgt) {
		rightSubNode = rgt;
		rightSubNode.setParent(this);
		notCalculated();
	}
	
	
	/**
	 * {@summary gets the value}
	 * {@code will not recalculate this and subNode tree if this node is already calculated}
	 */
	@Override
	public double getValue() {
		
		if (! isCalculated()) {
			calculated();
			setValueData(operation(getLeftSubNode().getValueData(),getRightSubNode().getValueData(),getValueData())); 
		}
		
		return valueData.getValue();
	}
	
	protected double operation(double a, double b) {
		Equation.warn("Operation method was not overriden for child of Two_subNode_node");
		return 0;
	}
	
	/*
	private double operation(EquationNode nodeA, EquationNode nodeB, char paramPlaceholder) {
		if (nodeA.getValueData() instanceof AdvancedValueNode || nodeB.getValueData() instanceof AdvancedValueNode) {
			// we have advanced data, see if we can perform this operation with it
			
			setValueData(operation( nodeA.getValueData().getClass().cast(nodeA.getValueData()) , nodeB.getValueData().getClass().cast(nodeB.getValueData()) ));
			
			return getValueData().getValue();
		}else { // we did not encounter advanced data so do operation normally
			
			return operation(nodeA.getValue(),nodeB.getValue());
		}
	}
	*/
	protected ValueNode operation(ValueNode nodeA, ValueNode nodeB, ValueNode outputNode) {
		if (nodeA instanceof AdvancedValueNode || nodeB instanceof AdvancedValueNode) Equation.warn(getClass() + " has no implementation for generic ValueNodes");
		outputNode.setValue(operation(nodeA.getValue(), nodeB.getValue())); // do operation normally and assign value to our ValueNode
		return outputNode;
	}
	
	
}
