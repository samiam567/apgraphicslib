package calculator_parser_solver;

public class ArcSine extends One_subNode_node {
	
	public ArcSine() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		return Math.asin(a);
	}
	
	public String toString() {
		return "asin";
	}
}
