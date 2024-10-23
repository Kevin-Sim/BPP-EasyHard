import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import io.StringIO;

public class GoogleOR3ToMyFormat{

	public static void main(String[] args) throws IOException {
		String[] data = StringIO.readStringsFromFile("binpack3.txt");
		int pos = 1;
		
		for(int i = 0; i < 20; i++) {
			String filename = data[pos].trim() + ".bpp";
			pos++;
			String[] properties = data[pos].trim().split(" ");
			String capacity = properties[0];
			String numItems = properties[1];
			StringBuilder sb = new StringBuilder();
			sb.append(numItems + "\r\n" + capacity + "\r\n");
			pos++;
			for(int j = 0; j < 500; j++) {
				sb.append(data[pos + j] + "\r\n");
			}
			pos +=500;
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(sb.toString());
			writer.close();
		}
	}

}
