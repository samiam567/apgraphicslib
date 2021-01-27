package calculator_parser_solver;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * {@summary extra methods and data only used when the user is controlling the calculator}
 * @author apun1
 *
 */
public class Commands {
	
	private static final String commands = "/help, /move, /degRadMode";
	
	
	private static ArrayList<Variable> variables = new ArrayList<Variable>(); // variables that the user has declared
	
	/**
	 * {@summary a user declared variable, used to replace variables in user-entered equations with their values}
	 * @author apun1
	 */
	private static class Variable {
		private String name;
		private double value;
		public Variable(String name, double value) {
			this.name = name;
			this.value = value;
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
			addVariable(commandInput);
		}
		
	}

	private static void output(String message, Equation eq) {
		JOptionPane.showMessageDialog(eq.calculatorAnchor,message);
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
		eq.useRadiansNotDegrees = ! eq.useRadiansNotDegrees;
		if (eq.useRadiansNotDegrees) {
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
			equation.setVariableValue(var.name, var.value);
		}
	}
	
	public static void addVariable(String commandInput) {
		
		String name = "";
		double value = -0;
		
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
				}else {
					name = c;
					foundName = true;
				}			
			}
			
			//get the value
			String valueS = "";
			for (i = i+1; i < commandInput.length(); i++) {
				c = commandInput.substring(i,i+1); // get the next character
				if (c.equals(" ")) continue;
				valueS += c; // append the character to the value (String version)
			}
			
			try {
				value = Double.parseDouble(valueS);
				foundValue = true;
			}catch(NumberFormatException n) {
				System.out.println("failed to parse variable value ( Commands.addVariable(String) )");
			}
			
		}else {
			foundValue = false;
			foundName = false;
		}
		
		if (foundValue && foundName) {
			variables.add(new Variable(name,value));
		}else {
			(new Exception("bad command format")).printStackTrace();
		}
			
	}
}
