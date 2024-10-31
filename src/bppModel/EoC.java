package bppModel;

import java.util.ArrayList;

import io.FileGetter;
import io.ProblemReader;

/**
 * Unlike other heuristics this always has an empty bin that could have a higher
 * priority than a partially filled one
 */
public class EoC extends AbstractAlgorithm {

	@Override
	public void packNextItem(Solution solution) {
		Item item = solution.getRemainingItems().remove(0);		
		ArrayList<Bin> validBins = new ArrayList<Bin>();
		int maxVal = 0;
		for (int i = 0; i < solution.getBins().size(); i++) {
			Bin bin = solution.getBins().get(i);
			// only bins that fit item
			if (bin.getBinWaste() < item.getLength()) {
				continue;
			}
			validBins.add(bin);
			if (bin.getBinWaste() > maxVal) {
				maxVal = bin.getBinWaste();
			}
		}
		double[] scores = new double[validBins.size()];
		for (int i = 0; i < scores.length; i++) {
			Bin bin = validBins.get(i);
			if (bin.getBinWaste() - item.getLength() == 0) {
				scores[i] = Double.MAX_VALUE;
			} else {
				scores[i] = Math.log(item.getLength()) * Math.pow(bin.getBinWaste(), 2)
						/ (item.getLength() * Math.sqrt(bin.getBinWaste() - item.getLength()));
				double temp = Math.pow(bin.getBinWaste() / new Double(item.getLength()), 3);
				scores[i] += temp;
				if (bin.getBinWaste() == maxVal) {
					scores[i] = -Double.MAX_VALUE;
				}
			}
		}

		Bin highestPriorityBin = null;
		double largestPriority = -Double.MAX_VALUE;
		for (int i = 0; i < scores.length; i++) {
			if (highestPriorityBin == null || scores[i] > largestPriority) {
				largestPriority = scores[i];
				highestPriorityBin = validBins.get(i);
			}
		}
//		System.out.println(highestPriorityBin);
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

	public static void main(String[] args) {

		String[] filenames = FileGetter.getFileNames("Evolved2024/googleWeibull", "", ".bpp");
		double sum = 0;
		for (String filename : filenames) {
			System.out.print(filename + " ");
			Problem p = ProblemReader.getProblem("./Evolved2024/googleWeibull/", filename);
			Solution s = new Solution(p);
			EoC eoc = new EoC();
			eoc.packRemainingItems(s);
			System.out.println(s.getBins().size() + " bins");
			sum += s.getBins().size();			
		}
		System.out.println(sum / filenames.length + " Avg bins");
	}
}
