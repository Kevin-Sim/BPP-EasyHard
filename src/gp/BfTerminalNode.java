package gp;

import bppModel.BF;

public class BfTerminalNode extends TerminalNode {

	public BfTerminalNode(Node parentNode) {
		super(parentNode);
	}

	@Override
	public Boolean eval() {
		BF bf = new BF();
		bf.packNextItem(Wrapper.solution);
		if(Wrapper.solution.hasRemainingItems()) {
			return true;
		}
		return false;
	}

}
