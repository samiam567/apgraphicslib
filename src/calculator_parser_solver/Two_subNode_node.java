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
			value = operation(getLeftSubNode(), getRightSubNode(),'1'); 
		}
		
		return value;
	}
	
	protected static double operation(double a, double b) {
		Exception e = new Exception("Operation method was not overriden for child of Two_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
	
	private double operation(EquationNode nodeA, EquationNode nodeB, char paramPlaceholder) {
		if ( (nodeA.getValueData() != null && nodeB.getValueData() != null) && (AdvancedValueNode.class.isAssignableFrom(nodeA.getValueData().getClass()) && AdvancedValueNode.class.isAssignableFrom(nodeB.getValueData().getClass()))) {
		
			setValueData(operation( (AdvancedValueNode) nodeA.getValueData(), (AdvancedValueNode) nodeB.getValueData()));
			
			if (getValueData() != null ) {
				value = getValueData().getValue();
				return value;
			}else {
				return value; //value should have been set earlier by Two_subNode_node.protected AdvancedValueNode operation(EquationNode a)
			}
		}else {
			return operation(nodeA.getValue(),nodeB.getValue());
		}
	}
	
	/**
	 * {@summary if this is not overridden already by child classes, displays a warning and calls operation with the value of the passed EquationNode}
	 * @param a
	 * @return the result of this operation
	 */
	protected AdvancedValueNode operation(AdvancedValueNode a, AdvancedValueNode b) {
		System.out.println("WARNING: Two_subNode_node child " + getClass() + " has not overriden operation(EquationNode a)");
		value = operation(a.getValue(),b.getValue());
		return null;
	}
	
}
