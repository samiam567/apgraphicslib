package calculator_parser_solver;

public class MatrixNode extends AdvancedValueNode {
	
	protected ValueNode[] values;
	
	private ValueNode[][] matrix;
	protected boolean matrixCalculated = false;
	
	public MatrixNode() {
		super('k');
		values = new ValueNode[0];
		notCalculated();
	}
	
	public MatrixNode(ValueNode[] values) {
		super('k');
		this.values = values;
		notCalculated();
	}
	
	public void setElements(ValueNode[] values) {
		this.values = values;
		notCalculated();
	}
	
	public String toString() {
		return getValueDataStr();
	}
	
	
	public ValueNode[][] getMatrix() {	
		if (! matrixCalculated) {
			matrixCalculated = true;
			matrix = MatrixCreate.combineIntoMatrix(values,this);
			getValuesFromMatrix();
			
		}
		
		return matrix;
	}
	
	private void getValuesFromMatrix() {
		notCalculated();
		
		assert matrix[0].length == matrix[matrix.length-1].length; //approximate check that the matrix is rectangular
		
		if (values.length != matrix.length) values = new ValueNode[matrix.length];
		
		if (matrix[0].length > 1) {
			for (int i = 0; i < matrix.length; i++) {
				values[i] = new Bra_ket(matrix[i],true);
			}
		}else {
			for (int i = 0; i < matrix.length; i++) {
				values[i] = matrix[i][0];
			}
		}
		
		
	}
	
	public void setMatrix(ValueNode[][] mat) {
		matrix = mat;
		matrixCalculated = true;
		getValuesFromMatrix();
	}
	
	public ValueNode[] getValues() {
		System.out.println("WARNING: THIS MAY NOT BE CONSISTANT WITH MATRIX");
		return values;
	}
	
	@Override
	public double getValue() {
		if (! isCalculated()) {
		
			double magnitude = 0;
			ValueNode outputNode = new ValueNode(0);
			Multiplication multi = new Multiplication();
			for (ValueNode v : values) {
				magnitude += ((Two_subNode_node)multi).operation(v,v,outputNode).getValue();
			}
		
			value = Math.sqrt(magnitude);
			
			calculated();
		}
		return value;
	}
	
	public String getValueDataStr() {
		String out = "";
		
		//add open "bracket" character
		out += "[";
		
		
		// add values
		if (values != null && values.length != 0) {
			for (int i = 0; i < values.length; i++) {
				out += values[i].getDataStr(); // add the value 
				out += i < values.length-1 ? "," : ""; // add a comma if this isn't the last value
						
			}
		}else { // if there aren't any values just add null
			out += "null";
		}
		
		
		//add close "bracket" character
		out += "]";
		
		return out;
	}
	

	
	

}
