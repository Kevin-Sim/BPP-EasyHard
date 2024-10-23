import bppModel.BF;
import bppModel.OR3;
import bppModel.Problem;
import bppModel.Solution;
import io.FileGetter;
import io.ProblemReader;

/**
 * sanity check that my code is OK
 * 
 * should be
 * 
 * 212.0
 * 207.45
 */
public class GoogleTest {

	public static void main(String[] args) {
		String[] filenames = FileGetter.getFileNames("googleWeib", "", ".bpp");
		int total = 0;
		for(String filename : filenames) {
			Problem p = ProblemReader.getProblem("./googleWeib/", filename);
			Solution s = new Solution(p);
			BF alg = new BF();
			alg.packRemainingItems(s);
			total += s.getBins().size();
//			System.out.println(filename + "\t" + s.getBins().size());
		}

		System.out.println(total / 5.0);
		
		total = 0;
		for(String filename : filenames) {
			Problem p = ProblemReader.getProblem("./googleWeib/", filename);
			Solution s = new Solution(p);
			OR3 alg = new OR3();
			alg.packRemainingItems(s);
			total += s.getBins().size();
//			System.out.println(filename + "\t" + s.getBins().size());
		}

		System.out.println(total / 5.0);
	}

}
