package gpNodesArchive;

import bppModel.BF;
import bppModel.Problem;
import bppModel.Solution;

public class GpTest {

	public static void main(String[] args) {
		for(Problem p : GpParameters.problems) {
			System.out.print("\t" + p.getFilename());
			int lb = getLowerBound(p);
			int bf = getBfBins(p);
			System.out.println("\t" + lb + "\t" + bf);
		}
	}

	private static int getBfBins(Problem p) {
		BF alg = new BF();
		Solution s = new Solution(p);
		alg.packRemainingItems(s);
		return s.getBins().size();
	}

	private static int getLowerBound(Problem p) {
		return (int) Math.ceil(p.getTotalItemLength() / p.getPr_capacity());
	}

}
