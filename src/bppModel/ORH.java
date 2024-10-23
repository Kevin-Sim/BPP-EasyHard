package bppModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.FileGetter;
import io.ProblemReader;

/**
 * Unlike other heuristics this always has an empty bin that could have a higher
 * priority than a partially filled one
 */
public class ORH extends AbstractAlgorithm {

	
	@Override
	public void packNextItem(Solution solution) {
		
		Item item = solution.getRemainingItems().remove(0);
		ArrayList<Bin> validBins = new ArrayList<Bin>();
		for (int i = 0; i < solution.getBins().size(); i++) {
			Bin bin = solution.getBins().get(i);
			// only bins that fit item
			if (bin.getBinWaste() < item.getLength()) {
				continue;
			}
			validBins.add(bin);
		}
		double[] scores = new double[validBins.size()];
		Bin bestBin = null;
		for (int i = 0; i < scores.length; i++) {
			Bin bin = validBins.get(i);
			scores[i] = 1000;
		    //Penalize bins with large capacities.
			scores[i] -= bin.getBinWaste() * (bin.getBinWaste() - item.getLength());
			//Extract index of bin with best fit.
			if(bestBin == null || bin.getBinWaste() < bestBin.getBinWaste()) {
				bestBin = bin;
			}
		}
		// Scale score of best fit bin by item size.
		scores[validBins.indexOf(bestBin)] *= item.getLength();
		//Penalize best fit bin if fit is not tight.
		scores[validBins.indexOf(bestBin)] -= Math.pow(bestBin.getBinWaste() - item.getLength(), 4);
				    
		Bin highestPriorityBin = null;
		double largestPriority = Integer.MIN_VALUE;
		for (int i = scores.length - 1; i > 0; i--) {
			if (scores[i] >= largestPriority) {
				largestPriority = scores[i];
				highestPriorityBin = validBins.get(i);
			}
		}
		
		highestPriorityBin.getItems().add(item);
		
	}

	/**
	 * Unlike other heuristics this always has an empty bin that could have a higher
	 * priority than a partially filled one
	 */
	@Override
	public Solution packRemainingItems(Solution solution) {
		for (int i = 0; i < solution.getProblem().getItems().size(); i++) {
			addBin(solution);
			solution.getBins().get(i).id = i;
		}
		super.packRemainingItems(solution);
		ArrayList<Bin> emptyBins = new ArrayList<Bin>();
		for (Bin bin : solution.getBins()) {
			if (bin.getBinLength() == 0) {
				emptyBins.add(bin);
			}
		}
		solution.getBins().removeAll(emptyBins);
		return solution;
	}

	public static void main(String[] args) throws IOException {
		// 209.7 avg
		String[] filenames = FileGetter.getFileNames(".\\Instances2024\\googleOR3", "", ".bpp");
		double sum = 0;
		for(String filename : filenames) {
			Problem p = ProblemReader.getProblem(".\\Instances2024\\googleOR3\\", filename);
			Solution s = new Solution(p);
			ORH orh = new ORH();
			orh.packRemainingItems(s);
			System.out.println(s.getBins().size() + " bins");
			sum += s.getBins().size();
		}
		System.out.println("Avg = " + sum / filenames.length);

	}
}
