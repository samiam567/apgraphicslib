package calculator_parser_solver;

public class Subtraction extends Two_subNode_node {
	
	public Subtraction() {
		orderOfOpsLevel = 1;
	}
	
	protected double operation(double a, double b) {
		return a-b;
	}
	
	public String toString() {
		return "-";
	}
}