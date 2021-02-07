package calculator_parser_solver;

public class MatrixCreate extends Sandwich_operatorNode {
	Equation equation;
	
	public MatrixCreate(Equation equation, String data, int parenthesisLevel, MatrixNode outputMatrix) {
		super(equation, data, parenthesisLevel);
		this.equation = equation;
		super.setValueData(outputMatrix);
		notCalculated();
	}
	
	
	@Override
	public double getValue() {
		return getValueData().getValue();
	}

	@Override
	public ValueNode getValueData() {
		if (! isCalculated()) {
			getMatrix();
			calculated();
		}
		return valueData;
	}
	
	@Override
	public void setValueData(ValueNode v) {
		Exception e = new Exception("You should never set the valueData for a MatrixCreate node");
		e.printStackTrace();
	}
	
	public ValueNode[] getElements() {
		return ((MatrixNode) getValueData()).getElements();
	}
	
	private void getMatrix() {
		ValueNode[] elements = new ValueNode[getSubNodes().length];
		((MatrixNode) valueData).setElements(elements);
		
		// set the elements to the valueData of our subNodes
		for (int i = 0; i < elements.length; i++) {
			elements[i] = getSubNodes()[i].getValueData();
		}
		
		
	}
	
	
}
