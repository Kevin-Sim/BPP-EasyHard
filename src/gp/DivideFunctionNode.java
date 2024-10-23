package gp;

public class DivideFunctionNode extends FunctionNode {

	/**
	 * Protected divide if node 2 is 0 then divides by 0.00001
	 * @param parentNode
	 */
	public DivideFunctionNode(Node parentNode) {
		super(parentNode);
		setNumberOfChildren(2);		
		setGraphSymbol("/");
	}

	@Override
	public
	Double eval() {
		Double result = null;
		if(getChildNodes().size() != getNumberOfChildren()){
			return null;
		}
		Double denominator = getChildNodes().get(1).eval();
		if(denominator == 0){
			return 1.0;
		}
		result = getChildNodes().get(0).eval() / denominator;
		return result;
	}
}
