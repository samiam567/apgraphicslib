package calculator_parser_solver;

public class Tangent extends One_subNode_node {
	Equation equation;
	
	public Tangent(Equation equation) {
		this.equation = equation;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("tan" + a);

		if (equation.useRadiansNotDegrees) {
			return Math.tan(a);
		}else {
			return Math.tan(a*Math.PI / 180);
		}
	}
	
	public String toString() {
		return "tan";
	}
	
}