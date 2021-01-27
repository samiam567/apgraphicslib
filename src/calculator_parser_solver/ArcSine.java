package calculator_parser_solver;

public class ArcSine extends One_subNode_node {
	Equation equation;
	
	public ArcSine(Equation equation) {
		this.equation = equation;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("asin" + a);
		
		if (equation.useRadiansNotDegrees) {
			return Math.asin(a);
		}else {
			return 180 * Math.asin(a)/Math.PI;
		}
	}
	
	public String toString() {
		return "asin";
	}
}
