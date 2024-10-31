package gp;

public class AndFnNode extends FunctionNode {

	public AndFnNode(Node parentNode) {
		super(parentNode);
		setNumberOfChildren(2);		
		setGraphSymbol("&&");
	}

	@Override
	public Boolean eval() {
		Boolean result = getChildNodes().get(0).eval() && getChildNodes().get(1).eval();
		return result;
	}

}
