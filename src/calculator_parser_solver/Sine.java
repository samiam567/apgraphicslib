package calculator_parser_solver;

public class Sine extends One_subNode_node {
	
	public Sine() {

	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("sin" + a);
		return Math.sin(a);
	}
	
	public String toString() {
		return "sin";
	}
}