package bppModel;

import java.util.ArrayList;

/**
 * A Bin
 * @author cs378
 *
 */
public class Bin implements Comparable<Bin>{

	private ArrayList<Item> items;	
	private int capacity;
	public int id;
	public Bin(int capacity){
		this.capacity = capacity;
		items = new ArrayList<Item>();		
	}	

	/**
	 * The total lenghth of the bins items
	 * @return the fill of the bin
	 */
	public int getBinLength(){
		int result = 0;
		try {
			for(Item item : items){
				result += item.getLength();
			}
		} catch (RuntimeException e) {
			
		}
		return result;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * From Falkenauer, E., Delchambre, A.: A genetic algorithm for bin packing and line
	 * balancing. In: Robotics and Automation, 1992. Proceedings., 1992 IEEE
	 * International Conference on. pp. 1186 --1192 vol.2 (1992)
	 * 
	 * @param maxBinSize The Bin Capacity
	 * @return The fitness of a bin. Note that the solution quality is derived by 
	 * summing (the bin fitness divided by the number of bins used) for all bins.
	 * 
	 */
	public double getFitness(int maxBinSize){		
		int k = 2;
		double binFitness  = ((double)getBinLength()) / ((double)maxBinSize);
		binFitness = Math.pow(binFitness, k);		
		return binFitness;
	}
	
	

		
	/**
	 * Determines if this bin is filled less than the supplied bin (-1)
	 * more than (1) or is of equal length (0)
	 * 
	 * Can override ie. GP sort
	 * 
	 * @param bin A bin to compare to this bin
	 * @return -1 if this bin is filled less, 0 if filled the same and 1 if filled more
	 */
	public int compareTo(Bin bin) {
		if(bin.getBinLength() > this.getBinLength()){
			return -1;
		}else if(bin.getBinLength() < this.getBinLength()){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Gets a copy of this bin
	 * @return a COPY of this bin containing the SAME Item objects.
	 */
	public Bin copy(){
		Bin bin = new Bin(capacity);
		for(Item item : items){
			bin.items.add(item);
		}
		return bin;
	}

	/**
	 * 
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	public int getBinWaste(){
		return capacity - getBinLength();
	}
	
	@Override
	public String toString() {
		String str = id + "\t" +  getBinLength() + ":" + "\t";
		for(Item item : items) {
			str += item.toString() + "\t";
		}
		return str;
	}
}
