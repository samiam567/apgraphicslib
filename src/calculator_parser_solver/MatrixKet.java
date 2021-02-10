package calculator_parser_solver;

public class MatrixKet extends Ket {
	
	public MatrixKet(NodeWrapper[] values) {
		super(values);
	}
	
	public MatrixKet(int rowsNum) {
		values = new NodeWrapper[rowsNum];
		for (int i = 0; i < values.length; i++) {
			values[i] = new NodeWrapper(new ValueNode(0));
		}
	}
	

	@Override
	public void setValues(ValueNode val) {
		if (! val.equals(this)) {
			if (val instanceof Bra_ket) {
				if ( ! (val instanceof Bra) ) (new Exception("WARNING: tried to set values to a ket's values to a bra. ")).printStackTrace();
				
				setValues(((Bra_ket) val).getValues());
			}
		}
	
	}
	
	@Override
	public void setValues(ValueNode[] vals) {
					
			if (vals.length != size()) {
				(new Exception("MatrixKet's length should never change")).printStackTrace();
				
				if ( vals.length > size()) return;
			}
			
			for (int i = 0; i < vals.length; i++) {
				((NodeWrapper) getValues()[i]).setValue(vals[i]);
			}
			
			notCalculated();
			
		
		
	}
	
	public void setValue(int indx, ValueNode value) {
		getWrappers()[indx].setValue(value);
		notCalculated();
	}
	
	@Override
	public ValueNode getValue(int indx) {
		return getWrappers()[indx].getValueNodeChildOfWrapper();
	}
	
	
	public NodeWrapper[] getWrappers() {
		return (NodeWrapper[]) values;
	}
	
	@Override
	public ValueNode[] getValues() {
		ValueNode[] values = new ValueNode[getWrappers().length];
		for (int i = 0; i < values.length; i++) {
			values[i] = getWrappers()[i].getValueNodeChildOfWrapper();
		}
		return values;
	}
}
