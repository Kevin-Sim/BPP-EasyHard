package gp;

public class AbsFunctionNode extends FunctionNode {

	public AbsFunctionNode(Node parentNode) {
		super(parentNode);
		setNumberOfChildren(1);		
		setGraphSymbol("ABS");
	}

	@Override
	public
	Double eval() {
		Double result = null;
		if(getChildNodes().size() != getNumberOfChildren()){
			return null;
		}
		result = Math.abs(getChildNodes().get(0).eval());
		return result;
	}
	
}
