package calculator_parser_solver;

public class CompareTo extends Two_subNode_node {
	public CompareTo() {
		orderOfOpsLevel = 0;
	}
	
	@Override
	protected double operation(double a, double b) {
		if (a > b) {
			return 1;
		}else if (a < b) {
			return -1;
		}else{
			return 0;
		}
	}
	
	protected static double operationStat(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "<=>" + b);
		return a+b;
	}
}
