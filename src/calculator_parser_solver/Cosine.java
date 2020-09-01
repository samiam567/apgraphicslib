package calculator_parser_solver;

public class Cosine extends One_subNode_node {
	
	public Cosine() {

	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("cos" + a);
		return Math.cos(a);
	}
	
	public String toString() {
		return "cos";
	}
	
}