package calculator_parser_solver;

public class Bra extends Bra_ket {

	public Bra() {
		super(true);
	}
	
	public Bra(int numVals) {
		super(true,numVals);
	}
	
	public Bra(ValueNode[] values) {
		super(values, true);
	}


	


}
