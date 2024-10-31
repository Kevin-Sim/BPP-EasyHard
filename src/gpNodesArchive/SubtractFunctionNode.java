package gpNodesArchive;

import gpNodesArchive.FunctionNode;
import gpNodesArchive.Node;

public class SubtractFunctionNode extends FunctionNode {

	public SubtractFunctionNode(Node parentNode) {
		super(parentNode);
		setNumberOfChildren(2);		
		setGraphSymbol("-");
	}

	@Override
	public
	Double eval() {
		Double result = null;
		if(getChildNodes().size() != getNumberOfChildren()){
			return null;
		}
		result = getChildNodes().get(0).eval() - getChildNodes().get(1).eval();
		return result;
	}	
}
