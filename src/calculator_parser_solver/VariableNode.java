package calculator_parser_solver;



/**
 * {@summary a value node can be a number or a variable in an equation}
 * @author apun1
 *
 */
public class VariableNode extends ValueNode {
	private String name = "number";

	protected boolean unsetVal = true;
	
	public VariableNode(String name, int parenthesisLevel) {
		super('k');
		setParenthesisLevel(parenthesisLevel);
		this.name = name;
		unsetVal = true;
		orderOfOpsLevel = Equation.operations.length;
		
	}
	
	public VariableNode(double value, int parenthesisLevel) {
		super(value);
		setParenthesisLevel(parenthesisLevel);
		unsetVal = false;
		orderOfOpsLevel = Equation.operations.length;
	}

	/*
	public void setValueData(ValueNode newData) {
		super.setValueData(newData);
	
	}
*/
	public double getValue() {	
		return value;
	}
	
	
	@Override
	public String toString() {
		if (name.equals("number")) {
			return "" + getValue();
		}else {
			return " " + name + ":" + getValue() + " ";
		}
	}

	public void setName(String newName) {
		this.name = newName;
	}
	public String getName() {
		return name;
	}
}
