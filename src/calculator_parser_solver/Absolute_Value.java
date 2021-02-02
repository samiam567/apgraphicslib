package calculator_parser_solver;

public class Absolute_Value extends One_subNode_node {
	
	public Absolute_Value() {
		orderOfOpsLevel = 4;
	}
	
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("|" + a + "|");
		return Math.abs(a);
	}
	
	public String toString() {
		return "abs";
	}
}