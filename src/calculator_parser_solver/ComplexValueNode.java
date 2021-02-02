package calculator_parser_solver;

public class ComplexValueNode extends AdvancedValueNode {
	private double imaginaryComponent;
	
	public ComplexValueNode() {
		super('k');
		notCalculated();
	}
	
	public ComplexValueNode(double real, double imaginary) { 
		super(real,'k');
		imaginaryComponent = imaginary;
	}
	
	public void congjugate() {
		imaginaryComponent = -imaginaryComponent;
	}
	
	public ComplexValueNode getConjugate() {
		return new ComplexValueNode(value,-imaginaryComponent);
	}
	
	public double getImaginaryComponent() {
		return imaginaryComponent;
	}
	
	/*
	@Override
	public ComplexValueNode copy() {
		ComplexValueNode cV = new ComplexValueNode(value,imaginaryComponent);
		cV.setUnsetVal(unsetVal(),'k');
		cV.setName(getName());
		System.out.println(cV.unsetVal());
		return cV;
	}*/
	
	
	public AdvancedValueNode calculateOperation(EquationNode operation) {
		return null; // we do not have a defined implementation for this operation
	}
	
	/**
	 * 
	 * @param operation
	 * @param nodeB
	 * @param reverseParamOrder if the equation was originally a [operation] b and we called b.calculateOperation(a). This would be because a is not an AdvancedValueNode
	 * @return
	 */
	@Override
	public AdvancedValueNode calculateOperation(Two_subNode_node operation, EquationNode nodeB, boolean reverseParamOrder) {
		
		if (nodeB.getValueData() != null && ComplexValueNode.class.isAssignableFrom(nodeB.getValueData().getClass())) {
			ComplexValueNode nodeBComplx = (ComplexValueNode) nodeB;
			double realComponent = operation.operation(getValue(),nodeBComplx.getValueData().getValue());
			double imaginaryComponent = operation.operation(getImaginaryComponent(),nodeBComplx.getImaginaryComponent());
			
			return new ComplexValueNode(realComponent, imaginaryComponent);
		}else{
			double realComponent = operation.operation(value,nodeB.getValue());
			
			return new ComplexValueNode(realComponent, getImaginaryComponent()); // we do not have a defined implementation for this operation
		}
	}
	
	
	
	
	@Override
	public String toString() {
		return "" + getValue() + " + " + imaginaryComponent + "i ";
	}
	

}
