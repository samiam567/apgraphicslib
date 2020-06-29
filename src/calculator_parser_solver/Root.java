package calculator_parser_solver;

public class Root extends Two_subNode_node {
	
	public Root() {
		orderOfOpsLevel = 3;
	}
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "rt" + b);
		return Math.pow(a, 1/b);
	}
	
	public String toString() {
		return "rt";
	}
}