package gp;

import bppModel.BF;
import bppModel.Solution;
import bppModel.FSW;

public class WeibulTerminalNode extends TerminalNode {

	public WeibulTerminalNode(Node parentNode) {
		super(parentNode);
	}

	@Override
	public Boolean eval() {
		FSW weib = new FSW();
		if(Wrapper.solution.getBins().size() == 0) {
			for(int i = 0; i < Wrapper.solution.getProblem().getItems().size(); i++) {
				weib.addBin(Wrapper.solution);
			}
		}
		weib.packNextItem(Wrapper.solution);
		if(Wrapper.solution.hasRemainingItems()) {
			return true;
		}
		return false;
	}

}
