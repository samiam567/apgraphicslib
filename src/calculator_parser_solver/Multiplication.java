package calculator_parser_solver;

public class Multiplication extends Two_subNode_node {
	
	public Multiplication() {
		orderOfOpsLevel = 2;
	}
	
	protected double operation(double a, double b) {
		return a*b;
	}
	
	public String toString() {
		return "*";
	}
}
