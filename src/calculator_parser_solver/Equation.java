package calculator_parser_solver;

import java.util.LinkedList;

/**
 * {@summary this equation class will store the tree of operations and values that make up an equation and it will be capable of solving itself with different values}
 * @author apun1
 *
 */
public class Equation extends One_subNode_node {
	
	public static final String[] operations = {"sin", "cos", "tan", "asin", "acos", "atan", "^", "rt", "sqrt", "*", "/", "+", "-" };
	private static String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static int[] numbers = {1,2,3,4,5,6,7,8,9,0};
	public static String[] numberChars = {"1","2","3","4","5","6","7","8","9","0",".",","};
	
	static final boolean printInProgress = false;
			
	private EquationNode[] nodes;
	
	/**
	 * {@summary testing method}
	 * @param equation
	 */
	public static void main(String[] args) { 
		Equation e = new Equation("1 + 1");  // start simple
		Equation e2 = new Equation("1 + 2 * 6^2"); //get a little more complicated
		Equation e3 = new Equation("((4^2*3-45)^(1+1*4) / 3) * 2"); //REALLY complicated
		Equation e4 = new Equation("45/2 + sin(10-5)/3"); //testing Sine
		Equation e5 = new Equation("4rt(sin(asin(0.12))) + 13-sqrt4");
		
		if (e.solve() == (1+1)) { 
			System.out.println("e worked!");
		}else {
			System.out.println("e failed :(");
		}
		
		if (e2.solve() == (1 + 2 * Math.pow(6,2))) { 
			System.out.println("e2 worked!");
		}else {
			System.out.println("e2 failed :(");
		}
		
		if (e3.solve() == ((Math.pow((Math.pow(4,2)*3-45),(1+1*4)) / 3) * 2 )) { 
			System.out.println("e3 worked!");
		}else {
			System.out.println("e3 failed :(");
		}
		
		if (e4.solve() == (45D/2 + Math.sin(10-5) / 3) ) {
			System.out.println("e4 worked!");
		}else {
			System.out.println("e4 failed :(");
		}
		
		if (e5.solve() == Math.pow( 4,1/(Math.sin(Math.asin(0.12))) ) + 13-Math.sqrt(4) ){
			System.out.println("e5 worked!");
		}else {
			System.out.println("e5 failed :(");
		}
	
	}	
	
	
	/**
	 * {@summary will create an Equation with the passed String}
	 * @param equation
	 */
	public Equation(String equation) {
		orderOfOpsLevel = 0; //we are the top level
		setParenthesisLevel(0);
		
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
					System.out.print("(");
				}
				
				for (int i = 0; i > (n.getParenthesisLevel()-parenthesisLevel); i--) {
					System.out.print(")");
				}
				parenthesisLevel = n.getParenthesisLevel();
			}
			System.out.print(n + " ");
		}
		System.out.println();
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
		
		nodes = new EquationNode[0];

		
		String mode = "unknown";
		String prevMode = "unknown";
		String inputBuffer = "";
		
		int parenthesisLevel = 0;
		
		String cChar = "";
		for (int i = 0; i < equation.length()+1; i++) {	
	
			if (i < equation.length()) {
				cChar = equation.substring(i, i+1);
					
				System.out.println(cChar);
				
				if (cChar.equals(" ")) continue; //skip spaces
					
					
					
				if ( cChar.equals("(") ) { //it is an open-parenthesis, and the parenthesis level goes up
					mode = "openParent";
					System.out.println("openParent");
				}else if ( cChar.equals(")") ) { //it is an end-parenthesis, and the parenthesis level goes down
					mode = "closeParent";
					System.out.println("closeParent");			
				}else if (indexOf(cChar,numberChars) != -1) { //it is a number, and must be a value
					mode = "numberInput";
					System.out.println("numbInput");
				}else if (indexOf(cChar,letters) != -1) { //it is a letter, and if it is a single letter it is a variable, but if there are multiple letters it is an operation
					if (! mode.equals("multi-char-operation")) {
						mode = "letterInput";
						System.out.println("letter");
						if (prevMode.equals("letterInput")) {
							mode = "multi-char-operation";
							System.out.println("multi-char-operation");
						}
					}
				}else {
					mode = "operation";
					System.out.println("operation");	
				}
			}else { //we are at the end of the equation
				mode = "end";
			}
			
			
			
			if ((! prevMode.equals(mode)) && (! prevMode.equals("unknown")) ){
				System.out.println("modeChange:" + inputBuffer);
				if (prevMode.equals("letterInput") && (! mode.equals("multi-char-operation"))) { //create a variable 
					addToNodesArray(new ValueNode(inputBuffer,parenthesisLevel)); //add a new ValueNode with the variable name
					inputBuffer = ""; //clear the inputBuffer
				}else if (prevMode.equals("numberInput")) { //create a value
					addToNodesArray(new ValueNode(Double.parseDouble(inputBuffer),parenthesisLevel)); //add a new ValueNode with the variable name
					inputBuffer = ""; //clear the inputBuffer
				}else if (prevMode.equals("operation") || prevMode.equals("multi-char-operation") ) {
					if (indexOf(inputBuffer,operations) != -1) {
						addToNodesArray(createOperation(inputBuffer,parenthesisLevel));
					}else {
						Exception e = new Exception("operation not found in operations array: " + inputBuffer);
						e.printStackTrace();
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
		
		printNodeArray(nodes);
		
		if (parenthesisLevel > 0) {
			Exception e = new Exception("ParenthesisError: missing close-parenthesis");
			e.printStackTrace();
		}else if (parenthesisLevel < 0) {
			Exception e = new Exception("ParenthesisError: missing open-parenthesis");
			e.printStackTrace();
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
		printNodeArray(arr);
		
		//find the lowest level node/operation
		EquationNode lowestNode = arr[0];
		EquationNode n;
		int lowestIndx = 0;
		for (int i = 0; i < arr.length; i++) {
			n = arr[i];
			if ( n.getLevel() < lowestNode.getLevel() ) {
				lowestNode = n;
				lowestIndx = i;
			}
		}
		
		System.out.println("lowestNode: " + lowestNode + " lowestIndx: " + lowestIndx);
		
		try {
			Two_subNode_node node = (Two_subNode_node) lowestNode;
			System.out.println("two_subnode");
			node.setLeftSubNode(getTree(resizeNodesArray(arr,0,lowestIndx-1)));
			node.setRightSubNode(getTree(resizeNodesArray(arr,lowestIndx+1,arr.length-1)));
		}catch(ClassCastException c) {
			try {
				One_subNode_node node = (One_subNode_node) lowestNode;
				System.out.println("one_subnode");
				if (lowestIndx != 0) {
					Exception e = new Exception("there should be nothing to the right of a lowest-priority single-node operation");
					e.printStackTrace();
				}
				
				node.setSubNode(getTree(resizeNodesArray(arr,lowestIndx+1,arr.length-1)));
			}catch(ClassCastException e) {
				System.out.println("valueNode");
			}
		}
		
		
		return lowestNode;
	}
	
	private EquationNode createOperation(String op, int parenthesisLevel) {
		EquationNode node = null;
		
		switch (op) {
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
		default:
			Exception e = new Exception("operation not found in createOperation: " + op);
			e.printStackTrace();
			break;
			
		}
		
		node.setParenthesisLevel(parenthesisLevel);
		
		return node;
	}
	
	public double solve() {
		return getValue();
	}
	
	@Override
	public double operation(double a) {
		return a;
	}
	
	
}
