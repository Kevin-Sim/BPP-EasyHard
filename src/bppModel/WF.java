package bppModel;

import bppModel.Bin;
import bppModel.Item;
import bppModel.Problem;
import bppModel.Solution;
import io.FileGetter;
import io.ProblemReader;

public class WF extends AbstractAlgorithm {

	
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
		Bin worstBin = null;
		int worstWasteAfterPack = -1;
		for(Bin bin : solution.getBins()) {
			int wasteAfterPack = bin.getBinWaste() - item.getLength();
			if(wasteAfterPack >= 0 && wasteAfterPack > worstWasteAfterPack) {
				worstWasteAfterPack = wasteAfterPack;
				worstBin = bin;
			}
		}
		
		if (worstBin != null) {
			worstBin.getItems().add(item);
		}else {
			Bin bin = addBin(solution);
			bin.getItems().add(item);	
		}
	}

}
