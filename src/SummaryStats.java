import io.FileGetter;
import io.StringIO;

public class SummaryStats {

	public static void main(String[] args) {
		String[] dirNames = FileGetter.getDirNamesRecursive("C:\\Users\\Kev\\Dropbox\\Laptop\\BPPWS\\BPP-EasyHard\\Instances2024\\IndependentBenchmarks");
		int fileNum = 0;
		for(String dirName : dirNames) {
			String[] filenames = FileGetter.getFileNames(dirName, "", "IR.csv");
			if(filenames.length == 0) {
				continue;
			}
			String stats = ",";
			for(String filename : filenames) {
				fileNum++;
				String[] lines = StringIO.readStringsFromFile(dirName + filename);
				if(lines.length <= 1) {
					continue;
				}
				if(fileNum == 1) {
					System.out.println(lines[0]);
				}
				dirName.replace("\\", "\\\\");
				String[] parentDirs = dirName.split("\\\\");
				String parentDir = parentDirs[parentDirs.length - 1];
				String lastLine = lines[lines.length - 1].replace("Excess,", "");
				System.out.println(parentDir + "," + lastLine);
			}
		}
	}

}
