package calculator_parser_solver;

public class Cosine extends One_subNode_node {
	
	public Cosine() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		return Math.cos(a);
	}
	
	public String toString() {
		return "cos";
	}
	
}