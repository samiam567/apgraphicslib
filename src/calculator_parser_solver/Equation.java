package calculator_parser_solver;

import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * {@summary this equation class will store the tree of operations and values that make up an equation and it will be capable of solving itself with different values}
 * @author apun1
 *
 */
public class Equation extends One_subNode_node {
	
	public static final String[] operations = {"_","isPrime","rand","abs","sin", "cos", "tan", "asin", "acos", "atan", "^", "rt", "sqrt", "*", "/", "+", "-" };
	private static String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static int[] numbers = {1,2,3,4,5,6,7,8,9,0};
	public static String[] numberChars = {"1","2","3","4","5","6","7","8","9","0",".",","};
	
	
	private ArrayList<ValueNode> variables;	// just a list of the variables for quick access
	private EquationNode[] nodes;
	
	PrintStream out = System.out;
	
	//calculator settings 
	public static boolean JOptionPane_error_messages = true;
	public static final boolean printInProgress = false;
	public static boolean useRadiansNotDegrees = true;
	
	//used by the runUserCalculator method
	JFrame calculatorAnchor;
	EquationNode prevAns = new ValueNode(0); 
	
	
	/**
	 * {@summary testing method}
	 * @param equation
	 */
	public static void main(String[] args) { 
		JOptionPane_error_messages = true;
		(new Equation()).runUserCalculator();
	}
	
	public void runUserCalculator() {
		
		// put some constants into the variables as a default
		Commands.enableJFrameOutput = false; //be quiet about it
		Commands.addVariable("/pi=3.14159265358979323846264",this); // pi
		Commands.addVariable("/e=2.7182818284590452353602874713527",this); // e
		Commands.addVariable("/h=6.62607004*10^_34",this); // plank's constant
		Commands.addVariable("i", new ComplexValueNode(0,1), this);
		Commands.enableJFrameOutput = true;
		
		out.println("Test took " + testCalculator() + " nanos to evaluate equations");
		
		
		calculatorAnchor = new JFrame();
	
		calculatorAnchor.setVisible(true);
		calculatorAnchor.setSize(300,10);
		calculatorAnchor.setTitle("Calculator Parser/Solver - Programmed by Alec Pannunzio");
		
		String input = "";
		
		while (true) { //if the user presses cancel the program will automatically terminate
			while (input.length() == 0) {
				input = JOptionPane.showInputDialog(calculatorAnchor,"Type in what you want to solve");
				
				
				if (input == null || input.isBlank() || input.contains("exit")) {
					out.println("terminating");
					calculatorAnchor.dispose();
					System.exit(1);
					out.println("exited");
				}else if (input.substring(0,1).equals("/")) {
					Commands.parseCommand(input,this);
					input = "";
				}
			}
		
			out.println("Input: " + input);
			
			
			
			try {
				createTree(input);
				Commands.applyVariables(this);
				prevAns = evaluate();
				JOptionPane.showMessageDialog(calculatorAnchor, toString());
				out.println("Output: " + toString());
			}catch(Exception e) {
				e.printStackTrace();
				out.println("terminating because of an exception");
				calculatorAnchor.dispose();
				out.println("exited");
				System.exit(1);
			}
			
			input = "";
		}
		
	}	
	
	
	/**
	 * {@summary will create an Equation with the passed String}
	 * @param equation
	 */
	public Equation(String equation) {
		variables = new ArrayList<ValueNode>();
		createTree(equation);
	}
	
	public Equation() {
		variables = new ArrayList<ValueNode>();
	}
	
	/**
	 * {@summary resizes the passed array to the given constraints. Can lengthen the array}
	 * @param array
	 * @param start
	 * @param end
	 * @return
	 */
	private EquationNode[] resizeNodesArray(EquationNode[] arr, int start, int end) {
		EquationNode[] prevArray = arr;
		arr = new EquationNode[1+end-start];
		
		for (int i = start; i <= end; i++) {
			try {
				arr[i-start] = prevArray[i];
			}catch(ArrayIndexOutOfBoundsException a) {/*okay yeah this is a lazy way of doing this but it'll work every time*/}
		}
		
		return arr;
	
	}
	
	private void printNodeArray(EquationNode[] arr) {
		int parenthesisLevel = 0;
	
		for (EquationNode n : arr) {
			
			if (n.getParenthesisLevel() != parenthesisLevel) {
				for (int i = 0; i < (n.getParenthesisLevel()-parenthesisLevel); i++) {
					out.print("(");
				}
				
				for (int i = 0; i > (n.getParenthesisLevel()-parenthesisLevel); i--) {
					out.print(")");
				}
				parenthesisLevel = n.getParenthesisLevel();
			}
			out.print(n + " ");
		}
		out.println();
	}
	
	/**
	 * {@summary searches for a String in an array and returns the index of that string in the array}
	 * {@code returns -1 if the String was not found}
	 * @param str the string to search for
	 * @param arr the array to search in
	 */
	private int indexOf(String str,String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) {
				return i;
			}
		}
		return -1;
	}
	
	private void addToNodesArray(EquationNode n) {
		nodes = resizeNodesArray(nodes, 0,nodes.length);
		nodes[nodes.length-1] = n;
	}

	/**
	 * {@summary recursively adds the child nodes to the parent}
	 * @param equation
	 * @param parent
	 */
	public void createTree(String equation) {
		variables.clear();
		
		if (equation.length() == 0) {
			Exception e = new Exception("Cannot create a tree with an equation String of length 0");
			e.printStackTrace();
			return;
		}
		
		orderOfOpsLevel = 0; //we are the top level
		setParenthesisLevel(0);
		
		//create nodes
		
		while(equation.contains("ans")) {
			equation = equation.replace("ans", "" + prevAns);
		}
		
		nodes = new EquationNode[0];
		
		

		
		String mode = "unknown";
		String prevMode = "unknown";
		String inputBuffer = "";
		
		int parenthesisLevel = 0;
		
		String cChar = "";
		for (int i = 0; i < equation.length()+1; i++) {	
	
			if (i < equation.length()) {
				cChar = equation.substring(i, i+1);
					
				if (printInProgress) out.println(cChar);
				
				if (cChar.equals(" ")) continue; //skip spaces
					
					
					
				if ( cChar.equals("(") ) { //it is an open-parenthesis, and the parenthesis level goes up
					mode = "openParent";
					if (printInProgress) out.println("openParent");
				}else if ( cChar.equals(")") ) { //it is an end-parenthesis, and the parenthesis level goes down
					mode = "closeParent";
					if (printInProgress) out.println("closeParent");			
				}else if (indexOf(cChar,numberChars) != -1) { //it is a number, and must be a value
					mode = "numberInput";
					if (printInProgress) out.println("numbInput");
				}else if (indexOf(cChar,letters) != -1) { //it is a letter, and if it is a single letter it is a variable, but if there are multiple letters it is an operation
					if (! mode.equals("multi-char-operation")) {
						mode = "letterInput";
						if (printInProgress) out.println("letter");
						if (prevMode.equals("letterInput")) {
							mode = "multi-char-operation";
							if (printInProgress) out.println("multi-char-operation");
						}
					}
				}else {
					mode = "operation";
					if (printInProgress) out.println("operation");	
				}
			}else { //we are at the end of the equation
				mode = "end";
			}
			
			
			if (indexOf(inputBuffer,operations) != -1) { //catch if we have two operations in a row ex: 1 + sin(25) or 1 + _3
				addToNodesArray(createOperation(inputBuffer,parenthesisLevel,mode));
				inputBuffer = "";
				prevMode = "unknown";
			}
			
			
			if ((! prevMode.equals(mode)) && (! prevMode.equals("unknown")) ){
				if (printInProgress) out.println("modeChange:" + inputBuffer);
				if (prevMode.equals("letterInput") && (! mode.equals("multi-char-operation"))) { //create a variable 
					ValueNode newVariable = new ValueNode(inputBuffer,parenthesisLevel);
					variables.add(newVariable);
					addToNodesArray(newVariable); //add a new ValueNode with the variable name
					inputBuffer = ""; //clear the inputBuffer
				}else if (prevMode.equals("numberInput")) { //create a value
					addToNodesArray(new ValueNode(Double.parseDouble(inputBuffer),parenthesisLevel)); //add a new ValueNode with the variable name
					inputBuffer = ""; //clear the inputBuffer
				}else if (prevMode.equals("operation") || prevMode.equals("multi-char-operation") ) {
					if (indexOf(inputBuffer,operations) != -1) {
						addToNodesArray(createOperation(inputBuffer,parenthesisLevel,mode));
					}else { //treat as a variable name
						
						//this new code treats multi-char strings that aren't operations as variables
						ValueNode newVariable = new ValueNode(inputBuffer,parenthesisLevel);
						variables.add(newVariable);
						addToNodesArray(newVariable); //add a new ValueNode with the variable name
						
						/* this code treated multi-char strings that aren't operations as an error
						Exception e = new Exception("operation not found in operations array: " + inputBuffer);
						e.printStackTrace(out);
						if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
						*/
					}
					inputBuffer = ""; //clear the inputBuffer
				}
			}
			
			if (mode.equals("openParent")) {
				parenthesisLevel++;
			}else if (mode.equals("closeParent")) {
				parenthesisLevel--;
			}else {		
				inputBuffer += cChar;
			}
			
			prevMode = mode;
			 
			
		}
		
		if (printInProgress) printNodeArray(nodes);
		
		if (parenthesisLevel > 0) {
			Exception e = new Exception("ParenthesisError: missing close-parenthesis");
			e.printStackTrace(out);
			if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
		}else if (parenthesisLevel < 0) {
			Exception e = new Exception("ParenthesisError: missing open-parenthesis");
			e.printStackTrace(out);
			if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
		}
		
		
		//create tree linkups
		
		setSubNode(getTree(nodes));
		
		
	}
	
	/**
	 * {@summary recursively returns the tree of the passed node array}
	 * {@code 1. search for the lowest level node/operation -----
	     2.a if it is a single subnode, getTree(everything right of it) is its subnode -----
	     2.b if it is a double subnode, getTree(everything left of it) is its left subnode, and getTree(everything right of it) is its left subnode
		}
	 * @param arr
	 * @return
	 */
	private EquationNode getTree(EquationNode[] arr) {
		if (printInProgress) printNodeArray(arr);
		
		//find the lowest level node/operation
		EquationNode lowestNode = arr[0];
		EquationNode n;
		int lowestIndx = 0;
		for (int i = arr.length-1; i > 0; i--) {
			n = arr[i];
			if ( n.getLevel() < lowestNode.getLevel() ) {
				lowestNode = n;
				lowestIndx = i;
			}
		}
		
		if (printInProgress) out.println("lowestNode: " + lowestNode + " lowestIndx: " + lowestIndx);
		
		try {
			Two_subNode_node node = (Two_subNode_node) lowestNode;
			if (printInProgress) out.println("two_subnode");
			node.setLeftSubNode(getTree(resizeNodesArray(arr,0,lowestIndx-1)));
			node.setRightSubNode(getTree(resizeNodesArray(arr,lowestIndx+1,arr.length-1)));
		}catch(ClassCastException c) {
			try {
				One_subNode_node node = (One_subNode_node) lowestNode;
				if (printInProgress) out.println("one_subnode");
				if (lowestIndx != 0) {
					Exception e = new Exception("there should be nothing to the left of a lowest-priority single-node operation");
					e.printStackTrace(out);
					if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + "Node: " + node.toString() + "\nOpsLvl: " + node.orderOfOpsLevel);
				}
				
				node.setSubNode(getTree(resizeNodesArray(arr,lowestIndx+1,arr.length-1)));
			}catch(ClassCastException e) {
				if (printInProgress) out.println("valueNode");
			}
		}
		
		
		return lowestNode;
	}
	
	private EquationNode createOperation(String op, int parenthesisLevel, String mode) {
		EquationNode node = null;
		
		switch (op) {
		case("_"):
			node = new Negative(mode);
			break;
		case("sin"):
			node = new Sine(this);
			break;
		case("cos"):
			node = new Cosine(this);
			break;
		case("tan"):
			node = new Tangent(this);
			break;
		case("asin"):
			node = new ArcSine(this);
			break;
		case("acos"):
			node = new ArcCosine(this);
			break;
		case("atan"):
			node = new ArcTangent(this);
			break;
		case("^"):
			node = new Pow();
			break;
		case("rt"):
			node = new Root();
			break;
		case("sqrt"):
			node = new SquareRoot();
			break;
		case("*"):
			node = new Multiplication();
			break;
		case("/"):
			node = new Division();
			break;
		case("+"):
			node = new Addition();
			break;
		case("-"):
			node = new Subtraction();
			break;
		case("abs"):
			node = new Absolute_Value();
			break;
		case("rand"):
			node = new Rand();
			break;
		case("isPrime"):
			node = new IsPrime();
			break;
		default:
			Exception e = new Exception("operation not found in createOperation: " + op);
			e.printStackTrace(out);
			if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
			break;
			
		}
		
		node.setParenthesisLevel(parenthesisLevel);
		
		return node;
	}
	
	/**
	 * @return the solution to the equation. If the equation has been solved before and no modifications have been made, it will simply return the value of the previous calculation.
	 */
	public double solve() {
	
		if (Double.isNaN(getValue()))  (new Exception("Calculation returned NaN")).printStackTrace();
		if (printInProgress) out.println(getValue());
		
		return getValue();
	}
	
	
	/**
	 * {@summary calculates the entire tree and returns the top level node}
	 * @return
	 */
	public EquationNode evaluate() {
		getSubNode().getValue();
		return getSubNode();
	}
	
	
	@Override
	public double operation(double a) {
		return a;
	}
	
	/**
	 *  {@summary replaces all variables with the passed name with the passed AdvancedValueNode}
	 * @param varName the name of the variables to replace
	 * @param value the AdvancedValueNode to replace the variables with 
	 */
	public boolean setAdvancedVariableValue(String varName, AdvancedValueNode value) {
		boolean varFound = false;
		
		for (ValueNode n : variables) {
			if (n.getName().equals(varName)) {
				n.setValueData(value);
				n.setValue(value.getValue());
			
				n.setName(varName);
				
				varFound = true;
			}
		}
		
		return varFound;
	}

	/**
	 * {@summary sets ALL variables in the equation with the passed name to the passed value.}
	 * {@code the equation will automatically know to recalculate itself}
	 * @param varName
	 * @param value
	 * @return
	 */
	public boolean setVariableValue(String varName, double value ) {
		boolean varFound = false;
		for (ValueNode n : variables) {
			if (n.getName().equals(varName)) {
				n.setValue(value);
				varFound = true;
			}
		}	
		return varFound;
	}
	
	/**
	 * {@summary sets the value of the variable with the passed index to the passed value}
	 * @param varIndx
	 * @param value
	 */
	public void setVariableValue(int varIndx, double value) {
		try {
			variables.get(varIndx).setValue(value);
		}catch(IndexOutOfBoundsException i) {
			Exception e = new Exception("Variable index not found. That Variable dosen't exist. Indx: " + varIndx);
			e.printStackTrace(out);
			if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
		}
	}


	/**
	 * @param varName
	 * @return the index of the first instance of a variable with the passed name, -1 if the variable does not appear in the equation
	 */
	public int getVariableIndex(String varName) {
		ValueNode n;
		for (int i = 0; i < variables.size(); i++) {
			n = variables.get(i);
			if (n.getName().equals(varName)) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	public ValueNode getVariable(int varIndx) {
		try {
			return variables.get(varIndx);
		}catch(IndexOutOfBoundsException i) {
			Exception e = new Exception("Variable index not found. That Varaible dosen't exist. Indx: " + varIndx);
			e.printStackTrace(out);
			if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private static long testCalculator() {
		System.out.println("Testing calculator to ensure accuracy...");
		
		System.out.println("Building equations...");
		boolean successful = true;
		
		Equation e1 = new Equation("1 + 1");  // start simple
		Equation e2 = new Equation("1 + 2 * 6^2"); //get a little more complicated, test negative exponents
		Equation e3 = new Equation("((4^2*3-45)^(1+1*4) / 3) * 2"); //REALLY complicated
		Equation e4 = new Equation("45/2 + sin(10-5)/3"); //testing Sine
		Equation e5 = new Equation("4rt(tan(atan(0.12))) + 13-sqrt4"); //test sin, asin,sqrt,rt
		Equation e6 = new Equation("sqrt(3^2 + 4^2) * ( (abs(3)/3 * abs(4)/4) * (_abs(3)/3 + _abs(4)/4) - 1)"); //test abs and negatives
		Equation e7 = new Equation("1/2*3/2"); //test left to right execution
		Equation e8 = new Equation("atan(_(2rt(3)))"); //testing arctan
		Equation e9 = new Equation("4*10^_31*sin24"); //testing negative exponents
		Equation e10 = new Equation("1 + _ans * 4");
		Equation e11 = new Equation("sin8+cos9+tan11*asin0.1+acos0.2+atan0.453"); //test all trig functions
		Equation e12 = new Equation("isPrime(342)"); //test isPrime

		long start = System.nanoTime();
			
		System.out.println("equations built. Testing accuracy...");
		if (e1.solve() == (1+1)) { 
			System.out.println("e1 worked!");
		}else {
			System.out.println("e1 failed :(");
			successful = false;
		}
		
		if (e2.solve() == (1 + 2 * Math.pow(6,2))) { 
			System.out.println("e2 worked!");
		}else {
			System.out.println("e2 failed :(");
			successful = false;
		}
		
		if (e3.solve() == ((Math.pow((Math.pow(4,2)*3-45),(1+1*4)) / 3) * 2 )) { 
			System.out.println("e3 worked!");
		}else {
			System.out.println("e3 failed :(");
			successful = false;
		}
		
		if (e4.solve() == (45D/2 + Math.sin(10-5) / 3) ) {
			System.out.println("e4 worked!");
		}else {
			System.out.println("e4 failed :(");
			successful = false;
		}
		
		if (e5.solve() == ( Math.pow( (Math.tan(Math.atan(0.12))),1.0/4 ) + 13-Math.sqrt(4) ) ){
			System.out.println("e5 worked!");
		}else {
			System.out.println("e5 failed :(");
			successful = false;
		}
		
		if (e6.solve() ==  Math.sqrt(3*3 + 4*4) * ( (Math.abs(3)/3 * Math.abs(4)/4) * (-Math.abs(3)/3 + -Math.abs(4)/4) - 1)){
			System.out.println("e6 worked!");
		}else {
			System.out.println("e6 failed :(");
			successful = false;
		}
		
		if (e7.solve() ==  1D/2*3/2){
			System.out.println("e7 worked!");
		}else {
			System.out.println("e7 failed :(");
			successful = false;
		}
		
		
		if (e8.solve() ==  Math.atan(-Math.sqrt(3))){
			System.out.println("e8 worked!");
		}else {
			System.out.println("e8 failed :(");
			successful = false;
		}
	
		if (e9.solve() ==  4*Math.pow(10, -31)*Math.sin(24)){
			System.out.println("e9 worked!");
		}else {
			System.out.println("e9 failed :(");
			successful = false;
		}
		
	
		if (e10.solve() ==  1 + -e9.solve() * 4){
			System.out.println("e10 worked!");
		}else {
			System.out.println("e10 failed :(");
			successful = false;
		}
	
		if (e11.solve() ==  Math.sin(8)+Math.cos(9)+Math.tan(11)*Math.asin(0.1) + Math.acos(0.2) + Math.atan(0.453)) {
			System.out.println("e11 worked!");
		}else {
			System.out.println("e11 failed :(");
			successful = false;
		}
		
		if (e12.solve() == 0) {
			System.out.println("e12 worked!");
		}else {
			System.out.println("e12 failed :(");
			successful = false;
		}
		
		e1.createTree("((4^2*3-45)^(1+1*4) / 3) * 2"); //test equation reusability
		
		if (e1.solve() == ((Math.pow((Math.pow(4,2)*3-45),(1+1*4)) / 3) * 2 )) { 
			System.out.println("e1 test2 worked!");
		}else {
			System.out.println("e1 test2 failed :(");
			successful = false;
		}
		
		if (successful) {
			System.out.println("test complete. All systems functional");
		}else {
			System.out.println("test FAILED. One or more equations gave an incorrect answer.");
			JOptionPane.showMessageDialog(null, "Calculator Test failed. One or more equations did not yield a correct answer");
		}
		
		long end = System.nanoTime();
		
		
		
		return end-start;
	}
		

	public void setPrintStream(PrintStream outputStream) {
		out = outputStream;
	}
	
	@Override
	public String toString() {
		return evaluate().getDataStr();
	}
	
}
