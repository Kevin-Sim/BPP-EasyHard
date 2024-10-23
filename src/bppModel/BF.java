package bppModel;

import bppModel.Bin;
import bppModel.Item;
import bppModel.Problem;
import bppModel.Solution;
import io.FileGetter;
import io.ProblemReader;

public class BF extends AbstractAlgorithm {

	@Override
	public void packNextItem(Solution solution) {
		if (solution == null) {
			return;
		}
		if (solution.getRemainingItems() == null || solution.getRemainingItems().size() == 0) {
			return;
		}
		Item item = solution.getRemainingItems().remove(0);
		boolean packed = false;
		Bin bestBin = null;
		int bestWasteAfterPack = solution.getProblem().getPr_capacity();
		for (Bin bin : solution.getBins()) {
			int wasteAfterPack = bin.getBinWaste() - item.getLength();
			if (wasteAfterPack >= 0 && wasteAfterPack < bestWasteAfterPack) {
				bestWasteAfterPack = wasteAfterPack;
				bestBin = bin;
			}
		}

		if (bestBin != null) {
			bestBin.getItems().add(item);
		} else {
			Bin bin = addBin(solution);
			bin.getItems().add(item);
		}
	}
}
