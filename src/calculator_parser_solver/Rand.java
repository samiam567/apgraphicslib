package calculator_parser_solver;

import java.util.Random;

/**
 * {@summary i'm not convinced this works}
 * @author apun1
 *
 */
@Deprecated
public class Rand extends One_subNode_node {
	
	Random rand;
	
	public Rand() {
		orderOfOpsLevel = 4;
		rand = new Random();
	}
	
	@Override
	protected double operation(double a) {
		if (Equation.printInProgress) System.out.println("rand" + a);
		rand.setSeed((long) a);
		return rand.nextDouble();
		
	}
	
	@Override
	public double getValue() {
		if (! isCalculated()) {
			value = operation(getSubNode().getValue()); 
			
			//make all of the parents not calculated so that we have to get a new rand number...
			notCalculated(); 
			//but this node is calculated so that we don't re-set the seed
			calculated();
			return value;
		}else {
			
			return rand.nextDouble();
		}
	}
	
	public String toString() {
		return "sin";
	}
}