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
 * 
 * score = 5443 * np.ones(bins.shape)
    score -= bins * (bins-item) # Extract index of bin with best fit.
    index = np.argmin(bins) # Scale score of best fit bin by item size.
    score[index] *= item # Penalize best fit bin if fit is not tight.
    score[index] -= (bins[index] - item)**5
    return score

 */
public class FS2_IR extends AbstractAlgorithm {

	
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
			scores[i] = 5443;
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
		scores[validBins.indexOf(bestBin)] -= Math.pow(bestBin.getBinWaste() - item.getLength(), 5);
				    
		Bin highestPriorityBin = null;
		double largestPriority = -Double.MAX_VALUE;
		for (int i = scores.length - 1; i > 0; i--) {
			if (scores[i] >= largestPriority) {
				largestPriority = scores[i];
				highestPriorityBin = validBins.get(i);
			}
		}
		if(scores.length > 0 && scores[0] > largestPriority) {
			highestPriorityBin = validBins.get(0);
		}
		
		try {
			highestPriorityBin.getItems().add(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
			FS2_IR orh = new FS2_IR();
			orh.packRemainingItems(s);
			System.out.println(s.getBins().size() + " bins");
			sum += s.getBins().size();
		}
		System.out.println("Avg = " + sum / filenames.length);

	}
}
