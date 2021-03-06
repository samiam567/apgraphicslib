package calculator_parser_solver;

// percentError(experimental, theoretical)
public class PercentError extends functionNode {
	
	public ValueNode function(EquationNode[] params, ValueNode outputNode) {
		if (! Equation.Assert(params.length == 2, "Must have 2 and only 2 parameters for percent error.")) return new ValueNode(-1);
		System.out.println(params[0]);
		double experimental = params[0].getValue();
		double actual = params[1].getValue();
		
		outputNode.setValue((experimental-actual)/actual * 100);
		
		return outputNode;
	}
}
