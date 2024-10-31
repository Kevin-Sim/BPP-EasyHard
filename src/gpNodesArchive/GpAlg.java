package gpNodesArchive;

import java.util.ArrayList;
import java.util.Collections;import java.util.Comparator;

import bppModel.AbstractAlgorithm;
import bppModel.Bin;
import bppModel.Item;
import bppModel.Solution;

/**
 * GP sort next bin
 * could also have side effect next bin
 * .... combo of nodes ff node etc
 * @author 40004938
 *
 */
public class GpAlg extends AbstractAlgorithm {
	private Node node = null;	 
	
	public GpAlg(Node node) {
		this.node = node;
	}


	@Override
	public void packNextItem(Solution solution) {
		if(solution == null){
			return;
		}
		if(solution.getRemainingItems() == null || solution.getRemainingItems().size() == 0){
			return;
		}			
		if(node == null) {
			return;
		}
		
		Item item = solution.getRemainingItems().remove(0);
		
		Wrapper.item = item;
		
		
		/**
		 * Below for GP 
		 * Order feasible bins, pick first, pack or if null then new bin;
		 */
		boolean packed = false;
		ArrayList<Bin> feasibleBins = new ArrayList<>();
		for(Bin bin : solution.getBins()) {
			if(bin.getBinWaste() >= item.getLength()) {
				feasibleBins.add(bin);
			}
		}
		
		/*
		 *	Sort Bins with Gp 
		 */
		
		Collections.sort(feasibleBins, new Comparator<Bin>() {

			@Override
			public int compare(Bin b1, Bin b2) {
				Wrapper.bin = b1;				
				double b1Val = node.eval();
				Wrapper.bin = b2;
				double b2Val = node.eval();
				
				/**
				 * For example -- only feasible bins so if node is bin size ordered by size largest to smallest and
				 * largest selected waste will be minimum
				 * 
				 * Same as BF
				 */
				if(b1Val > b2Val) {
					return -1;
				}else if(b2Val > b1Val) {
					return 1;
				}
				return 0;
			}
			
		});

		if(feasibleBins.size() > 0) {
			feasibleBins.get(0).getItems().add(item);
//			System.out.println(feasibleBins.size());
		}else {
			Bin bin = addBin(solution);
			bin.getItems().add(item);	
		}			
	}

}
