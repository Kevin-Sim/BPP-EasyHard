package gp;

import bppModel.AbstractAlgorithm;
import bppModel.Solution;

public class GpAlg extends AbstractAlgorithm {
	public Node node = null;

	
	public GpAlg(Node node) {
		super();
		this.node = node;
	}


	@Override
	public void packNextItem(Solution solution) {
		while(solution.hasRemainingItems()) {
			node.eval();
		}
	}

}
