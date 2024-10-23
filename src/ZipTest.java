import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipTest {

	public static void main(String[] args) throws IOException {
		String zipFilePath = ".//Instances2024//BPP.zip";

		try (ZipFile zipFile = new ZipFile(zipFilePath)) {
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while (entries.hasMoreElements()) {
		        ZipEntry entry = entries.nextElement();
		        System.out.println(entry.getName());
		        // Check if entry is a directory
		        if (!entry.isDirectory()) {
		            try (InputStream inputStream = zipFile.getInputStream(entry)) {
		            	Scanner scanner = new Scanner(inputStream);
		            	while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
//							System.out.println(line);
						}		                
		            }
		        }
		    }
		}
	}

}
