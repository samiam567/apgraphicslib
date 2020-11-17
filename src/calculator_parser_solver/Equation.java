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
	
	private ArrayList<ValueNode> variables;
	
	public static final boolean JOptionPane_error_messages = true;
	public static final boolean printInProgress = false;
	
	private static JFrame calculatorAnchor;	
	private PrintStream out = System.out;
		
	private static double prevAns = 0; //used by the main method
	private EquationNode[] nodes;
	
	/**
	 * {@summary testing method}
	 * @param equation
	 */
	public static void main(String[] args) { 
	
		System.out.println("Test took " + testCalculator() + " nanos");
		
		calculatorAnchor = new JFrame();
	
		calculatorAnchor.setVisible(true);
		calculatorAnchor.setSize(300,10);
		calculatorAnchor.setTitle("Calculator Parser/Solver - Programmed by Alec Pannunzio");
		
		String input = "";
		
		while (true) { //if the user presses cancel the program will automatically terminate
			while (input.length() == 0) {
				input = JOptionPane.showInputDialog(calculatorAnchor,"Type in what you want to solve");
				
				if (input.equals("/move")) {
					JOptionPane.showMessageDialog(calculatorAnchor, "Press ok to be able to move the calculator for a limited amount of time");
					
					try {
						Thread.sleep(3500);
					}catch(InterruptedException i) {
						i.printStackTrace();
					}
					input = "";
				}else if (input.isBlank() || input.contains("exit")) {
					System.out.println("terminating");
					calculatorAnchor.dispose();
					System.exit(1);
					System.out.println("exited");
				}
			}
		
			System.out.println("Input: " + input);
			
			
			
			try {
				Equation eq = new Equation(input);
				prevAns = eq.solve();
				JOptionPane.showMessageDialog(calculatorAnchor, eq.value);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("terminating because of an exception");
				calculatorAnchor.dispose();
				System.out.println("exited");
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
		orderOfOpsLevel = 0; //we are the top level
		setParenthesisLevel(0);
		
		variables = new ArrayList<ValueNode>();
		createTree(equation);
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
		
		
		//create nodes
		
		while(equation.contains("ans")) {
			equation = equation.replace("ans", "" + prevAns);
		}
		
		nodes = new EquationNode[0];
		
		variables.clear();

		
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
					}else {
						Exception e = new Exception("operation not found in operations array: " + inputBuffer);
						e.printStackTrace(out);
						if (JOptionPane_error_messages) JOptionPane.showMessageDialog(calculatorAnchor, e.toString() + "\n" + e.getStackTrace().toString());
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
			node = new Sine();
			break;
		case("cos"):
			node = new Cosine();
			break;
		case("tan"):
			node = new Tangent();
			break;
		case("asin"):
			node = new ArcSine();
			break;
		case("acos"):
			node = new ArcCosine();
			break;
		case("atan"):
			node = new ArcTangent();
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
	 * 
	 * @return the solution to the equation. If the equation has been solved before and no modifications have been made, it will simply return the value of the previous calculation.
	 */
	public double solve() {
		if (printInProgress) out.println(getValue());
		return getValue();
	}
	
	@Override
	public double operation(double a) {
		return a;
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
		Equation e8 = new Equation("atan(_sqrt(3))"); //testing arctan
		Equation e9 = new Equation("4*10^_31*sin24"); //testing negative exponents
		Equation e10 = new Equation("1 + _ans * 4");

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
		
		if (e5.solve() == Math.pow( 4,1/(Math.tan(Math.atan(0.12))) ) + 13-Math.sqrt(4) ){
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
	
}
