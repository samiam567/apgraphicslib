package calculator_parser_solver;

public class MatrixCreate extends Sandwich_operatorNode {
	Equation equation;
	
	public MatrixCreate(Equation equation, String data, int parenthesisLevel, AdvancedValueNode outputMatrix) {
		super(equation, data, parenthesisLevel);
		this.equation = equation;
		setValueData(outputMatrix);
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
		}
		return valueData;
	}
	
	@Override
	public void setValueData(ValueNode v) {
		Exception e = new Exception("You should never set the valueData for a MatrixCreate node");
		e.printStackTrace();
	}
	
	public ValueNode[][] getElements() {
		return ((MatrixNode) getValueData()).getElements();
	}
	
	private void getMatrix() {
		//((MatrixNode) getValueData()).setElements();
		
		int braMode = -1; //0 = bra, 1 = ket
		
		// find the size matrix we need
		int row = 0, col = 0;
		ValueNode data;
		for (EquationNode n : subNodes) {
			data = n.getValueData();
			
			if (data instanceof Bra_ket && (! ((Bra_ket) data).bra)) {
				
				if (braMode == 1) {
					Exception e = new Exception("Error while parsing matrix: Cannot combine bras and kets in the same matrix directly. Converting bras to kets");
					Commands.output(e.getStackTrace().toString(),equation);
					
					//convert bras to kets
					for (EquationNode n2 : subNodes) {
						if (n instanceof Bra_ket) ((Bra_ket) n).bra = false;
					}
					
					row = row < ((MatrixNode) data).getElements().length ? ((MatrixNode) data).getElements().length : row;
					continue;
				}
				
				braMode = 0;
				
				col = col < ((MatrixNode) data).getElements().length ? ((MatrixNode) data).getElements().length : col;
			}
			if (data instanceof MatrixNode && ((! (data instanceof Bra_ket) ) || ((Bra_ket) data).bra  )   ) { //we are dealing with a matrix or a ket
				
				if (data instanceof Bra_ket) {

					if (braMode == 0) { // we have been dealing with kets and now we found a bra
						Exception e = new Exception("Error while parsing matrix: Cannot combine bras and kets in the same matrix directly. Converting bras to kets");
						Commands.output(e.getStackTrace().toString(),equation);
						
						// converting kets to bras
						for (EquationNode n2 : subNodes) {
							if (n instanceof Bra_ket) ((Bra_ket) n).bra = true;
						}
						
						col = col < ((MatrixNode) data).getElements().length ? ((MatrixNode) data).getElements().length : col;
						
						continue;
					}
					
					braMode = 1;
			
				}
				row = row < ((MatrixNode) data).getElements().length ? ((MatrixNode) data).getElements().length : row;
				col = col < ((MatrixNode) data).getElements()[0].length ? ((MatrixNode) data).getElements()[0].length : col;
			}
		}
		
		System.out.println("row: " + row + "   col: " + col);
		
		ValueNode[][] elements = new ValueNode[row][col];
		
		
		for (int row_indx = 0; row_indx < row; row_indx++) {
			
			for (int col_indx = 0; col_indx < col; col_indx++) {
				try {
					elements[row_indx][col_indx] = braMode == 0 ? ((MatrixNode) subNodes[row_indx].getValueData()).getSubList(col_indx) : ((MatrixNode) subNodes[col_indx].getValueData()).getSubList(row_indx);
					
				}catch(ArrayIndexOutOfBoundsException a) {
					equation.out.println("WARNING: matrix does not have enough elements to be rectangular. Replacing missing elements with zeroes");
					elements[row_indx][col_indx] = new ValueNode(0);
				}catch(ClassCastException c) {
					if (col_indx != 0) {
						equation.out.println("WARNING: matrix does not have enough elements to be rectangular. Replacing missing elements with zeroes");
						elements[row_indx][col_indx] = new ValueNode(0);
					}else {
						elements[row_indx][col_indx] = subNodes[0].getValueData();
					}
				}
			}
			
		}
		
		
	}
	
	
}
