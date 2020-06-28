package calculator_parser_solver;


/**
 * {@summary a value node can be a number or a variable in an equation}
 * @author apun1
 *
 */
public class ValueNode extends EquationNode {
	
	public void setValue(double value) {
		getParent().notCalculated();
		this.value = value;
		calculated();
	}
	
}
