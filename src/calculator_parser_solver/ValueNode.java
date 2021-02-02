package calculator_parser_solver;

import javax.swing.JOptionPane;

/**
 * {@summary a value node can be a number or a variable in an equation}
 * @author apun1
 *
 */
public class ValueNode extends EquationNode {
	private String name = "number";

	protected boolean unsetVal = false;
	
	public ValueNode(String name) {
		unsetVal = true;
		this.name = name;
		orderOfOpsLevel = Equation.operations.length;
	}
	
	public ValueNode(String name, int parenthesisLevel) {
		unsetVal = true;
		this.name = name;
		setParenthesisLevel(parenthesisLevel);
		orderOfOpsLevel = Equation.operations.length;
	}
	
	public ValueNode(double v) {
		setValue(v);
		orderOfOpsLevel = Equation.operations.length;
	}
	
	public ValueNode(double v, int parenthesisLevel) {
		setValue(v);
		setParenthesisLevel(parenthesisLevel);
		orderOfOpsLevel = Equation.operations.length;
	}
	
	@Override 
	public double getValue() {
		if (! isCalculated()) {
			if (unsetVal) { //make sure this node's value was set
				Exception e = new Exception("Variable-node " + name + " never had its value set");
				e.printStackTrace();
				if (Equation.JOptionPane_error_messages) JOptionPane.showMessageDialog(null,e);
			}
			
			calculated();
		}
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@summary sets unsetVal. This method should ONLY be used in SPECIFIC circumstances where using other methods is not an option}
	 * @param unsetVal
	 * @param key
	 */
	public void setUnsetVal(boolean unsetVal, char key) {
		assert key == 'k'; //makes sure you know what you're doing when you use this method
		this.unsetVal = unsetVal;
	}
	
	@Override
	public long getLevel() {
		return Long.MAX_VALUE;
	}
	
	public boolean unsetVal() {
		return unsetVal;
	}
	
	public void setValue(double value) {
		if (getParent() != null) getParent().notCalculated();
		this.value = value;
		calculated();
		unsetVal = false;
	}
	
	public String toString() {
		if (name.equals("number")) {
			return "" + value;
		}else {
			return " " + name + ":" + value + " ";
		}
	}
}
