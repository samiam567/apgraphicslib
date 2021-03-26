package calculator_parser_solver;

public class ParallelImpedanceAdd extends FunctionNode {
	private static Addition add = new Addition();
	@Override
	public ValueNode function(EquationNode[] params, ValueNode outputNode) {
		
		// add up 1 / each impedance
		
		ValueNode divisionResult = new ValueNode(0);
		ValueNode additionResult = new ValueNode(0);
		ValueNode one = new ValueNode(1);
		for (int i = 0; i < params.length; i++) {
			one.setValue(1); //just in case this gets altered at any point
			divisionResult = new Division().operation(one,params[i].getValueData(),divisionResult);
			additionResult = new Addition().operation(additionResult,divisionResult,new ValueNode(0));
		}
		
		
		//do 1 / that sum
		one.setValue(1);
		outputNode = new Division().operation(one, additionResult,outputNode);
		
		return outputNode;
		
	}
}
