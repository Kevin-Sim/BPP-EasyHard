package bppModel;

import io.FileGetter;
import io.ProblemReader;

/**
 * 
 * Packs to last bin if fits else new bin
 *
 */
public final class NF extends AbstractAlgorithm{
	
	
			
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
		if(solution.getBins().size() > 0) {
			Bin bin = solution.getBins().get(solution.getBins().size() - 1);
			if(bin.getBinWaste() >= item.getLength()) {
				bin.getItems().add(item);
				packed = true;				
			}
		}
		
		if (!packed) {
			Bin bin = addBin(solution);
			bin.getItems().add(item);	
		}					
	}
}
