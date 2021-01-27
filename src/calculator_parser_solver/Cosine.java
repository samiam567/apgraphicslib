package calculator_parser_solver;

public class Cosine extends One_subNode_node {
	Equation equation;
	
	public Cosine(Equation equation) {
		this.equation = equation;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("cos" + a);

		if (equation.useRadiansNotDegrees) {
			return Math.cos(a);
		}else {
			return Math.cos(a*Math.PI / 180);
		}
	}
	
	public String toString() {
		return "cos";
	}
	
}