package gpNodesArchive;

import gpNodesArchive.Node;
import gpNodesArchive.TerminalNode;
import gpNodesArchive.Wrapper;

public class ItemSizeTerminalNode extends TerminalNode{	

	public ItemSizeTerminalNode(Node parentNode) {
		super(parentNode);
		setGraphSymbol("IS");
	}

	@Override
	public Double eval() {
		return null;
//		return (double) Wrapper.item.getLength() / new Double(Wrapper.bin.getCapacity());
	}

}
