package calculator_parser_solver;

import java.util.LinkedList;

/**
 * {@summary this equation class will store the tree of operations and values that make up an equation and it will be capable of solving itself with different values}
 * @author apun1
 *
 */
public class Equation extends One_subNode_node {
	
	private String[] operations = { "(", ")", "sin", "cos", "tan", "asin", "acos", "atan", "^", "rt", "sqrt", "*", "/", "+", "-" };
	private String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static int[] numbers = {1,2,3,4,5,6,7,8,9,0};
			
	private LinkedList<EquationNode> nodes = new LinkedList<EquationNode>();
	
	/**
	 * {@summary testing method}
	 * @param equation
	 */
	public static void main(String[] args) { 
		Equation e = new Equation("1 + 1");  // start simple
		Equation e2 = new Equation("1 + 2 * 6^2"); //get a little more complicated
		Equation e3 = new Equation("((4^2*3-45)^(1+1*4) / 3) * 2 "); //REALLY complicated
		
		if (e.solve() == (1+1)) { 
			System.out.println("e worked!");
		}else {
			System.out.println("e failed :(");
		}
		
		if (e2.solve() == (1 + 2 * Math.pow(6,2))) { 
			System.out.println("e worked!");
		}else {
			System.out.println("e failed :(");
		}
		
		if (e3.solve() == (((4^2*3-45)^(1+1*4) / 3) * 2 )) { 
			System.out.println("e worked!");
		}else {
			System.out.println("e failed :(");
		}
	}
	
	
	/**
	 * {@summary will create an Equation with the passed String}
	 * @param equation
	 */
	public Equation(String equation) {
		setLevel(0); //we are the top level
		addNodesAt(equation,this);
	}
	
	/**
	 * {@summary recursively adds the child nodes to the parent}
	 * @param equation
	 * @param parent
	 */
	public void addNodesAt(String equation, EquationNode parent) {
		//TODO write this
		//this is the input method that will generate the node trees
		
		
		
		//To test we will just do one of the sample equations manually building the tree.
		//"1 + 2 * 6^2"
		
		Addition a = new Addition();
		setSubNode(a);
	
		a.setLeftSubNode(new ValueNode(1));
		
		Multiplication m = new Multiplication();
		a.setRightSubNode(m);
		m.setLeftSubNode(new ValueNode(2));
		
		
		Pow p = new Pow();
		m.setRightSubNode(p);
		
		p.setLeftSubNode(new ValueNode(6));
		p.setRightSubNode(new ValueNode(2));
		
		
		System.out.println(getValue());
		
		

		
	}
	
	
	public double solve() {
		return getValue();
	}
	
	@Override
	public double operation(double a) {
		return a;
	}
	
	
}
