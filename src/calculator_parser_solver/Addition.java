package calculator_parser_solver;

public class Addition extends Two_subNode_node {
	@Override
	protected double operation(double a, double b) {
		return a+b;
	}
}
