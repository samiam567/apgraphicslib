package calculator_parser_solver;

public class Logarithm extends FunctionNode {
	
	private static Natural_logarithm logCalc = new Natural_logarithm();
	private static Division divCalc = new Division();
	@Override
	public ValueNode function(EquationNode[] values, ValueNode outputNode) {
		Equation.Assert(values.length == 2, "log takes two parameters, base and value ");
		
		outputNode = divCalc.operation(logCalc.operation(values[1].getValueData(),new ValueNode(1)) , logCalc.operation(values[0].getValueData(),new ValueNode(1)), outputNode);
		return outputNode;
	}
		
	public String toString() {
		return "log";
	}
}
