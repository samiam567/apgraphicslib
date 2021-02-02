package calculator_parser_solver;

/**
 * @author apun1
 *
 */
public abstract class EquationNode {
	private int parenthesisLevel;
	protected int orderOfOpsLevel;
	private EquationNode parent;
	protected double value;
	private AdvancedValueNode valueData = null;
	
	private boolean calculated = false;
	
	
	public boolean isCalculated() {
		return calculated;
	}
	
	protected void calculated() {
		calculated = true;
	}
	
	/**
	 * {@summary sets us to not calculated, and makes all parents of this node un-calculated as well}
	 */
	protected void notCalculated() {
		calculated = false;
		
		if ((getParent() != null) ) parent.notCalculated(); //if we have a parent, they need to redo their calculation as well

	}
	
	public void setParent(EquationNode parent) {
	    notCalculated();
		this.parent = parent;
	}
	
	public EquationNode getParent() {
		return parent;
	}
	
	public AdvancedValueNode getValueData() {
		getValue();
		return valueData;
	}
	
	/**
	 * {@summary tells the node to evaluate and evaluates all children and returns the current value}
	 * {@code will be overridden by some children}
	 * @return
	 */
	public double getValue() {
		if (! calculated) {
			Exception e = new Exception("getValue() was not overriden by some children");
			e.printStackTrace();
			calculated = true;
		}
		return value;
	}


	public int getParenthesisLevel() {
		return parenthesisLevel;
	}

	protected void setParenthesisLevel(int parenthesisLevel) {
		this.parenthesisLevel = parenthesisLevel;
	}

	public int getOrderOfOpsLevel() {
		return orderOfOpsLevel;
	}
	
	public long getLevel() {
		return parenthesisLevel * Equation.operations.length + orderOfOpsLevel;
	}
	
	
	/**
	 * {@summary this should be overridden to display the operation symbol (*,+,-.etc) or the value if the node is not an operation
	 */
	@Override
	public String toString() {
		return "" + getValue();
	}
	
	/**
	 * {@summary this should be overridden to display the data that the node stores} 
	 * @return
	 */
	public String getDataStr() {
		if (getValueData() == null) {
			return "" + getValue();
		}else{
			return getValueData().toString();
		}
	}


	public void setValueData(AdvancedValueNode valueData) {
		this.valueData = valueData;
	}


}
