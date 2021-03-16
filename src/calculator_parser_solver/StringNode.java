package calculator_parser_solver;

public class StringNode extends AdvancedValueNode {
	
	protected String string;
	
	public StringNode(String string) {
		super('k');
		this.string = string;
	}
	
	@Override
	public double getValue() {
		return string.length();
	}
	
	@Override
	public String toString() {
		return string;
	}

}
