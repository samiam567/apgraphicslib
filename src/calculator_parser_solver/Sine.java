package calculator_parser_solver;

public class Sine extends One_subNode_node {
	
	public Sine() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		return Math.sin(a);
	}
	
	public String toString() {
		return "sin";
	}
}