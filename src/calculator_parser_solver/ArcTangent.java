package calculator_parser_solver;

public class ArcTangent extends One_subNode_node {
	Equation equation;
	
	public ArcTangent(Equation equation) {
		this.equation = equation;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("atan" + a);

		if (Equation.useRadiansNotDegrees) {
			return Math.atan(a);
		}else {
			return 180 * Math.atan(a)/Math.PI;
		}
	}
	
	public String toString() {
		return "atan";
	}
}