package calculator_parser_solver;

public class Pow extends Two_subNode_node {
	protected double operation(double a, double b) {
		return Math.pow(a,b);
	}
}

