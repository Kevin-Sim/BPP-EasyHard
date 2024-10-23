package gp;

public class DoubleTerminalNode extends TerminalNode{	

	double val = GpParameters.random.nextGaussian();
	
	public DoubleTerminalNode(Node parentNode) {
		super(parentNode);	
		setGraphSymbol("" + val);
	}

	@Override
	public Double eval() {
		return new Double(val);
	}

}
