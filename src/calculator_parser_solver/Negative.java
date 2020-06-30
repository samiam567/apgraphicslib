package calculator_parser_solver;

public class Negative extends One_subNode_node {
	
	public Negative() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("_" + a);
		return 0-a;
	}
	
	public String toString() {
		return "_";
	}
}