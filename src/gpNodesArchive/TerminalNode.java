package gpNodesArchive;

public abstract class TerminalNode extends Node {

	public TerminalNode(Node parentNode) {
		super(parentNode);			
		setNumberOfChildren(0);
	}	
	
	@Override
	public String toString() {
		return super.toString();// + "\t value = " + getTerminalValue();
	}
}
