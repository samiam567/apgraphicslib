package calculator_parser_solver;

public class Bra_ket extends MatrixNode {
	public boolean bra = false;
	
	public Bra_ket(ValueNode[] values, boolean bra) {
		super(values);
		this.bra = bra;
	}
}
