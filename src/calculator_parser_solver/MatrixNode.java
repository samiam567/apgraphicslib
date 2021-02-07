package calculator_parser_solver;

public class MatrixNode extends AdvancedValueNode {
	
	public ValueNode[] values;
	
	public MatrixNode() {
		super('k');
		values = new ValueNode[0];
		
	}
	
	public MatrixNode(ValueNode[] values) {
		super('k');
		this.values = values;
	}
	
	public void setElements(ValueNode[] values) {
		this.values = values;
	}
	
	public String toString() {
		return getValueDataStr();
	}
	
	public ValueNode[] getElements() {
		return values;
	}
	public ValueNode[] getValues() {
		return values;
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

	public ValueNode getElement(int col_indx) {
		return getElements()[col_indx];
	}
	
	

}
