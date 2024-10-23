package bppModel;


public class Item extends Object implements Comparable<Item>{

	private int length;
	
	public Item() {
		
	}
	public Item(int len) {
		this.length = len;
	}

	@Override
	/**
	 * @return A copy (different object) of the item
	 */
	protected Object clone(){ 
		Item item = new Item();
		item.length = this.length;
		return item;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public String toString(){
		String result = "" + length;
		return result;
	}
	
	/**
	 * Returns -1 if this item is smaller, 0 if the same and 1 if THIS item is bigger
	 */
	public int compareTo(Item item) {
		if(item.length > this.length){
			return -1;
		}else{
			if(item.length < this.length){
				return 1;
			}
		}
		return 0;		
	}	
	
	
}
