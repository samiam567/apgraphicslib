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
			
			if (n1 instanceof MatrixNode && n2 instanceof MatrixNode) {
				// both matrices
				Exception e = new Exception("NOT CODED");
				e.printStackTrace();
			}else if (n1 instanceof MatrixNode) {
				// only n1 matrixNode
				if (! (outputNode instanceof MatrixNode)) outputNode = new MatrixNode();
				
				((MatrixNode) outputNode).setElements(new ValueNode[((MatrixNode) n1).getValues().length]);
				
				for (int i = 0; i < ((MatrixNode) n1).getValues().length; i++ ) {
					((MatrixNode) outputNode).getValues()[i] = new ValueNode(0);
					((MatrixNode) outputNode).getValues()[i] = operation(((MatrixNode) n1).getValues()[i],n2,((MatrixNode) outputNode).getValues()[i]);
				}
			}else if (n2 instanceof MatrixNode) {
				// only n2 matrixNode
				if (! (outputNode instanceof MatrixNode)) outputNode = new MatrixNode();
				
				((MatrixNode) outputNode).setElements(new ValueNode[((MatrixNode) n2).getValues().length]);
				
				
				for (int i = 0; i < ((MatrixNode) n2).getValues().length; i++ ) {
					((MatrixNode) outputNode).getValues()[i] = new ValueNode(0);
					((MatrixNode) outputNode).getValues()[i] = operation(n1,((MatrixNode) n2).getValues()[i],((MatrixNode) outputNode).getValues()[i]);
				}
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
