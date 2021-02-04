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
			getValueData().setValue(operation(getSubNode(),'k')); 
		}
		
		return valueData.getValue();
	}
	

	protected double operation(double a) {
		Exception e = new Exception("Operation method was not overriden for child of One_subNode_node");
		e.printStackTrace();
		return 0;
	}
	
	
	private double operation(EquationNode nodeA, char paramPlaceholder) {
		
		if (nodeA.getValueData() instanceof AdvancedValueNode) {
			// we have advanced data, see if we can perform this operation with it
			
			setValueData(operation( nodeA.getValueData().getClass().cast(nodeA.getValueData()) )); // Perform advanced data operation
			
			return getValueData().getValue();
		}else{ // we did not encounter advanced data so do operation normally
			return operation(nodeA.getValue());
		}
	}
	
	private ValueNode operation(ValueNode nodeA) {
		System.out.println("WARNING: " + getClass() + " has no implementation for AdvancedValueNode of type " + nodeA.getClass() + ". Resorting to normal simple-operation");
		getValueData().setValue(operation(nodeA.getValue())); // do operation normally and assign value to our ValueNode
		return getValueData();
	}
	

	
}
