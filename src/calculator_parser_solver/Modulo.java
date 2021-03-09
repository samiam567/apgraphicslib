package calculator_parser_solver;

public class Modulo extends Two_subNode_node {
	
	public Modulo() {
		orderOfOpsLevel = 2;
	}
	
	@Override
	public double operation(double a, double b) {
		System.out.println("operation");
		return a % b;
	}
	
	public String toString() {
		return "%";
	}
}
