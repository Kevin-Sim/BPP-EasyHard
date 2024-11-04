import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bppModel.AWF;
import bppModel.AbstractAlgorithm;
import bppModel.BF;
import bppModel.FF;
import bppModel.FS1;
import bppModel.NF;
import bppModel.Problem;
import bppModel.Solution;
import bppModel.WF;
import bppModel.FSW;
import ea.Individual;
import io.FileGetter;
import io.ProblemReader;
import io.StringIO;

public class TestAllHeuristics {

	public static void main(String[] args) throws IOException {
//		String[] dirNames = FileGetter.getDirNamesRecursive("Evolved2024a");
		String[] dirNames = FileGetter.getDirNamesRecursive("C:\\Users\\Kev\\Dropbox\\Laptop\\BPPWS\\BPP-EasyHard\\zz\\");

		StringBuilder sb;
		ArrayList<AbstractAlgorithm> algorithms;
		//10.39
		for (String dirName : dirNames) {
			System.out.println(dirName);
			algorithms = Individual.getAlgs();
			String parentDirName = dirName.substring(0, dirName.length() - 1);
			parentDirName = parentDirName.substring(parentDirName.lastIndexOf('\\') + 1);
			String[] filenames = FileGetter.getFileNames(dirName, "", ".bpp");
			if (filenames.length == 0) {
				continue;
			}
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
			Solution s;
			for (String filename : filenames) {
//				algorithms = Individual.getAlgs();
				count++;
				System.out.println(count + "  of  " + filenames.length + "\t" + dirName + filename);
				// fixes issue with slow down after 1000 problems no idea why it slows but
				// creating new algs fixes it
				// only prob on office pc?
				if (count % 1 == 0) {
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
					}
					if (s.getBins().size() > loser) {
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
					}
					if (s.getFitness() < loser) {
						loser = s.getFitness();
						losers = alg.getClass().getSimpleName();
					} else if (s.getFitness() == loser) {
						losers += " " + alg.getClass().getSimpleName();
					}
				}
				sb.append(winners + "," + losers);
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + "/" + parentDirName + "IR.csv", false));
			writer.write(sb.toString());
			writer.close();
			sb = new StringBuilder();
			System.out.println("finished " + parentDirName);
			//break;
		}
		// add stats average
		for (String dirName : dirNames) {
			String[] filenames = FileGetter.getFileNames(dirName, "", "IR.csv");
			if (filenames.length == 0) {
				continue;
			}
			String stats = ",";
			for (String filename : filenames) {
				String[] lines = StringIO.readStringsFromFile(dirName + filename);
				int lineNo = 0;
				for (int col = 1; col <= Individual.getAlgs().size() + 1; col++) {
					double sum = 0;
					lineNo = 0;
					for (String line : lines) {
						lineNo++;
						if (lineNo == 1 || line.equals("")) {
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
				for (int col = 4 + Individual.getAlgs().size(); col < 4 + 2 * Individual.getAlgs().size(); col++) {
					double sum = 0;
					lineNo = 0;
					for (String line : lines) {
						lineNo++;
						if (lineNo == 1 || line.equals("")) {
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
//			break;
		}
		// add stats excess
		for (String dirName : dirNames) {
			String[] filenames = FileGetter.getFileNames(dirName, "", "IR.csv");
			if (filenames.length == 0) {
				continue;
			}
			String stats = ",";
			for (String filename : filenames) {
				String[] lines = StringIO.readStringsFromFile(dirName + filename);
				String lastLine = lines[lines.length - 1];
				//System.out.println(lastLine);
				double lb = Double.parseDouble(lastLine.split(",")[1]);
				for (int col = 2; col <= Individual.getAlgs().size() + 1; col++) {
					double avg = 0;
					try {
						avg = Double.parseDouble(lastLine.split(",")[col]);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stats += String.format("%.2f", 100 * (avg - lb) / lb) + ",";
				}
//				stats += ",,";
//				for (int col = 4 + Individual.getAlgs().size(); col < 4 + 2 * Individual.getAlgs().size(); col++) {
//					double sum = 0;
//					lineNo = 0;
//					for (String line : lines) {
//						lineNo++;
//						if (lineNo == 1 || line.equals("")) {
//							continue;
//						}
//						try {
////								System.out.println(line);
//							sum += Double.parseDouble(line.split(",")[col]);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					stats += sum / (lines.length - 1) + ",";
//				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + filename, true));
				writer.write("Excess," + stats + "\r\n");
				writer.close();
			}
//			break;
		}
	}
}
