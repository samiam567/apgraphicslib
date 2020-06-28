package calculator_parser_solver;

public class Root extends Two_subNode_node {
	protected double operation(double a, double b) {
		return Math.pow(a, 1/b);
	}
}