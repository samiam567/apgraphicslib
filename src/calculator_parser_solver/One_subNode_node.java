package calculator_parser_solver;

public class One_subNode_node extends EquationNode {
	
	private EquationNode subNode;
	
	public One_subNode_node() {
		orderOfOpsLevel = 4;
	}
	
	public EquationNode getSubNode() {
		subNode.getValue();
		return subNode;
	}
	
	public void setSubNode(EquationNode sub) {
		subNode = sub;
		subNode.setParent(this);
		notCalculated();
	}

	@Override
	public double getValue() {
		if (! isCalculated()) {
			calculated();
			value = operation(getSubNode(),'1'); 
		}
		
		return value;
	}
	

	protected double operation(double a) {
		Exception e = new Exception("Operation method was not overriden for child of One_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
	
	private double operation(EquationNode nodeA, char paramPlaceholder) {
		
		if (nodeA.getValueData() != null) {
			// we have advanced data, see if we can perform this operation with it
			
			setValueData(operation(nodeA.getValueData())); // Perform advanced data operation
			
			if (getValueData() != null) {
				return getValueData().getValue();
			}else { //this means that we have no written advanced data operation for this node
				return value; //value should have been set earlier by One_subNode_node.protected AdvancedValueNode operation(EquationNode a)
			}
		}else{
			return operation(nodeA.getValue());
		}
	}
	
	/**
	 * {@summary if this is not overridden already by child classes, displays a warning and calls operation with the value of the passed EquationNode}
	 * @param a
	 * @return the result of this operation
	 */
	protected AdvancedValueNode operation(AdvancedValueNode nodeA) {
		System.out.println("WARNING: One_subNode_node child " + getClass() + " has not overriden operation(EquationNode a)");
		value = operation(nodeA.getValue());
		return null;
	}
	
}
