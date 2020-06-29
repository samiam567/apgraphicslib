package calculator_parser_solver;

public class SquareRoot extends One_subNode_node {
	
	public SquareRoot() {
		orderOfOpsLevel = 3;
	}
	
	protected double operation(double a) {
		return Math.sqrt(a);
	}
	
	public String toString() {
		return "sqrt";
	}
}
