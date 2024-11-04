import bppModel.Bin;
import bppModel.FSW;
import bppModel.Problem;
import bppModel.Solution;
import io.ProblemReader;

public class Sanity {

	public static void main(String[] args) {
	
		Problem p = ProblemReader.getProblem("C:\\Users\\Kev\\Dropbox\\Laptop\\BPPWS\\BPP-EasyHard\\Instances2024\\IndependentBenchmarks\\Shuffled\\Irnich_BPP\\", "csAA125_1_Shuf0.bpp");
		Solution s = new Solution(p);
		FSW alg = new FSW();
		alg.packRemainingItems(s);
		
		System.out.println(p.getTotalItemLength());
		long len = 0;
		for(Bin b : s.getBins()) {
			len += b.getBinLength();
			if(b.getBinLength() > p.getPr_capacity()) {
				System.err.println("bin overfull");
			}
		}
		System.out.println(len);
	}

}
