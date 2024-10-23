package bppModel;

import java.util.ArrayList;
import java.util.Collections;

import bppModel.Bin;
import bppModel.Item;
import bppModel.Problem;
import bppModel.Solution;
import io.FileGetter;
import io.ProblemReader;

public class AWF extends AbstractAlgorithm {

	
	@Override
	public void packNextItem(Solution solution) {
		if(solution == null){
			return;
		}
		if(solution.getRemainingItems() == null || solution.getRemainingItems().size() == 0){
			return;
		}			
		Item item = solution.getRemainingItems().remove(0);
		boolean packed = false;
		
		ArrayList<Bin> bins = new ArrayList<>();
		for(Bin bin : solution.getBins()) {
			int wasteAfterPack = bin.getBinWaste() - item.getLength();
			if(wasteAfterPack >= 0) {
				bins.add(bin);
			}
		}
		
		Collections.sort(bins);
		Bin bin = null;
		if(bins.size() > 1) {
			bin = bins.get(1);
		}else if (bins.size() == 1){
			bin = bins.get(0);
		}else {
			bin = addBin(solution);
		}
		bin.getItems().add(item);			
	}

}
