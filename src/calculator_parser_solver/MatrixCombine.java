package calculator_parser_solver;

/**
 * {@summary combines two matrixes together}
 * @author apun1
 *
 */
public class MatrixCombine extends Two_subNode_node {
	
	private Addition addition = new Addition();
	
	
	public MatrixCombine() {
		orderOfOpsLevel = 1;
	}
	
	@Override
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "+" + b);
		return a+b;
	}
	
	public String toString() {
		return "<+>";
	}
	
	
	
	// class-specific advanced operations
	
	@Override
	protected MatrixNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
		assert outputNode instanceof MatrixNode; // we can't combine into a non-matrix
			
		if (n1 instanceof AdvancedValueNode || n2 instanceof AdvancedValueNode) {
				
			if (Equation.printInProgress) System.out.println(n1.getDataStr() + toString() + n2.getDataStr());
			
			
			if (n1 instanceof Bra_ket && n2 instanceof Bra_ket) {
				// vector <+> vector
			
			}else if (n1 instanceof Bra_ket && n2 instanceof MatrixNode) {
				// vector <+> matrix
				
			}else if (n1 instanceof MatrixNode && n2 instanceof Bra_ket) {
				// matrix <+> vector
				
			}else if (n1 instanceof MatrixNode && n2 instanceof MatrixNode) {
				// matrix <+> matrix
			}else {
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
				outputNode.setValue(addition.operation(n1.getValue(),n2.getValue()));
			}
				
			
		}else { //they are just normal values
			outputNode.setValue(operation(n1.getValue(),n2.getValue()));
		}
			
		return (MatrixNode) outputNode;
	}
		
		

}
