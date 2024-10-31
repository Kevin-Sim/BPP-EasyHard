import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import bppModel.AbstractAlgorithm;
import bppModel.Problem;
import bppModel.Solution;
import ea.Individual;
import io.FileGetter;
import io.ProblemReader;

public class GenShuffled {

	public static void main(String[] args) throws IOException {
		String[] dirNames = FileGetter.getDirNamesRecursive("C:\\Users\\Kev\\Desktop\\IndependentBenchmarks\\Shuffled");
		StringBuilder sb;
		ArrayList<AbstractAlgorithm> algorithms;
		for (String dirName : dirNames) {
			String parentDirName = dirName.substring(0, dirName.length() - 1);
			parentDirName = parentDirName.substring(parentDirName.lastIndexOf('\\') + 1);
			String[] filenames = FileGetter.getFileNames(dirName, "", ".bpp");
			if(filenames.length == 0) {
				continue;
			}
			System.out.println(parentDirName + "\t" + filenames.length + " files");	
			Problem p;
			for (String filename : filenames) {			
				p = ProblemReader.getProblem(dirName, filename);
				for(int count = 0; count < 1; count++) {
					Collections.shuffle(p.getItems());
					String newFilename = dirName + filename.substring(0, filename.length() - 4) + "_Shuf" + count + ".bpp";
					BufferedWriter writer = new BufferedWriter(new FileWriter(newFilename));
					writer.write(p.toString());
					writer.close();
				}
				boolean success = new File(dirName + filename).delete();
			}
		}
	}

}
