package gpNodesArchive;

import gpNodesArchive.Node;
import gpNodesArchive.TerminalNode;
import gpNodesArchive.Wrapper;

public class BinSizeTerminalNode extends TerminalNode{	

	public BinSizeTerminalNode(Node parentNode) {
		super(parentNode);
		setGraphSymbol("BS");
	}

	@Override
	public Double eval() {
		return null;
//		return (double) Wrapper.bin.getBinLength() / new Double(Wrapper.bin.getCapacity());
	}

}
