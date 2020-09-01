package calculator_parser_solver;

public class ArcCosine extends One_subNode_node {
	
	public ArcCosine() {

	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("acos" + a);
		return Math.acos(a);
	}
	
	public String toString() {
		return "acos";
	}
}