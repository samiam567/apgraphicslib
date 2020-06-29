package calculator_parser_solver;

@Deprecated //current alg never uses this
public class Parenthesis extends One_subNode_node {
	
	public Parenthesis() {
		orderOfOpsLevel = 5;
	}
	
	protected double operation(double a) {
		return a;
	}
	
	public String toString() {
		return "()";
	}
}