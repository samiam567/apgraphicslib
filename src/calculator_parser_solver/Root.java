package calculator_parser_solver;

public class Root extends Two_subNode_node {
	
	public Root() {
		orderOfOpsLevel = 3;
	}
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "rt" + b);
		return Math.pow(b, 1/a);
	}
	
	public String toString() {
		return "rt";
	}
}