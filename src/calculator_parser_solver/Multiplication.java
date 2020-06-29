package calculator_parser_solver;

public class Multiplication extends Two_subNode_node {
	
	public Multiplication() {
		orderOfOpsLevel = 2;
	}
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "*" + b);
		return a*b;
	}
	
	public String toString() {
		return "*";
	}
}
