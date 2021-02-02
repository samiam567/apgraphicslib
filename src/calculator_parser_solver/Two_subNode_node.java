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
	
	protected double operation(double a, double b) {
		Exception e = new Exception("Operation method was not overriden for child of Two_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
	
	private double operation(EquationNode nodeA, EquationNode nodeB, char paramPlaceholder) {
		if ( (nodeA.getValueData() != null || nodeB.getValueData() != null) ) {
			// we have advanced data, see if we can perform this operation with it
			
			if (nodeA.getValueData() != null) {
				setValueData(nodeA.getValueData().calculateOperation(this,nodeB,false)); // Perform advanced data operation
			}else { // if nodeA is a normal value we need to use nodeB to do the calculation
				setValueData(nodeB.getValueData().calculateOperation(this,nodeA,true));
			}
			
			if (getValueData() != null ) {
				return getValueData().getValue();
			}else {
				System.out.println("WARNING: AdvancedValueNode " + nodeA.getValueData().getName() + " (Class= " + nodeA.getValueData().getClass() + " ) has no writen implementation for " + getClass() + " with data type " + nodeA.getValueData().getClass());
				return operation(nodeA.getValue(),nodeB.getValue()); //value should have been set earlier by Two_subNode_node.protected AdvancedValueNode operation(EquationNode a)
			}
		}else {
			
			return operation(nodeA.getValue(),nodeB.getValue());
		}
	}
	
	
	
}
