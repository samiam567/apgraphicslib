package calculator_parser_solver;

public class MatrixCreate extends Sandwich_operatorNode {
	Equation equation;
	
	public MatrixCreate(Equation equation, String data, int parenthesisLevel, MatrixNode outputMatrix) {
		super(equation, data, parenthesisLevel);
		this.equation = equation;
		super.setValueData(outputMatrix);
		notCalculated();
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
	
	public ValueNode[] getValues() {
		return ((MatrixNode) getValueData()).getValues();
	}
	
	public ValueNode[][] getMatrix() {
		return ((MatrixNode) getValueData()).getMatrix();
	}
	
	private void getMatrixNode() {
		ValueNode[] elements = new ValueNode[getSubNodes().length];
		((MatrixNode) valueData).setElements(elements);
		
		// set the elements to the valueData of our subNodes
		for (int i = 0; i < elements.length; i++) {
			elements[i] = getSubNodes()[i].getValueData();
		}
		
		valueData.notCalculated();
	}
	
	//@Override
	public static ValueNode[][] combineIntoMatrix(ValueNode[] nodes, MatrixNode outputNode) {
		//TODO talk to Dr. Upadhyaya and figure this out
		(new Exception("method not programmed yet")).printStackTrace();
		
		MatrixCombine matCombine = new MatrixCombine();
		
		for (int i = 0; i < nodes.length; i++) {
			outputNode = (MatrixNode) matCombine.operation(outputNode,nodes[i],outputNode);
		}
		
		
		return outputNode.getMatrix();
		
	}
	
	
}
