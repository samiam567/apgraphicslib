package calculator_parser_solver;

//round(value,numDecimals) or round(value) 
public class Round extends functionNode {
	public ValueNode function(EquationNode[] params, ValueNode outputNode) {
		Equation.Assert(params.length <= 2, "Round can take a max of two parameters. Throwing away extra params");
		if (! Equation.Assert(params.length != 0, "Round must have at least one parameter")) return outputNode;
		
		
		if (params.length == 2) {
			outputNode.setValue(  Math.round( Math.pow(10,Math.round(params[1].getValue())) * params[0].getValue() )  / Math.pow(10,Math.round(params[1].getValue())) );
		}else {
			outputNode.setValue(Math.round(params[0].getValue()));
		}
		
		return outputNode;
	}
}
