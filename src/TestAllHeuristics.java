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
import io.StringIO;

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
			sb.append("winners,losers,");
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
				double winner = Integer.MAX_VALUE;
				String winners = "";	
				double loser = Integer.MIN_VALUE;
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
					} else if (s.getBins().size() == loser) {
						losers += " " + alg.getClass().getSimpleName();
					}
				}
				sb.append(winners + "," + losers + ",");
				winner = Integer.MIN_VALUE;
				winners = "";	
				loser = Integer.MAX_VALUE;
				losers = "";
				for (AbstractAlgorithm alg : algorithms) {
					s = new Solution(p);
					alg.packRemainingItems(s);
					sb.append(s.getFitness() + ",");
					if (s.getFitness() > winner) {
						winner = s.getFitness();
						winners = alg.getClass().getSimpleName();
					} else if (s.getFitness() == winner) {
						winners += " " + alg.getClass().getSimpleName();
					}if (s.getFitness() < loser) {
						loser = s.getFitness();
						losers = alg.getClass().getSimpleName();
					} else if (s.getFitness() == loser) {
						losers += " " + alg.getClass().getSimpleName();
					}
				}
				sb.append(winners + "," + losers);
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + "/" + parentDirName + ".csv", false));
			writer.write(sb.toString());
			writer.close();
			sb = new StringBuilder();
			System.out.println("finished " + parentDirName);
//			break;
		}
		//add stats
		for(String dirName : dirNames) {
			String[] filenames = FileGetter.getFileNames(dirName, "", ".csv");
			String stats = ",";
			for(String filename : filenames) {
				String[] lines = StringIO.readStringsFromFile(dirName + filename);
				int lineNo = 0;
				for(int col = 1; col <= Individual.getAlgs().size() + 1; col++) {
					double sum = 0;
					lineNo = 0;
					for(String line : lines) {
						lineNo++;
						if(lineNo == 1 || line.equals("")) {
							continue;
						}
						try {
							sum += Double.parseDouble(line.split(",")[col]);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					stats += sum / (lines.length - 1) + ",";
				}
				stats += ",,";
				for(int col = 4 + Individual.getAlgs().size(); col < 4 + 2 * Individual.getAlgs().size(); col++ ) {
					double sum = 0;
					lineNo = 0;
					for(String line : lines) {
						lineNo++;
						if(lineNo == 1 || line.equals("")) {
							continue;
						}
						try {
//							System.out.println(line);
							sum += Double.parseDouble(line.split(",")[col]);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					stats += sum / (lines.length - 1) + ",";
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + filename, true));
				writer.write("\r\nAverage" + stats + "\r\n");
				writer.close();
			}
			
		}
	}
}
