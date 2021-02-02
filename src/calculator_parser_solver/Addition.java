package calculator_parser_solver;

public class Addition extends Two_subNode_node {
	
	public Addition() {
		orderOfOpsLevel = 1;
	}
	
	@Override
	protected double operation(double a, double b) {
		return Addition.operationStat(a,b);
	}
	
	protected static double operationStat(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "+" + b);
		return a+b;
	}
	
	public String toString() {
		return "+";
	}
}
