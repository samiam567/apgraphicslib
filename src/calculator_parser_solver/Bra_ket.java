package calculator_parser_solver;

public class Bra_ket extends MatrixNode {
	public boolean bra = false;
	
	public Bra_ket(boolean bra) {
		this.bra = bra;
	}
	
	public Bra_ket(ValueNode[] values, boolean bra) {
		super(values);
		this.bra = bra;
	}
	
	
	public String toString() {
		return getValueDataStr();
	}
	
	public String getValueDataStr() {
		String out = "";
		
		//add open "bracket" character
		out += bra ? "<" : "|";
		
		
		// add values
		if (values != null && values.length != 0) {
			for (int i = 0; i < values.length; i++) {
				out += values[i]; // add the value 
				out += i < values.length-1 ? "," : ""; // add a comma if this isn't the last value
						
			}
		}else { // if there aren't any values just add null
			out += "null";
		}
		
		
		//add close "bracket" character
		out += bra ? "|" : ">";
		
		return out;
	}
}
