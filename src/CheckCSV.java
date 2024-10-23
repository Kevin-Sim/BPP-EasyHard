import ea.Parameters;
import io.FileGetter;
import io.StringIO;

public class CheckCSV {

	public static void main(String[] args) {
		for(String dirname : FileGetter.getDirNamesRecursive("Evolved2024")) {
			outer: for(String filename : FileGetter.getFileNames(dirname, "", ".csv")) {
//				System.out.println(dirname + filename);
				int count = 0;
				for(String line : StringIO.readStringsFromFile(dirname + filename)) {
					String[] data = line.split(",");
					count++;
					if(count == 1) {
						continue;
					}
					if(filename.startsWith("E")) {
						if(data[10].split(" ").length > 1) {
							System.out.println(dirname + " created with Parameters.breakOnBestEqual = true");
							break outer;
						}
					}else if(filename.startsWith("H")) {
						if(data[11].split(" ").length > 1) {
							System.out.println(dirname + " created with Parameters.breakOnBestEqual = true");
							break outer;
						}
					}

				}
				System.out.println(dirname + " created with Parameters.breakOnBestEqual = false");
				break outer;
				
			}
		}
		
	}

}
