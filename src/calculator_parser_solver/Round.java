package calculator_parser_solver;

//round(value,numDecimals) or round(value) 
public class Round extends functionNode {
	
	private ValueNode round(ValueNode value, int precision, ValueNode outputNode) {
		if (value instanceof Matrixable) {
			if (! (outputNode instanceof Matrixable)) outputNode = new Bra();
			((Matrixable) outputNode).setValues(((Matrixable) value).getValues());
			
			ValueNode indValue;
			for (int i = 0; i < ((Matrixable) value).getValues().length; i++) {
				indValue = ((Matrixable) value).getValues()[i];
				((Matrixable) outputNode).getValues()[i] = round(indValue,precision,((Matrixable) outputNode).getValues()[i]);
			}
		}else {
			outputNode.setValue(Math.round( Math.pow(10,precision) * value.getValue() )  / Math.pow(10,precision));
		}
		
		return outputNode;
	}
	
	public ValueNode function(EquationNode[] params, ValueNode outputNode) {
		Equation.Assert(params.length <= 2, "Round can take a max of two parameters. Throwing away extra params");
		if (! Equation.Assert(params.length != 0, "Round must have at least one parameter")) return outputNode;
		
		
		if (params.length == 2) {
			outputNode = round(params[0].getValueData(),(int) Math.round(params[1].getValue()),outputNode);
		}else {
			outputNode = round(params[0].getValueData(),0,outputNode);
		}
		
		return outputNode;
	}
}
