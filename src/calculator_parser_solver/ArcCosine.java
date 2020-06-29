package calculator_parser_solver;

public class ArcCosine extends One_subNode_node {
	
	public ArcCosine() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		return Math.acos(a);
	}
	
	public String toString() {
		return "acos";
	}
}