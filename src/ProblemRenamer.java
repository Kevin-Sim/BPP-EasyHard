import java.io.File;

import io.FileGetter;

public class ProblemRenamer {

	public static void main(String[] args) {
		String[] filenames = FileGetter.getFileNamesRecursive(".\\InstancesNew\\E_120_N_40_60", "", ".bpp");
		System.err.println(filenames.length);
		for(String filename : filenames) {
			File file = new File(filename);
			System.out.println(file.getAbsolutePath());
			String newName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('\\'))
					+ "\\E_120_N_40_60" + filename.substring(filename.lastIndexOf('_'));
			System.out.println(newName);
			File file2 = new File(newName);
			boolean ok = file.renameTo(file2);
			System.out.println("");
			
		}
	}

}
