package bppModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import ea.Individual;

public class Problem {	

	private int pr_capacity = -1;

	private String filename = "";

	private ArrayList<Item> items = null;		

	private int pr_opt = -1;	

	public Problem(int pr_capacity, ArrayList<Item> items) {
		super();
		this.pr_capacity = pr_capacity;		
		this.items = items;
	}
	public Problem(int pr_capacity, String filename, ArrayList<Item> items) {
		super();
		this.pr_capacity = pr_capacity;
		this.filename = filename;
		this.items = items;
	}

	public int getPr_capacity() {
		return pr_capacity;
	}

	public String getFilename() {
		return filename;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public int getPr_opt() {
		if (pr_opt  == -1) {			
			pr_opt = (int) (Math.ceil((double) getTotalItemLength() / pr_capacity));
		}
		return pr_opt;
	}

	public int getTotalItemLength() {
		try {
			int result = 0;
			for (Item item : getItems()) {
				result += item.getLength();
			}
			return result;
		} catch (RuntimeException e) {
			return -1;
		}
	}

	public void sortItemsDescending() {
		if(items == null) {
			return;
		}
		Collections.sort(items);
	}

	public void shuffle(Random rnd) {
		Collections.shuffle(items, rnd);		
	}

	/**
	 * Gets a copy of the items array containing the same item objects
	 * 
	 * @return
	 */
	public ArrayList<Item> getCopyOfItems() {
		ArrayList<Item> theItems = getItems();
		ArrayList<Item> copyOfItems = new ArrayList<Item>();
		for(Item item : theItems){
			copyOfItems.add(item);
		}		
		return copyOfItems ;
	}
	
	@Override
	public String toString() {
		String str = "";
		str += items.size() + "\r\n";
		str += pr_capacity + "\r\n";
		for(Item item : items) {
			str += item + "\r\n";
		}		
		return str;
	}
	
	public String getInstanceData(ArrayList<AbstractAlgorithm> algs) {
		String str = "";		
		for(Item item : items) {
			str += item + ",";
		}	
		AbstractAlgorithm best = null;
		double bestFitness = -1;
		for(AbstractAlgorithm alg : algs) {
			Solution s = new Solution(this);
			alg.packRemainingItems(s);
			if(s.getFitness() > bestFitness) {
				bestFitness = s.getFitness();
				best = alg;
			}
		}
		str += best.getClass().getSimpleName();
		return str;
	}

	public Problem copy() {
		Problem p = new Problem(pr_capacity, new ArrayList<>());
		for(Item item : items) {
			Item item2 = new Item(item.getLength());
			p.getItems().add(item2);
		}
		return p;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if(other.pr_capacity != this.pr_capacity) {
			return false;
		}
		if(other.pr_opt != this.pr_opt) {
			return false;
		}
		if(other.items.size() != this.items.size()) {
			return false;
		}
		for(int i = 0; i < items.size(); i++) {
			if(this.items.get(i).getLength() != other.items.get(i).getLength()) {
				return false;
			}
		}
		return true;
	}
	
	public double[] getDiversity(Problem p, ArrayList<Problem> otherProblems){
		double[] diversity = new double[otherProblems.size()];
		for(Problem aProblem : otherProblems) {
			for(int i = 0; i < p.items.size(); i++) {
				if(p.items.get(i).getLength() == aProblem.getItems().get(i).getLength()) {
					diversity[otherProblems.indexOf(aProblem)] += 1;
				}
			}
		}
		for(int i = 0; i < diversity.length; i++) {
			diversity[i] /= items.size();
		}
		return diversity;
		
	}
	
	public double getAverageDiversity(ArrayList<Problem> problems){
		double sum = 0;
		Problem p = problems.remove(0);
		if(problems.size() > 0) {
			double[] divs = getDiversity(p, problems);
			for(double d : divs) {
				sum += d;
			}
			return sum / problems.size();
		}
		return 1;
	}
	
}
