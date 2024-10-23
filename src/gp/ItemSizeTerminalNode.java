package gp;

public class ItemSizeTerminalNode extends TerminalNode{	

	public ItemSizeTerminalNode(Node parentNode) {
		super(parentNode);
		setGraphSymbol("IS");
	}

	@Override
	public Double eval() {
		return (double) Wrapper.item.getLength() / new Double(Wrapper.bin.getCapacity());
	}

}
