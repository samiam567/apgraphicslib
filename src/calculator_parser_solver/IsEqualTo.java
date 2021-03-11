package calculator_parser_solver;

public class IsEqualTo extends Two_subNode_node {
	public IsEqualTo() {
		orderOfOpsLevel = 0;
	}
	
	@Override
	protected double operation(double a, double b) {
		return a == b ? 1 : 0;
	}
	
	protected static double operationStat(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "<=>" + b);
		return a+b;
	}
}