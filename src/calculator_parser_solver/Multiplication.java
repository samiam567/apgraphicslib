package calculator_parser_solver;

public class Multiplication extends Two_subNode_node {
	
	public Multiplication() {
		orderOfOpsLevel = 2;
	}
	
	protected double operation(double a, double b) {
		if (Equation.printInProgress) System.out.println(a + "*" + b);
		return a*b;
	}
	
	public String toString() {
		return "*";
	}
	
	protected ValueNode operation(ValueNode n1, ValueNode n2, ValueNode outputNode) {
			
		if (n1 instanceof AdvancedValueNode || n2 instanceof AdvancedValueNode) {
			if (Equation.printInProgress) System.out.println(n1.getDataStr() + toString() + n2.getDataStr());
			
			
			if (false) {//n1 instanceof MatrixNode && n2 instanceof MatrixNode) {
				// both matrices
				
			}else if (n1 instanceof MatrixNode) {
				
				// only n1 matrixNode
				MatrixNode N1 = (MatrixNode) n1;
				
				
				if (! (outputNode instanceof MatrixNode)) outputNode = new MatrixNode(N1.getRows().length, N1.getColumns().length);
				

				for (int i = 0; i < N1.getRows().length; i++) {
					((MatrixNode) outputNode).getRows()[i].setValues(((Bra) operation(N1.getRows()[i],n2,((MatrixNode) outputNode).getRows()[i])));
				}
				
			}else if (n2 instanceof MatrixNode) {
				// only n2 matrixNode
				
				MatrixNode N2 = (MatrixNode) n2;
				
				
				if (! (outputNode instanceof MatrixNode)) outputNode = new MatrixNode(N2.getRows().length, N2.getColumns().length);
				

				for (int i = 0; i < N2.getRows().length; i++) {
					((MatrixNode) outputNode).getRows()[i] = ((Bra) operation(N2.getRows()[i],n1,((MatrixNode) outputNode).getRows()[i]));
				}
				
				
			}else if (n1 instanceof Bra && n2 instanceof Ket) {
				// inner product
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
				
			}else if (n1 instanceof Ket && n2 instanceof Bra) {
				// outer product
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
				
			}else if (n1 instanceof Bra && ! (n2 instanceof Bra) ) {
				Bra N1 = (Bra) n1;
				if (! (outputNode instanceof Bra) ) outputNode = new Bra( N1.size() );
				//Bra * number
				for (int i = 0; i < N1.size(); i++) {
					((Bra)outputNode).setValue(i,operation(N1.getValue(i),n2,((Bra)outputNode).getValue(i)));
				}
				
			}else if (false) {//n1 instanceof Ket && ! (n2 instanceof Ket)) {
				
			}else if (false) {//n2 instanceof Bra && ! (n1 instanceof Bra) ) {
				
			}else if (false) {//n2 instanceof Ket && ! (n1 instanceof Ket)) {
				
			}else if (n1 instanceof ComplexValueNode && n2 instanceof ComplexValueNode) { 
				// both complex numbers
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal()-((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getComplex(), ((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getComplex()+((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getReal());
			}else if (n1 instanceof ComplexValueNode) { 
				// only n1 Complex Number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n1).getReal()*((ComplexValueNode) n2).getReal(), ((ComplexValueNode) n1).getComplex()*((ComplexValueNode) n2).getReal());
			}else if (n2 instanceof ComplexValueNode) { 
				// only n2 complex number
				if (! (outputNode instanceof ComplexValueNode) ) outputNode = new ComplexValueNode();
				((ComplexValueNode) outputNode).setValues(((ComplexValueNode) n2).getReal()*((ComplexValueNode) n1).getReal(), ((ComplexValueNode) n2).getComplex()*((ComplexValueNode) n1).getReal());
			}else {
				System.out.println("WARNING: class " + getClass() + " has no implementation for AdvancedValueNodes of class " + n1.getClass() + " and " + n2.getClass());
				outputNode.setValue(operation(n1.getValue(),n2.getValue()));
			}
			
		
		}else { //they are just normal values
			outputNode.setValue(operation(n1.getValue(),n2.getValue()));
		}
		
		return outputNode;
	}
}
