package calculator_parser_solver;

public class Pow extends Two_subNode_node {
	
	public Pow() {
		orderOfOpsLevel = 3;
	}
	
	protected double operation(double a, double b) {
		return Math.pow(a,b);
	}
	
	public String toString() {
		return "pow";
	}
}

