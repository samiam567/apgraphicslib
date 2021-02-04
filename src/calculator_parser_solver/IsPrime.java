package calculator_parser_solver;

/**
 * {@summary checks if the number is prime - returns 1 if true, 0 if false}
 * @author apun1
 *
 */

public class IsPrime extends One_subNode_node {	
	
	public int isPrime(double a) {
		int aInt = (int) a;
		assert aInt == a;
		
		for (int i = 2; i <= aInt/2; i++) {
			if (aInt % i == 0) {
				return 0;
			}
		}
		return 1;
	}
	
	@Override
	protected double operation(double a) {
		return isPrime(a);
	}
	/*
	@Override
	public double getValue() {
		if (! isCalculated()) {
			value = operation(getSubNode().getValue()); 
			return value;
		} else {
			return value;
		}
	}*/
	
	public String toString() {
		return "isPrime";
	}
}
