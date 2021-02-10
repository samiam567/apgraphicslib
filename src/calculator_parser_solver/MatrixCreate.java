package calculator_parser_solver;

public class MatrixCreate extends Sandwich_operatorNode {
	Equation equation;
	
	public MatrixCreate(Equation equation, String data, int parenthesisLevel, Matrixable outputMatrix) {
		super(equation, data, parenthesisLevel);
		this.equation = equation;
		notCalculated();
		super.setValueData((ValueNode) outputMatrix);
	
		setParenthesisLevel(parenthesisLevel);
		orderOfOpsLevel = Equation.operations.length+1;
	}
	
	
	@Override
	public double getValue() {
		return getValueData().getValue();
	}

	@Override
	public ValueNode getValueData() {
		if (! isCalculated()) {
			getMatrixNode();
			calculated();
		}
		return valueData;
	}
	
	@Override
	public void setValueData(ValueNode v) {
		Exception e = new Exception("You should never set the valueData for a MatrixCreate node");
		e.printStackTrace();
	}
		
	public MatrixNode getMatrix() {
		return (MatrixNode) getValueData();
	}
	
	private void getMatrixNode() {
		if (! ((Matrixable) valueData).valuesSet()) {
		
			ValueNode[] elements = new ValueNode[getSubNodes().length];
			
			
			// set the elements to the valueData of our subNodes
			for (int i = 0; i < elements.length; i++) {
				elements[i] = getSubNodes()[i].getValueData();
			}
			
			((Matrixable) valueData).setValues(elements);
			
			valueData.notCalculated();
			
			calculated();
			
		}
	}
	
	//@Override
	public static MatrixNode combineIntoMatrix(ValueNode[] nodes, MatrixNode outputNode) {
		
		MatrixCombine matCombine = new MatrixCombine();
		
		for (int i = 0; i < nodes.length; i++) {
			outputNode = (MatrixNode) matCombine.operation(outputNode,nodes[i],outputNode);
		}	
		
		return outputNode;
		
	}
	
	
}
