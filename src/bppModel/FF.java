package bppModel;

import io.FileGetter;
import io.ProblemReader;

public final class FF extends AbstractAlgorithm{
	
	
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
		for(Bin bin : solution.getBins()) {
			if(bin.getBinWaste() >= item.getLength()) {
				bin.getItems().add(item);
				packed = true;
				break;
			}
		}
		if (!packed) {
			Bin bin = addBin(solution);
			bin.getItems().add(item);	
		}					
	}
}
