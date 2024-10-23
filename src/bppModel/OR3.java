package bppModel;

import java.util.ArrayList;

/**
 * Unlike other heuristics this always has an empty bin that could have 
 * a higher priority than a partially filled one 
 */
public class OR3 extends AbstractAlgorithm {

	@Override
	public void packNextItem(Solution solution) {
		Bin highestPriorityBin = null;
		double highestPriorityValue = -1;
		Item item = solution.getRemainingItems().remove(0);
		for(Bin bin : solution.getBins()) {
			//only bins that fit item
			if(bin.getCapacity() - bin.getBinLength() < item.getLength()) {
				continue;
			}
			int remainingSpace = bin.getCapacity() - bin.getBinLength() - item.getLength();
			double priority = -1;
			if (remainingSpace <= 2){
				priority = 4;
			}else if(remainingSpace <= 3) {
				priority = 3;
			}else if(remainingSpace <= 5) {
				priority = 2;
			}else if(remainingSpace <= 7) {
				priority = 1;
			}else if(remainingSpace <= 9) {
				priority = 0.9;
			}else if(remainingSpace <= 12) {
				priority = 0.95;
			}else if(remainingSpace <= 15) {
				priority = 0.97;
			}else if(remainingSpace <= 18) {
				priority = 0.98;
			}else if(remainingSpace <= 20) {
				priority = 0.98;
			}else if(remainingSpace <= 21) {
				priority = 0.98;
			}else {
				priority = 0.99;
			}
			
			if(priority >= highestPriorityValue) {
				highestPriorityValue = priority;
				highestPriorityBin = bin;				
			}
		}
		
		highestPriorityBin.getItems().add(item);

	}

	
	/**
	 * Unlike other heuristics this always has an empty bin that could have 
	 * a higher priority than a partially filled one 
	 */
	@Override
	public Solution packRemainingItems(Solution solution) {
		for(int i = 0; i < solution.getProblem().getItems().size(); i++) {
			addBin(solution);
		}
		super.packRemainingItems(solution);
		ArrayList<Bin> emptyBins = new ArrayList<Bin>();
		for(Bin bin : solution.getBins()) {
			if(bin.getBinLength() == 0) {
				emptyBins.add(bin);
			}
		}
		solution.getBins().removeAll(emptyBins);
		return solution;
	}
}
