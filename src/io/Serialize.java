package io;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gp.Node;



public class Serialize {

	/**
	 * Saves the JSP to file
	 * @param problem
	 * @param filename
	 */
	public static void saveNode(Node node, String filename) {		
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(node);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}		
	/**
	 * Loads the JSP from file
	 * @param filename
	 * @return
	 */
	public static Node loadNode(String filename) {
		Node node = null;
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			node = (Node) in.readObject();			
			in.close();
			fileIn.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return node;
	}
	
}
