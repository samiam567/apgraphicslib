package calculator_parser_solver;

public class Division extends Two_subNode_node {
	
	public Division() {
		orderOfOpsLevel = 2;
	}
	
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "/" + b);
		return a/b;
	}
	
	public String toString() {
		return "/";
	}
}