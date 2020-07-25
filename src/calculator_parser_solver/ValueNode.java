package calculator_parser_solver;


/**
 * {@summary a value node can be a number or a variable in an equation}
 * @author apun1
 *
 */
public class ValueNode extends EquationNode {
	private String name = "number";

	
	private boolean unsetVal = false;
	
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
		if (unsetVal) { //make sure this node's value was set
			Exception e = new Exception("Variable-node " + name + " never had its value set");
			e.printStackTrace();
		}
		
		return super.getValue();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public long getLevel() {
		return Long.MAX_VALUE;
	}
	
	public void setValue(double value) {
		unsetVal = false;
		if (getParent() != null) getParent().notCalculated();
		this.value = value;
		calculated();
	}
	
	public String toString() {
		if (name.equals("number")) {
			return "" + value;
		}else {
			return " " + name + ":" + value + " ";
		}
	}
}
