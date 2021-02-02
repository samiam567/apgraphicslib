package calculator_parser_solver;

public class ArcCosine extends One_subNode_node {
	Equation equation;
	
	public ArcCosine(Equation equation) {
		this.equation = equation;
	}
	
	protected static double operation(double a) {
		if (Equation.printInProgress) System.out.println("acos" + a);
		
		if (Equation.useRadiansNotDegrees) {
			return Math.acos(a);
		}else {
			return 180 * Math.acos(a)/Math.PI;
		}
	}
	
	public String toString() {
		return "acos";
	}
}