package bppModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.ProblemReader;

/**
 * Unlike other heuristics this always has an empty bin that could have a higher
 * priority than a partially filled one
 */
public class Weib extends AbstractAlgorithm {

	
	

	@Override
	public void packNextItem(Solution solution) {
		int max_bin_cap = solution.getProblem().getPr_capacity();
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
		for (int i = 0; i < scores.length; i++) {
			Bin bin = validBins.get(i);
			scores[i] = Math.pow(bin.getBinWaste() - max_bin_cap, 2) / item.getLength();
			scores[i] += Math.pow(bin.getBinWaste(), 2) / (item.getLength() * item.getLength());
			scores[i] += Math.pow(bin.getBinWaste(), 2) / Math.pow(item.getLength(), 3);
			if(bin.getBinWaste() > item.getLength()) {
				scores[i] = -scores[i];
			}

		}

		Bin highestPriorityBin = null;
		double largestPriority = Integer.MIN_VALUE;
		for (int i = scores.length - 1; i > 0; i--) {
			scores[i] = scores[i] - scores[i - 1];
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
		// 2019 for first i get 2335
		Problem p = ProblemReader.getProblem("", "weib00.bpp");
		Solution s = new Solution(p);
		Weib weib = new Weib();
		weib.packRemainingItems(s);
		System.out.println(s.getBins().size() + " bins");

	}
}
