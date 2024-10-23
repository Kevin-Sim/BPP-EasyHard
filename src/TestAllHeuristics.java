import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bppModel.AWF;
import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.FF;
import bppModel.OR3;
import bppModel.NF;
import bppModel.Problem;
import bppModel.Solution;
import bppModel.WF;
import bppModel.Weib;
import ea.Individual;
import io.FileGetter;
import io.ProblemReader;

public class TestAllHeuristics {

	public static void main(String[] args) throws IOException {
		String[] dirNames = FileGetter.getDirNamesRecursive("Evolved2024");
		
		StringBuilder sb;
		ArrayList<AbstractAlgorithm> algorithms;
		for (String dirName : dirNames) {
			System.out.println(dirName);
			algorithms = Individual.getAlgs();
			String parentDirName = dirName.substring(0, dirName.length() - 1);
			parentDirName = parentDirName.substring(parentDirName.lastIndexOf('\\') + 1);
			String[] filenames = FileGetter.getFileNames(dirName, "", ".bpp");
			System.out.println(parentDirName + "\t" + filenames.length + " files");
			sb = new StringBuilder();
			sb.append("File,Opt L1,");
			for (AbstractAlgorithm alg : algorithms) {
				sb.append(alg.getClass().getSimpleName() + ",");
			}
			sb.append("winners,losers");
			int count = 0;		
			Problem p;
			Solution s ;
			for (String filename : filenames) {
				count++;
				//fixes issue with slow down after 1000 problems no idea why it slows but creating new algs fixes it
				//only prob on office pc?
				if(count % 100 == 0) {
//					System.out.println(parentDirName + "\t" + count );
//					System.gc();
//					algorithms = Individual.getAlgs();
				}
				p = ProblemReader.getProblem(dirName, filename);
				sb.append("\r\n" + filename.substring(filename.lastIndexOf('\\') + 1) + "," + p.getPr_opt() + ",");
				int winner = Integer.MAX_VALUE;
				String winners = "";	
				int loser = Integer.MIN_VALUE;
				String losers = "";
				for (AbstractAlgorithm alg : algorithms) {
					s = new Solution(p);
					alg.packRemainingItems(s);
					sb.append(s.getBins().size() + ",");
					if (s.getBins().size() < winner) {
						winner = s.getBins().size();
						winners = alg.getClass().getSimpleName();
					} else if (s.getBins().size() == winner) {
						winners += " " + alg.getClass().getSimpleName();
					}if (s.getBins().size() > loser) {
						loser = s.getBins().size();
						losers = alg.getClass().getSimpleName();
					} else if (s.getBins().size() == winner) {
						losers += " " + alg.getClass().getSimpleName();
					}
				}
				sb.append(winners + "," + losers);
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(parentDirName + ".csv", false));
			writer.write(sb.toString());
			writer.close();
			sb = new StringBuilder();
			System.out.println("finished " + parentDirName);
		}
	}
}
