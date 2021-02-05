package calculator_parser_solver;

/**
 * {@summmary runs an operation on what ever is inside it. ex. [ <nodedata> ]
 * @author apun1
 *
 */
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
}
