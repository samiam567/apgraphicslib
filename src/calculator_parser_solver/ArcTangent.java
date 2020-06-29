package calculator_parser_solver;

public class ArcTangent extends One_subNode_node {

	public ArcTangent() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("atan" + a);
		return Math.atan(a);
	}
	
	public String toString() {
		return "atan";
	}
}