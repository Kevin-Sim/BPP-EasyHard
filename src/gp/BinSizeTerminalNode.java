package gp;

public class BinSizeTerminalNode extends TerminalNode{	

	public BinSizeTerminalNode(Node parentNode) {
		super(parentNode);
		setGraphSymbol("BS");
	}

	@Override
	public Double eval() {
		return (double) Wrapper.bin.getBinLength() / new Double(Wrapper.bin.getCapacity());
	}

}
