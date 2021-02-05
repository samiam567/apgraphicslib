package calculator_parser_solver;

public class Sandwich_operatorNode extends EquationNode {

	
	protected EquationNode[] subNodes;
	
	
	public EquationNode[] getSubNodes() {
		return subNodes;
	}
	@Override
	public double getValue() {
		Exception e = new Exception("getValue() not overriden for class " + getClass());
		e.printStackTrace();
		return 1;
	}
	
	public Sandwich_operatorNode(ValueNode[] values) {
		//TODO parse through values and generate matrix
		
		Exception e = new Exception("Method not written");
		e.printStackTrace();
	}
	
	public Sandwich_operatorNode(Equation equation, String data, int parenthesisLevel ) {
		setParenthesisLevel(parenthesisLevel);
		
		
		String[] subEquations = data.split(",");
		
		subNodes = new EquationNode[subEquations.length];
		
		for (int i = 0; i < subEquations.length; i++) {
			System.out.println(subEquations[i]);
			subNodes[i] = equation.recursiveCreateTree(subEquations[i]);
		}
		
	}
	
	
	/**
	 * {@summary will return the portion of the input string that is between the sandwich operators}
	 * @param input
	 * @return
	 */
	public static int getSandwichSubString(String input,String beginChar, String endChar) {
		System.out.println("getting sandwich from: " + input);
		int level = 1, i = 1;
		String cChar;
		for (i = 1; i < input.length(); i++) {
			cChar = input.substring(i,i+1);
			
			if (cChar.equals(beginChar)) {
				level++;
			}else if (cChar.equals(endChar)) {
				level--;
			}
			
			
			if (level == 0) break; //we have reached this sandwich's end character
		}
		
		if (level != 0) {
			Exception e = new Exception("Bad sandwich!! Missing a close character - input: " + input);
			e.printStackTrace();
		}
		
		return i-1;
	}
	
}