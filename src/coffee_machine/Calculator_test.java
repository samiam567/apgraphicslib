package coffee_machine;

import calculator_parser_solver.Equation;

public class Calculator_test {
	
	public static void main(String[] args) {
		
		Equation eq = new Equation("x+1");
		eq.setVariableValue("x",1);
	
		System.out.println("---------------");
		
		System.out.println(eq.getDataStr());
		
	}
}
