package calculator_parser_solver;

public class MatrixNode extends AdvancedValueNode {
	
	public ValueNode[][] values;
	
	public MatrixNode() {
		super('k');
		values = new ValueNode[0][0];
		
	}
	
	public MatrixNode(int rows, int columns) {
		super('k');
		values = new ValueNode[rows][columns];
	}
	
	public MatrixNode(ValueNode[][] values) {
		super('k');
		this.values = values;
	}
	
	public String toString() {
		return getValueDataStr();
	}
	
	public ValueNode[][] getElements() {
		return values;
	}
	public ValueNode[][] getValues() {
		return values;
	}
	
	public String getValueDataStr() {
		String out = "";
		
		//add open "bracket" character
		out += "[";
		
		
		// add values
		if (values != null && values.length != 0) {
			for (int i = 1; i < values[0].length; i++) {
				out += values[0][i]; // add the value 
				out += i < values[0].length-1 ? "," : ""; // add a comma if this isn't the last value
						
			}
		}else { // if there aren't any values just add null
			out += "null";
		}
		
		
		//add close "bracket" character
		out += "]";
		
		return out;
	}

	public ValueNode getSubList(int col_indx) {
		if (values.length > 1) {
			return new Bra_ket(values[col_indx],true);
		}else {
			return values[0][col_indx];
		}
	
	}
	
	

}
