package calculator_parser_solver;

public class Tangent extends One_subNode_node {
	
	public Tangent() {

	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("tan" + a);
		return Math.tan(a);
	}
	
	public String toString() {
		return "tan";
	}
	
}