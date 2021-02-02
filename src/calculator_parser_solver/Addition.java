package calculator_parser_solver;

public class Addition extends Two_subNode_node {
	
	public Addition() {
		orderOfOpsLevel = 1;
	}
	
	@Override
	protected static double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "+" + b);
		return a+b;
	}
	
	public String toString() {
		return "+";
	}
}
