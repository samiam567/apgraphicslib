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
		if (!isCalculated()) {
			calculated();
			setValueData(operation(getSubNode().getValueData(),getValueData()));
		}

		return valueData.getValue();
	}

	protected double operation(double a) {
		Equation.warn("Operation method was not overriden for child of One_subNode_node");
		return 0;
	}

	/*
	private double operation(EquationNode nodeA, char paramPlaceholder) {

		if (nodeA.getValueData() instanceof AdvancedValueNode) {
			// we have advanced data, see if we can perform this operation with it

			setValueData(operation(nodeA.getValueData().getClass().cast(nodeA.getValueData()))); // Perform advanced
																									// data operation

			return getValueData().getValue();
		} else { // we did not encounter advanced data so do operation normally
			return operation(nodeA.getValue());
		}
	}
*/
	
	/**
	 * 
	 * @param nodeA the value to calculate with
	 * @param outputNode will output to this node and return it if new node creation is not necessary
	 * @return
	 */
	protected ValueNode operation(ValueNode nodeA, ValueNode outputNode) {
		Equation.warn(getClass() + " has no implementation for generic ValueNode");
		outputNode.setValue(operation(nodeA.getValue())); // do operation normally and assign value to our ValueNode
		return outputNode;
	}

}
