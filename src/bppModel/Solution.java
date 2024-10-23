package bppModel;

import java.util.ArrayList;

public class Solution implements Comparable<Solution> {

	private ArrayList<Bin> bins;

	private Problem problem;

	private double fitness = -1;

	private ArrayList<Item> remainingItems;	

	private Solution() {

	}

	/**
	 * Specify whether to sort the items in decending order Nothe most are
	 * already sorted in the database
	 * 
	 * @param problem
	 * @param sort
	 */
	public Solution(Problem problem, boolean sort) {
		this();
		this.problem = problem;
		if (sort) {
			problem.sortItemsDescending();
		}
		this.remainingItems = problem.getCopyOfItems();
		this.bins = getBins();
	}

	public Solution(Problem problem) {
		this(problem, false);		
	}

	/**
	 * The list of bins in the solution. Bins should be added via the public Bin
	 * packNextBin(Solution solution) method inherited by all heuristics from
	 * the bstractAlgorithm class
	 * 
	 * @return The list of bins. Note if null then Arraylist<Bin> is created
	 * 
	 */
	public ArrayList<Bin> getBins() {
		if (bins == null) {
			this.bins = new ArrayList<Bin>();
		}
		return bins;
	}

	public void setBins(ArrayList<Bin> bins) {
		this.bins = bins;
	}

	public int[] getBinLengths() {
		int[] result = new int[getBins().size()];
		int count = 0;
		for (Bin bin : getBins()) {
			result[count] = bin.getBinLength();
			count++;
		}
		return result;
	}

	public int[] getBinWaste() {
		int[] result = new int[getBins().size()];
		int count = 0;
		for (Bin bin : getBins()) {
			result[count] = bin.getCapacity() - bin.getBinLength();
			count++;
		}
		return result;
	}
	
	public double getAverageBinWaste() {
		int sum = 0;
		for (int i : getBinWaste()){
			sum += i;
		}
		double avg = sum / new Double(getBins().size());
		return avg;		
	}
	
	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	

	public ArrayList<Bin> getCopyOfBins() {
		ArrayList<Bin> result = new ArrayList<Bin>();
		for (Bin bin : getBins()) {
			Bin newBin = new Bin(problem.getPr_capacity());
			for (Item item : bin.getItems()) {
				newBin.getItems().add(item);
			}
			result.add(newBin);
		}
		return result;
	}

	public double getFitness() {
		if(fitness == -1) {
			fitness = 0;
			for (Bin bin : bins) {
				fitness += bin.getFitness(problem.getPr_capacity());
			}
			fitness = fitness / getBins().size();
		}
		return fitness;
	}

	public Solution copy() {
		Solution individual = new Solution(problem);
		individual.setBins(getCopyOfBins());
		individual.remainingItems = getCopyOfRemainingItems();
		return individual;
	}

	private ArrayList<Item> getCopyOfRemainingItems() {
		if (remainingItems == null) {
			return null;
		}

		ArrayList<Item> result = new ArrayList<Item>();
		for (Item item : remainingItems) {
			result.add(item);
		}
		return result;
	}

	public int compareTo(Solution individual) {
		if (individual.getFitness() > this.getFitness()) {
			return 1;
		} else {
			if (individual.getFitness() < this.getFitness()) {
				return -1;
			}
		}
		return 0;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public final ArrayList<Item> getRemainingItems() {
		return remainingItems;
	}

	public boolean hasRemainingItems() {
		if (getRemainingItems().size() > 0) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		String str = "";
		for(Bin b : bins) {
			str += b.toString() + "\r\n";
		}
		return str;
	}
}