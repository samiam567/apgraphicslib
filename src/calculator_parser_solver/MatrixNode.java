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
	
	public MatrixNode(ValueNode[] values) {
		super('k');
		//TODO parse through values and generate matrix
		
		Exception e = new Exception("Method not written");
		e.printStackTrace();
	}
	
	
	

}
