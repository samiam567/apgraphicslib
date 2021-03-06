package calculator_parser_solver;

public interface Matrixable {
	public void setValues(ValueNode[] vals);

	public boolean valuesSet();
	
	public ValueNode[] getValues();
}
