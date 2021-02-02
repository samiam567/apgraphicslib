package calculator_parser_solver;


/**
 * {@summary signifies that this ValueNode stores more information than a regular ValueNode}
 * {@code essentially it takes more than one double to represent this value ex. complex numbers, matrices, etc}
 * @author samiam567
 */
public class AdvancedValueNode extends ValueNode {
	
	/**
	 * @param name
	 * @param key ensures that this constructor can only be used by child classes
	 */
	public AdvancedValueNode(String name, char key) {
		super(name);
		assert key == 'k';
		super.setValueData(this);
	}
	
	
	/**
	 * @param name
	 * @param key ensures that this constructor can only be used by child classes
	 */
	public AdvancedValueNode(char key) {
		super("AdvancedValueNode");
		assert key == 'k';
		super.setValueData(this);
	}
	
	/**
	 * @param name
	 * @param key ensures that this constructor can only be used by child classes
	 */
	public AdvancedValueNode(double val, char key) {
		super("AdvancedValueNode");
		assert key == 'k';
		setValue(val);
		super.setValueData(this);
	}
	
	
	/**
	 * @return a non-aliased (brand new) copy of this AdvancedValueNode with all identical data
	 *//*
	public AdvancedValueNode copy() {
		Exception e = new Exception("copy() was not overridden (and must be) in child of AdvancedValueNode");
		e.printStackTrace();
		return null;
	}*/
	
	//this method should be overridden since by definition this value can not be represented solely by it's single double representation
	@Override
	public String toString() {
		Exception e = new Exception("toString() was not overridden (and must be) in child of AdvancedValueNode");
		e.printStackTrace();
		return super.toString();
	}
	

	@Override
	public void setValueData(AdvancedValueNode valueData) {
		Exception e = new Exception("AdvancedValueNode is its own valueData so setValueData() should never be called.");
		e.printStackTrace();
	}
	
	
	public AdvancedValueNode calculateOperation(One_subNode_node operation) {
		return null; // we do not have a defined implementation for this operation
	}
	
	/**
	 * 
	 * @param operation
	 * @param nodeB
	 * @param reverseParamOrder if the equation was originally a [operation] b and we called b.calculateOperation(a). This would be because a is not an AdvancedValueNode
	 * @return
	 */
	public AdvancedValueNode calculateOperation(Two_subNode_node operation, EquationNode nodeB, boolean reverseParamOrder) {
		return null; // we do not have a defined implementation for this operation
	}
	
}
