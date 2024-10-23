package io;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StringIO {

	//writes the string to existing file if append is true or a new file if false;
	public static void writeStringToFile(String fileName, String string, boolean append) {
		try {
			FileWriter fw = new FileWriter(fileName, append);
			fw.write(string);
			fw.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.print("\r\n" + e.getMessage() + "\r\n");			
			System.exit(-1);
		}
	}
	
	public static String[] readStringsFromFile(String filename) {
		ArrayList<String> fileData = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while((line = reader.readLine()) != null){
				fileData.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		String[] result = new String[fileData.size()];
		for(int i = 0; i < fileData.size(); i ++){
			result[i] = fileData.get(i);
		}
		return result;
	}
	
}
