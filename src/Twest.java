import java.util.ArrayList;

import bppModel.Item;
import bppModel.Problem;
import bppModel.Solution;
import gp.BinSizeTerminalNode;
import gp.GpAlg;
import io.FileGetter;
import io.StringIO;

public class Twest {

	public static void main(String[] args) {
		
		boolean flag = false;
		for(String str : StringIO.readStringsFromFile("1.txt")) {			
			if(str.contains("<label>")) {
				System.out.println(str);
				flag = true;
			}
			if(flag && str.startsWith("<p>")) {
				System.out.println(str);
				flag = false;
			}
		}
		
		System.exit(-1);
		
		for(String filename : FileGetter.getFileNames("", "AWF", ".bpp")) {
			String[] data = StringIO.readStringsFromFile(filename);
			int capacity = Integer.parseInt(data[1]);
			ArrayList<Item> items = new ArrayList<>();
			for(int i = 2; i < data.length; i++) {
				Item item = new Item(Integer.parseInt(data[i]));
				items.add(item);
			}
			Problem p = new Problem(capacity, filename, items);	
			Solution s = new Solution(p);
			
			/**
			 * BF
			 */
			GpAlg alg = new GpAlg(new BinSizeTerminalNode(null));
			
			/**
			 * change this to GP getbestNode
			 */			
			alg.packRemainingItems(s);
			System.out.print(s.getFitness() + ",");		
			System.out.println();
						
		}
	}
}
