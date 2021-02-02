package calculator_parser_solver;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * {@summary extra methods and data only used when the user is controlling the calculator}
 * @author apun1
 *
 */
public class Commands {
	
	private static final String commands = "/help, /exit, /move, /degRadMode, /[insert 1-character variable name here] = [insert equation here]";
	
	static boolean enableJFrameOutput = true;
	
	static ArrayList<Variable> variables = new ArrayList<Variable>(); // variables that the user has declared
	
	
	/**
	 * {@summary a user declared variable, used to replace variables in user-entered equations with their values}
	 * @author apun1
	 */
	private static class Variable {
		private String name;
		private EquationNode value;
		public Variable(String name, EquationNode value2) {
			this.name = name;
			this.value = value2;
		}
	}
	public static void parseCommand(String commandInput, Equation eq) {
		String cIn = commandInput.toLowerCase();
		if (cIn.contains("move")) {
			move(eq);
		}else if (cIn.contains("degradmode")) {
			degRadMode(eq);
		}else if (cIn.contains("help")) {
			output("Possible commands are: " + commands,eq);
		}else if (cIn.contains("=")) {
			addVariable(commandInput,eq);
		}else {
			output("Command unrecognized",eq);
		}
		
	}

	private static void output(String message, Equation eq) {
		if (enableJFrameOutput) JOptionPane.showMessageDialog(eq.calculatorAnchor,message);
		eq.out.println(message);
	}
	
	private static void move(Equation eq) {
		JOptionPane.showMessageDialog(eq.calculatorAnchor, "Press ok to be able to move the calculator for a limited amount of time");
		
		try {
			Thread.sleep(3500); //wait for the user to move the window before continuing execution
		}catch(InterruptedException i) {
			i.printStackTrace();
		}
	
	}
	
	private static void degRadMode(Equation eq) {
		Equation.useRadiansNotDegrees = ! Equation.useRadiansNotDegrees;
		if (Equation.useRadiansNotDegrees) {
			output("Using Radians", eq);
		}else {
			output("Using Degrees", eq);
		}
	}
	
	
	/**
	 * {@summary applies the user-created variables to an equation}
	 * @param equation
	 */
	public static void applyVariables(Equation equation) {	
		for (Variable var : variables) {
			if (AdvancedValueNode.class.isAssignableFrom(var.value.getClass())) {
				equation.setAdvancedVariableValue(var.name,(AdvancedValueNode) var.value);
			}else{
				equation.setVariableValue(var.name, var.value.getValue());
			}
		}
	}
	
	public static void addVariable(String commandInput, Equation eq) {
		
		String name = "";
		EquationNode value = null;
		
		boolean foundName = false;
		boolean foundValue = false;
		
		if (commandInput.substring(0,1).equals("/")) { //make sure we are dealing with a line-zero command slash
			
		//iterate through the command  -  should be in the form /[varName]=[number] where varname is one char long
		
			//find the name (should be next character 
			String c;
			int i; //needed outside this loop
			for (i = 1; i < commandInput.length(); i++) {
				c = commandInput.substring(i,i+1); // get the next character
				
				if (c.equals(" ")) {
					continue; // skip spaces
				}else if (c.equals("=")) { //we have reached the equals
					break;
				}else { // this is part of the variable name
					name += c; 
					foundName = true;
				}			
			}
			
			Equation varEq = new Equation();
			varEq.prevAns = eq.prevAns; // make sure we can use prevAns
			varEq.createTree(commandInput.substring(i+1,commandInput.length()));
			applyVariables(varEq); // make sure we have all our variables and constants
			value = varEq.evaluate();
			foundValue = true;
		
		}else {
			foundValue = false;
			foundName = false;
		}
		
		if (foundValue && foundName) {
			variables.add(new Variable(name,value));
			output("Variable " + name + " is now set to " + value.getDataStr(), eq);
		}else {
			(new Exception("bad command format")).printStackTrace();
		}
			
	}
}
