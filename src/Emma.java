import io.FileGetter;
import io.StringIO;

public class Emma {

	public static void main(String[] args) {
		
		
		StringBuilder sb = new StringBuilder();
		String[] paths = {".\\InstancesNew\\E_250_N_40_60\\", ".\\InstancesNew\\E_250_U_20_100\\"}; ;
		int c = 0;
		for(String path : paths) {
			for(String filename : FileGetter.getFileNames(path, "", ".bpp")) {
				if(filename.contains("AWF")) {
					continue;
				}
				String[] lines = StringIO.readStringsFromFile(path + filename);
				sb.append(filename + ","); 
				
				for(String line : lines) {
					sb.append(line + ",");
				}
				sb.append(filename.substring(filename.lastIndexOf('_') + 1, filename.lastIndexOf('_') + 3) + "\r\n");
			}
		}
		StringIO.writeStringToFile("emma.csv", sb.toString(), false);
	}

}
