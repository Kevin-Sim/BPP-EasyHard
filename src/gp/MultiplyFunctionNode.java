package gp;

public class MultiplyFunctionNode extends FunctionNode {

	public MultiplyFunctionNode(Node parentNode) {
		super(parentNode);
		setNumberOfChildren(2);		
		setGraphSymbol("X");
	}

	@Override
	public
	Double eval() {
		Double result = null;
		if(getChildNodes().size() != getNumberOfChildren()){
			return null;
		}
		result = getChildNodes().get(0).eval() * getChildNodes().get(1).eval();
		return result;
	}	
}
