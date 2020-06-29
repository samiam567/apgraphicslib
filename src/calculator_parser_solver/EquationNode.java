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
		if ((getParent() != null) ) parent.notCalculated(); //if we have a parent, they need to redo their calculation as well
		
		calculated = false;
	}
	
	public void setParent(EquationNode parent) {
	    notCalculated();
		this.parent = parent;
	}
	
	public EquationNode getParent() {
		return parent;
	}
	
	
	/**
	 * {@summary gets the value of this node and evaluates all children}
	 * {@code will be overridden by some children}
	 * @return
	 */
	public double getValue() {
		calculated = true;
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


}
